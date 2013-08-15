/**
 * TODO add an extra option for choosing composition strategy.  This option can be used in combination with the -typechecker option.  Now the -fopRefs option is used, which actually has the same effect of choosing the family composition strategy, but it's misleading and is actually a hack. 
 * 
 */
package fuji;

import static fuji.Main.OptionName.*;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.core.io.UnsupportedModelException;

import AST.ASTNode;
import AST.CompilationUnit;
import AST.ComposingVisitor;
import AST.Problem;
import AST.Program;
import AST.MethodDecl;

/**
 * The main fuji class. Manages all the work.
 * 
 * @author kolesnik
 */
// TODO rename in Fuji
public class Main implements CompositionContext {

    private Options options; // available fuji command line options
    private CommandLine cmd; // parsed command line options of fuji
    private List<String> backboneCompilerArgs; // JastAddJ arguments

    private ComposingVisitor composingVisitor;
    private SPLStructure spl;
    private boolean generateClassFiles = true;
    private Collection<String> processedCUs = new ArrayList<String>();

    private Collection<Problem> errors = new ArrayList<Problem>();
    private Collection<Problem> warnings = new ArrayList<Problem>();

    /* Timer for -timer option. */
    long startTimeNano;

    /**
     * Starts fuji and processes exceptions.
     * 
     * @param args
     *            command line arguments.
     */
    public static void main(String[] args) {
        byte exitcode = 0;
        try {
            Main fuji = new Main(args, null, null);
            if (!(fuji.cmd.hasOption(HELP) || fuji.cmd.hasOption(VERSION))) {
                fuji.process();
            }
        } catch (IllegalArgumentException e) {
            printError(e.getMessage());
            exitcode = 2;
        } catch (ParseException e) {
            printError(e.getMessage() + "\n");
            exitcode = 1;
        } catch (IOException e) {
            printError(e.getMessage() + "\n");
            exitcode = 1;
        } catch (FeatureDirNotFoundException e) {
            printError(e.getMessage());
            exitcode = 1;
        } catch (SyntacticErrorException e) {
            printError(e.getMessage());
            exitcode = 1;
        } catch (SemanticErrorException e) {
            printError(e.getMessage());
            exitcode = 1;
        } catch (CompilerWarningException e) {
            System.err.println(e.getMessage());
            exitcode = 1;
        } catch (CompositionErrorException e) {
            printError(e.getMessage() + "\n");
            exitcode = 1;
        } catch (UnsupportedModelException e) {
            printError(e.getMessage() + "\n");
            exitcode = 1;
        } finally {
            if (exitcode != 0) {
                System.exit(exitcode);
            }
        }
    }

    /**
     * Default constructor. Manages all the fuji's work.
     * 
     * @param args
     *            command line args.
     * @param featureModel
     *            feature model
     * @param featuresList
     *            list of features to be composed. The order of features in this
     *            list specifies the order of composition of the corresponding
     *            feature modules.
     * 
     * @throws IllegalArgumentException
     * @throws ParseException
     * @throws SyntacticErrorException
     * @throws FeatureDirNotFoundException
     * @throws IOException
     * @throws SemanticErrorException
     * @throws CompilerWarningException
     * @throws UnsupportedModelException
     */
    public Main(String[] args, FeatureModel featureModel,
            List<String> featuresList) throws IllegalArgumentException,
            ParseException, IOException, FeatureDirNotFoundException,
            SyntacticErrorException, SemanticErrorException,
            CompilerWarningException, UnsupportedModelException {

        /* Setup timer start. */
        startTimeNano = getCpuTime();

        /* Initialize options and parse the command line. */
        options = initOptions();
        cmd = new GnuParser().parse(options, args);

        /* help, version */
        if (cmd.hasOption(HELP)) {
            printHelp(options);
            return;
        } else if (cmd.hasOption(VERSION)) {
            System.out.println(toolName() + " v." + version() + " "
                    + projectURL());
            return;
        }

        testOptions();
        String featuresFileOrFeatureModelPathname = null;
        if (cmd.getArgList().size() == 1) {
            featuresFileOrFeatureModelPathname = cmd.getArgs()[0];
        }

        /* Decide if the class file should be generated. */
        if (cmd.hasOption(EXT_INTROS) || cmd.hasOption(EXT_REFS)
                || cmd.hasOption(EXT_MEASURE_ASTS_SOURCE)
                || cmd.hasOption(DEPDEGREE) || cmd.hasOption(INFOFLOW)) {
            generateClassFiles = false;
        }

        /* Select a feature composition strategy. */
        if ((cmd.hasOption(COMPOSTION_STRATEGY) && cmd.getOptionValue(
                COMPOSTION_STRATEGY).equals(COMPOSTION_STRATEGY_ARG_FAMILY))
                || cmd.hasOption(EXT_INTROS) || cmd.hasOption(EXT_REFS)
                || cmd.hasOption(DEPDEGREE) || cmd.hasOption(INFOFLOW)) {

            /* Family-based. */
            composingVisitor = new AST.ComposingVisitorRSF();

        } else {

            /* Product-based. */
            composingVisitor = new AST.ComposingVisitorNormal();
        }

        String basedir = cmd.getOptionValue(BASEDIR,
                System.getProperty("user.dir"));

        /* Decide where the features-list/feature-model come from. */
        if (featuresFileOrFeatureModelPathname != null) {

            if (cmd.hasOption(TYPECHECKER)) {

                /* The provided file is a feature model. */
                spl = new SPLStructure(basedir,
                        featuresFileOrFeatureModelPathname, null,
                        !cmd.hasOption(SPL_HAS_NO_VARIABILITY));

            } else {

                /* The provided file is a features list. */
                spl = new SPLStructure(basedir, null,
                        featuresFileOrFeatureModelPathname,
                        !cmd.hasOption(SPL_HAS_NO_VARIABILITY));
            }

        } else {

            /*
             * Take the features list and the model from the parameters supplied
             * to the constructor.
             */
            spl = new SPLStructure(basedir, featureModel, featuresList,
                    !cmd.hasOption(SPL_HAS_NO_VARIABILITY));
        }

        backboneCompilerArgs = constructBackboneCompilerArgs();
    }

    /**
     * Do the processing of the product line according to the command line
     * options.
     */
    public void process() throws SemanticErrorException,
            CompilerWarningException, IOException, IllegalArgumentException,
            SyntacticErrorException {

        Composition composition = new Composition(this);
        Program ast = composition.composeAST();

        /* Setup timer stop. */
        if (cmd.hasOption(TIMER)) {
            System.out.println("Time_AST_construction_ms: "
                    + ((getCpuTime() - startTimeNano) / 1000000));
        }

        if (cmd.hasOption(INFOFLOW)) {
			ast.printInfoFlow();
        }
	
        if (cmd.hasOption(DEPDEGREE)) {
        	ast.printDepDegree();
        }	
	
        if (cmd.hasOption(TYPECHECKER)) {
            /* Type check timer start. */
            if (cmd.hasOption(TIMER)) {
                startTimeNano = getCpuTime();
            }
            typecheckAST(ast);

            /* Type check timer stop. */
            if (cmd.hasOption(TIMER)) {
                System.out.println("Time_typecheck_ms: "
                        + ((getCpuTime() - startTimeNano) / 1000000));
                System.out.println("Found_Errors: " + errors.size());
            }

        } else {
            /* AST processing timer start. */
            if (cmd.hasOption(TIMER)) {
                startTimeNano = getCpuTime();
            }
            processAST(ast);

            /* AST processing timer stop. */
            if (cmd.hasOption(TIMER)) {
                System.out.println("AST_processing_time_ms: "
                        + ((getCpuTime() - startTimeNano) / 1000000));
                System.out.println("Found_Errors: " + errors.size());
            }
        }
    }

    /*
     * Do sanity checks on the command line options.
     */
    private void testOptions() throws IllegalArgumentException {

        /* Check compatibility of options. */
        if ((cmd.hasOption(EXT_INTROS) || cmd.hasOption(EXT_REFS)
        		|| cmd.hasOption(DEPDEGREE) || cmd.hasOption(INFOFLOW))
               		&& cmd.hasOption(EXT_MEASURE_ASTS_SOURCE)) {

            /*
             * Intros/Refs/Depdegree/Infoflow calculation requires 
			 * ComposingVisitorRSF and ASTS calculation requires 
			 * ComposingVisitorNormal
             */
            throw new IllegalArgumentException("Incompatible options:"
                    + EXT_INTROS + "/" + EXT_REFS + "/" + DEPDEGREE + "/" 
                    + INFOFLOW + "and" + EXT_MEASURE_ASTS_SOURCE + "\n");
        }

        /* Check if the name of the compostion strategy is known. */
        if (cmd.hasOption(COMPOSTION_STRATEGY)) {
            String value = cmd.getOptionValue(COMPOSTION_STRATEGY);
            if (!value.equals(COMPOSTION_STRATEGY_ARG_FAMILY)
                    && !value.equals(COMPOSTION_STRATEGY_ARG_PRODUCT)) {
                throw new IllegalArgumentException(
                        "Unknown compostion strategy: " + value + "\n");
            }
        }

        /*
         * Check the file argument (i.e., features file or feature model). If in
         * programmatic mode, then the file argument can be omitted.
         */
        if (((cmd.getArgList().size() == 0) && cmd.hasOption(TYPECHECKER) && !cmd
                .hasOption(PROG_MODE))) {
            throw new IllegalArgumentException("No model file is specified.\n");
        } else if (((cmd.getArgList().size() == 0) && !cmd.hasOption(PROG_MODE))) {
            throw new IllegalArgumentException(
                    "No features file is specified.\n");
        } else if (cmd.getArgList().size() > 1) {
            throw new IllegalArgumentException("Too many arguments: "
                    + cmd.getArgList() + "\n");
        }
    }

    @SuppressWarnings("static-access")
    private Options initOptions() {
        Options ops = new Options();
        ops.addOption(OptionBuilder.hasArg().withArgName("path")
                .withDescription("Override location of bootstrap class files")
                .create(BOOTCLASSPATH));
        ops.addOption(OptionBuilder
                .hasArg()
                .withArgName("path")
                .withDescription(
                        "Specify where to find user class files and "
                                + "annotation processors").withLongOpt("cp")
                .create(CLASSPATH));
        ops.addOption(OptionBuilder
                .hasArg()
                .withArgName("directory")
                .withDescription("Specify where to place generated class files")
                .create(D));
        ops.addOption(OptionBuilder.hasArg().withArgName("dirs")
                .withDescription("Override location of installed extensions")
                .create(EXTDIRS));
        ops.addOption(OptionBuilder.withDescription(
                "Print a synopsis of standard options").create(HELP));
        ops.addOption(OptionBuilder.withDescription("Generate no warnings")
                .create(NOWARN));
        ops.addOption(OptionBuilder.hasArg().withArgName("path")
                .withDescription("Specify where to find input source files")
                .create(SOURCEPATH));
        ops.addOption(OptionBuilder.withDescription("Version information")
                .create(VERSION));
        ops.addOption(OptionBuilder.hasArg().withArgName("directory")
                .withDescription("Specyfy where to find feature modules")
                .create(BASEDIR));
        ops.addOption(OptionBuilder.withDescription("Print access statistics")
                .create(EXT_ACCESSCOUNT));
        ops.addOption(OptionBuilder.withDescription(
                "Print introduces relations").create(EXT_INTROS));
        ops.addOption(OptionBuilder.withDescription("Print ref relations")
                .create(EXT_REFS));
        ops.addOption(OptionBuilder
                .hasArg()
                .withArgName("product|family")
                .withDescription(
                        "Specify a composition strategy to be used to compose features. The default strategy is the product-based")
                .create(COMPOSTION_STRATEGY));
        ops.addOption(OptionBuilder
                .hasArg()
                .withArgName("directory")
                .withDescription(
                        "Make source to source translation "
                                + "and write generated java code to the "
                                + "specified directory").create(SRC));
        ops.addOption(OptionBuilder.withDescription(
                "Instantiate fuji in programmatic mode.  This "
                        + "mode is used to control fuji from "
                        + "another program and not by using command-line.")
                .create(PROG_MODE));
        ops.addOption(OptionBuilder
                .withDescription(
                        "Calculate the ASTS measure (only CompilationUnits that come from source files are analyzed).")
                .create(EXT_MEASURE_ASTS_SOURCE));
        ops.addOption(OptionBuilder.withDescription(
                "Instantiate fuji in typechecker mode. A file containing "
                        + "the feature model is expected instead of the file "
                        + "containing a list of features.").create(TYPECHECKER));
        ops.addOption(OptionBuilder
                .withDescription(
                        "If this option is set, fuji assumes that the SPL "
                                + "has no variability (i.e. consists only of one product). This option works only with '"
                                + TYPECHECKER + "' option.").create(
                        SPL_HAS_NO_VARIABILITY));
        ops.addOption(OptionBuilder
                .withDescription(
                        "Measure time (in milliseconds) used for AST composition and a subsequent processing step. Output the measurements to stdout.").create(TIMER));
        ops.addOption(OptionBuilder
                .withDescription(
                        "'original' method calls are treated as normal method calls (used in feature-based type checking). This option works only with '"
                                + TYPECHECKER + "' option.").create(
                        IGNORE_ORIGINAL));
        ops.addOption(OptionBuilder
				.withDescription(
						"Calculate and print DepDegrees")
				.withLongOpt("dd").create(DEPDEGREE));
		ops.addOption(OptionBuilder
				.withDescription(
						"Calculate and print Informational Flow")
				.withLongOpt("if").create(INFOFLOW));
        return ops;
    }

    /**
     * Construct command-line arguments for the backbone compiler (currently
     * JastAddJ) using command-line arguments supplied to fuji.
     */
    private ArrayList<String> constructBackboneCompilerArgs() {
        ArrayList<String> args = new ArrayList<String>();
        if (cmd.hasOption(BOOTCLASSPATH)) {
            args.add("-" + BOOTCLASSPATH);
            args.add(cmd.getOptionValue(BOOTCLASSPATH));
        }

        String classpath = "";
        if (cmd.hasOption(D)) {
            args.add("-" + D);
            String d = cmd.getOptionValue(D);

            /*
             * If the destination directory for generated class files is set,
             * add it to the class path.
             * 
             * Reason: if class A uses class B and B was composed and compiled
             * already, the composed and compiled class from the -d directory
             * will be retrieved for A compilation and not some incomplete base
             * or refinement class.
             * 
             * NOTE: It is not default javac behaviour.
             */
            classpath += ":" + d;
            args.add(d);
        }
        for (String pathname : spl.getFeatureModulePathnames()) {
            classpath += ":" + pathname;
        }
        if (cmd.hasOption(BASEDIR)) {
            classpath += ":" + cmd.getOptionValue(BASEDIR);
        }
        classpath += ":.";
        if (cmd.hasOption(CLASSPATH)) {
            classpath += ":" + cmd.getOptionValue(CLASSPATH);
        }
        args.add("-" + CLASSPATH);
        args.add(classpath);

        if (cmd.hasOption(EXTDIRS)) {
            args.add("-" + EXTDIRS);
            args.add(cmd.getOptionValue(EXTDIRS));
        }
        if (cmd.hasOption(SOURCEPATH)) {
            args.add("-" + SOURCEPATH);
            args.add(cmd.getOptionValue(SOURCEPATH));
        }

        /* Additional flags, that are not user definable options. */
        if (cmd.hasOption(EXT_REFS)) {
            args.add(AST.IntrosRefsUtil.ALLOW_MULTIPLE_DECLARATIONS);
        }
        return args;
    }

    public void typecheckAST(Program ast) throws SemanticErrorException,
            CompilerWarningException {

        if (cmd.hasOption(IGNORE_ORIGINAL)) {
            ast.setIgnoreOriginal(true);
        }
        ast.splErrorCheck(errors, warnings);

        throwErrorsAndWarnings();
    }

    /**
     * Process the AST of the variant according to the user options.
     * 
     * @throws SyntacticErrorException
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws SemanticErrorException
     * @throws CompilerWarningException
     */
    public void processAST(Program ast) throws IOException,
            IllegalArgumentException, SyntacticErrorException,
            SemanticErrorException, CompilerWarningException {

        @SuppressWarnings("unchecked")
        Iterator<CompilationUnit> iter = ast.compilationUnitIterator();
        while (iter.hasNext()) {
            CompilationUnit cu = iter.next();
            if (cu.fromSource()) {
                processCU(cu, errors, warnings);
            }
        }
        throwErrorsAndWarnings();

        /* Do analysis on the whole AST. */
        if (cmd.hasOption(EXT_MEASURE_ASTS_SOURCE)) {
            System.out.println(ast.measureASTSSource());
        }
    }

    /**
     * If there are compilation errors or warnings, throw them as exceptions. In
     * programmatic mode throwing errors and warnings is suppressed (the main
     * program can use corresponding getters and setters to get errors and
     * warnings and process them).
     * 
     * @throws SemanticErrorException
     * @throws CompilerWarningException
     */
    private void throwErrorsAndWarnings() throws SemanticErrorException,
            CompilerWarningException {
        if (!cmd.hasOption(PROG_MODE)) {
            if (!errors.isEmpty()) {
                StringBuilder message = new StringBuilder();
                for (Object o : errors) {
                    message.append(o + "\n");
                }
                throw new SemanticErrorException(message.toString());
            }
            if (!warnings.isEmpty() && !cmd.hasOption(NOWARN)) {
                StringBuilder message = new StringBuilder();
                for (Object o : warnings) {
                    message.append(o + "\n");
                }
                throw new CompilerWarningException(message.toString());
            }
        }
    }

    /**
     * Process a CompilationUnit according to the user options. And remember
     * processed CUs.
     */
    @SuppressWarnings("rawtypes")
    private void processCU(CompilationUnit cu, Collection errors,
            Collection warnings) throws IOException, IllegalArgumentException,
            SyntacticErrorException {

        if (cmd.hasOption(EXT_INTROS)
                && spl.isBaseRoleSourcefile(cu.pathName())
                && !processedCUs.contains(cu.pathName())) {

            cu.printIntros(spl.getFeatureModulePathnames());
        }
        if (cmd.hasOption(EXT_REFS) && spl.isBaseRoleSourcefile(cu.pathName())
                && !processedCUs.contains(cu.pathName())) {

            cu.printRefs(spl.getFeatureModulePathnames());
        }
        if (generateClassFiles) {

            /*
             * Check for syntactical errors. (They have been checked in
             * SPLStructure before while creating dependency graphs.
             * 
             * TODO: collect syntactical errors for _all_ CUs before
             * composition. See also bug revealed by
             * ReportParseError2Compile.test
             */
            Collection parseErrors = cu.parseErrors();
            if (!parseErrors.isEmpty()) {
                StringBuilder message = new StringBuilder();
                for (Object o : parseErrors) {
                    message.append(o + "\n");
                }
                throw new SyntacticErrorException(message.toString());
            }

            /*
             * Check for static semantic errors.
             */
            cu.errorCheck(errors, warnings);
            if (errors.isEmpty()) {

                /*
                 * Write source code for the compilation unit. This must be done
                 * before cu.transformation() is called.
                 */
                if (cmd.hasOption(SRC) && cu.fromRole()) {
                    generateSourcefile(cmd.getOptionValue(SRC), cu);
                }
                if (cmd.hasOption(EXT_ACCESSCOUNT)) {
                    // TODO refactor and insert the code from the pre-fuji
                    // implementation.
                }
                cu.transformation();
                cu.generateClassfile();
            }
        }
        		
		processedCUs.add(cu.pathName());
	}


    /**
     * Generate source code from the compilation unit and write it to a file in
     * the destination directory.
     * 
     * NOTE: Enum constructors are not printed (JastAddJ bug
     * http://bugs.jastadd.org/cgi-bin/bugzilla/show_bug.cgi?id=42 ).
     * 
     * NOTE: do not run cu.transformation() before this. It would add some
     * JastAddJ specific nodes to the AST that would make the generated code
     * invalid.
     */
    private void generateSourcefile(String destination, CompilationUnit cu)
            throws IOException, IllegalArgumentException {
        String destinationDir = new File(destination).getCanonicalPath();
        if (!spl.getBasedirPathname().contains(destinationDir)) {
            cu.generateSourcefile(destinationDir);
        } else {
            throw new IllegalArgumentException(
                    "Destination directory for generated ("
                            + destinationDir
                            + ") source files coincide with a feature module directory.");
        }
    }

	/**
	 * Get backbone compiler (currently JastAddJ) arguments (options) that were
	 * extracted from the fujis command line.
	 * 
	 * @return compiler arguments (options).
	 */
	public String[] getBackboneCompilerArgs() {
		return backboneCompilerArgs.toArray(new String[backboneCompilerArgs
				.size()]);
	}

	/**
	 * Returns composition strategy visitor to be used in the AST composition
	 * process.
	 * 
	 * @return composition strategy visitor.
	 */
	public ComposingVisitor getComposingVisitor() {
		return composingVisitor;
	}

	/**
	 * Returns an object representing the SPL structure.
	 * 
	 * @return object representing SPL structure.
	 */
	public SPLStructure getSPLStructure() {
		return spl;
	}

	/**
	 * A factory method for Composition objects.
	 * 
	 * @param compContext
	 *            a CompositionContext to initialize the Composition instance
	 *            with.
	 * 
	 * @return a instance of Composition object initialized with the
	 *         corresponding composition context.
	 */
	public Composition getComposition(CompositionContext compContext) {
		return new Composition(compContext);
	}

  	/**
     * Return compiler errors.
     * 
     * @return a unmodifiable collection with compiler errors.
     */
    public Collection<Problem> getErrors() {
        return Collections.unmodifiableCollection(errors);
    }

    /**
     * Return compiler warnings.
     * 
     * @return an unmodifiable collection with compiler warnings.
     */
    public Collection<Problem> getWarnings() {
        return Collections.unmodifiableCollection(warnings);
    }
    
	/**
     * Enumerates all options (and eventually their arguments) accepted by fuji.
	 * 
	 * @author kolesnik
	 */
	public static class OptionName {
		public static final String BOOTCLASSPATH = "bootclasspath"; //
		public static final String CLASSPATH = "classpath"; //
		public static final String D = "d"; //
		public static final String EXTDIRS = "extdirs"; //
		public static final String HELP = "help"; //
		public static final String NOWARN = "nowarn";
		public static final String SOURCEPATH = "sourcepath"; //
		public static final String VERSION = "version";

        /* Fuji-specific options. */
 		public static final String BASEDIR = "basedir";
        public static final String EXT_ACCESSCOUNT = "fopStatistic"; //
        public static final String EXT_INTROS = "fopIntroduces"; //
        public static final String EXT_REFS = "fopRefs"; //
        public static final String SRC = "src";
        public static final String PROG_MODE = "progmode";
        public static final String EXT_MEASURE_ASTS_SOURCE = "mASTS";
        public static final String COMPOSTION_STRATEGY = "compstrategy";
        public static final String COMPOSTION_STRATEGY_ARG_FAMILY = "family";
        public static final String COMPOSTION_STRATEGY_ARG_PRODUCT = "product";

        /* TYPECHECKER */
        public static final String TYPECHECKER = "typechecker";
        public static final String SPL_HAS_NO_VARIABILITY = "novariability";
        public static final String TIMER = "timer";
        public static final String IGNORE_ORIGINAL = "ignoreOriginal";
		
		/* Static analysis */
		public static final String DEPDEGREE = "depdegree";
		public static final String INFOFLOW = "infoflow";
	}

	private static void printError(String message) {
		System.err.println("Errors:");
		System.err.print(message);
	}

    private static void printHelp(Options options) {
        new HelpFormatter().printHelp(72, toolName() + " features_file", "",
                options, "", true);
    }

	private static String toolName() {
		return "fuji";
	}

	private static String projectURL() {
		return "http://www.fosd.de/fuji";
	}

	private static String version() {
        return "2013-07-08";
    }

    /**
     * Get CPU time in nanoseconds. Measuring CPU times in java:
     * http://nadeausoftware
     * .com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking
     */
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean
                .getCurrentThreadCpuTime() : 0L;
	}
}
