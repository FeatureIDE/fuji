package Everything;/* Java */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/* JDOM */
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;



/**
 * XML basierende Setting/Einstellungs-Modul.
 * 
 * @author Marcel Jaeschke
 */
public class Setting extends Object implements XMLizeable {
	/* === VARIABLES === */
	/**
	 * The program which ownes this setting.
	 */
	private SettableProgram program = null;
	/**
	 * Das temporäre XML-Dokument im Hauptspeicher.
	 */
	private Document xml;
	/**
	 * Der Pfad zu den Einstellungs-Dateien.
	 */
	private File path;
	/**
	 * Das Modul zum lesen von XML-Dokumenten.
	 */
	private SAXBuilder builder = new SAXBuilder();
	/**
	 * Temporer Speicher welcher die Feature Namen enthält welche wiederum gerade
	 * gealden werden ( nötig für symmetrische Abhänigkeiten).
	 */
	private HashSet cache;
	/**
	 * A flag which indicate that the settings are unchanged since the last
	 * saving/load.
	 */
	private boolean saved = true;

	/* === CONSTRUTORS === */
	/**
	 * Lädt ein Einstellungs-Profil anhand des ersten Modules.
	 * 
	 * @param program An instace of a program which own this setting.
	 * @throws SettingException Wird geworfen wenn das Hauptfeature nicht
	 *           vorhanden ist.
	 */
	public Setting ( SettableProgram program ) throws SettingException {
		this( new File( Setting.getProfileSettingPath( program ) ), program );
	}
	/**
	 * Lädt ein gegebenes Einstellungs-Profil anhand des ersten Modules.
	 * 
	 * @param profile The profile name which will be loaded.
	 * @param program An instace of a program which own this setting.
	 * @throws SettingException Wird geworfen wenn das Hauptfeature nicht
	 *           vorhanden ist.
	 */
	public Setting ( String profile, SettableProgram program ) throws SettingException {
		this( new File( Setting.getProfileSettingPath( profile, program ) ), program );
	}
	/**
	 * Lädt ein Einstellungs-Profil anhand des ersten Modules.
	 * 
	 * @param path Der Pfad zu den Einstellungs-Dateien.
	 * @param program An instace of a program which own this setting.
	 * @throws SettingException Wird geworfen wenn das Hauptfeature nicht
	 *           vorhanden ist.
	 */
	public Setting ( File path, SettableProgram program ) throws SettingException {
		this.path = path;
		this.program = program;
		this.load();
	}
	/* === METHODS === */
	/**
	 * Prüft ob ein Feature bereits geladen wurde.
	 * 
	 * @param featureName Das Feature welches geprüft werden soll.
	 * @return TRUE wenn das Feature bereits geladen ist, ansonsten FALSE.
	 */
	private boolean isFeatureLoaded ( String featureName ) {
		// Nachsehen ob evtl das Feature gerade in einer abhänigkeit geladen wird.
		if ( ((Setting) this).cache.contains( featureName ) ) { return true; }
		String xpath = String.format( "/config/feature[@name='%s']", featureName ); //$NON-NLS-1$
		try {
			return XPath.selectSingleNode( ((Setting) this).xml, xpath ) != null;
		} catch ( JDOMException e ) {
			System.err.println( String.format( "Coudn't check the status of the Feature '%s'.", featureName ) );
			return false;
		}
	}
	/**
	 * (Re-)Load the settings.
	 * 
	 * @throws SettingException Is thrown if there was an error during the loading
	 *           of the main feature.
	 */
	public void load () throws SettingException {
		((Setting) this).cache = new HashSet();
		((Setting) this).xml = new Document( new Element( "config" ) ); //$NON-NLS-1$
		// this.xml.setDocType( new DocType( "config", "validation.dtd" ) );
		((Setting) this).load( ((Setting) this).program.getSettingMainFeature(), false, 0 );
	}
	/**
	 * Läst ein bestimmtes Feature. Testet Versionsnummer und löst Abhänigkeiten
	 * auf.
	 * 
	 * @param featureName Das Feature welches auf Abhänigkeiten geprüft werden
	 *          soll.
	 * @param loadDefault Ein Flag welches angibt ob vom Profil oder die Standard
	 *          Einstellungen geladen werden sollen.
	 * @param minVersion Die minimale Versionsnummer welches das Feature haben
	 *          muss.
	 * @throws SettingException
	 */
	public void load ( String featureName, boolean loadDefault, double minVersion ) throws SettingException {
		double version = 0.0;
		Document feature = null;
		String path = null;
		((Setting) this).builder.setValidation( false ); // TODO activate validation
		((Setting) this).cache.add( featureName );
		try {
			path = String.format( "%s/%s.xml", ( loadDefault ? ((Setting) this).program.getRessourcePath() + "/settings" : ((Setting) this).path.toString() ), featureName ); //$NON-NLS-1$ //$NON-NLS-2$
			if ( loadDefault ) {
				// load from class ressources
				((Setting) this).builder.setValidation( false );
				feature = ((Setting) this).builder.build( ((Setting) this).getClass().getResourceAsStream( path ) );
			} else {
				// load specific config
				feature = ((Setting) this).builder.build( new File( path ) );
			}
			// check the version
			version = ( (Attribute) XPath.selectSingleNode( feature, "/feature/@version" ) ).getDoubleValue(); //$NON-NLS-1$
			if ( minVersion > version ) { throw new SettingException( String.format( "The required version of the feature '%s' isn't avaiable.", featureName ) ); } //$NON-NLS-1$
			// read dependecies
			loadDependecies( feature );
			// add feature
			((Setting) this).xml.getRootElement().addContent( feature.getRootElement().detach() );
			// mark the setting as loaded
		} catch ( FileNotFoundException e ) {
			if ( loadDefault ) {
				e.printStackTrace();
				throw new SettingException( String.format( "The feature '%s' obviously doesn't belong to the program.", featureName ) ); //$NON-NLS-1$
			}
			// Try to load de default
			System.err.println( String.format( "Setting: The feature '%s' doesn't exist in userhome.", featureName ) ); //$NON-NLS-1$
			((Setting) this).load( featureName, true, minVersion );
		} catch ( JDOMParseException e ) {
			System.err.println( String.format( "The configuration of the feature '%s' is corupt.", featureName ) ); //$NON-NLS-1$
			if ( !loadDefault ) {
				((Setting) this).load( "default", true, minVersion ); //$NON-NLS-1$
			}
			// I forget who invoke this exception.
			// } catch ( DataConversionException e ) {
			// throw new SettingException( String.format( "The version of the feature
			// '%s' number isn't valid.", featureName ) );
		} catch ( IOException e ) {
			throw new SettingException( String.format( "The configuration file of the feature '%s' coudn't be found.", featureName ) ); //$NON-NLS-1$
		} catch ( JDOMException e ) {
			throw new SettingException( String.format( "The configuration file of the feature '%s' coudn't be read.", featureName ) ); //$NON-NLS-1$
		} finally {
			((Setting) this).cache.remove( featureName );
		}
	}
	/**
	 * Läst die Abhänigkeiten für ein bestimmtes Feature auf.
	 * 
	 * @param feature Das Feature welches auf Abhänigkeiten geprüft werden soll.
	 * @throws SettingException Wird geworfen wenn das Feature nicht installiert
	 *           ist.
	 */
	private void loadDependecies ( Document feature ) throws SettingException {
		try {
			List dependecies = XPath.selectNodes( feature, "/feature/dependecies/dependency" ); //$NON-NLS-1$
			Iterator iterator = dependecies.iterator();
    		while ( iterator.hasNext() ) {
        		Element dependency = (Element) iterator.next();
				// load dependency
				String dependencyFeature = dependency.getAttributeValue( "name" ); //$NON-NLS-1$
				if ( !isFeatureLoaded( dependencyFeature ) ) {
					((Setting) this).load( dependencyFeature, false, Double.parseDouble( dependency.getAttributeValue( "version" ) ) ); //$NON-NLS-1$
				}
			}
		} catch ( JDOMException e ) {
			throw new SettingException( String.format( "The Feature '%s' isn't installed.", feature ) ); //$NON-NLS-1$
		}
	}
	/**
	 * Forderte eine Feature abhänige Option aus den Einstellungen an.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die angforderte Option.
	 * @return Den Wert der angforderte Option.
	 * @throws SettingException Wird geworfen wenn:
	 *           <ol>
	 *           <li> Wenn das/die Feature/Option nicht exisitert.
	 *           <li> Wenn die Anfrage fehlerhaft ist.
	 *           <li> Wenn keine Einstellungen geladen wurden.
	 *           </ol>
	 */
	private Attribute getOption ( String featureName, String option ) throws SettingException {
		String xpath = String.format( "/config/feature[@name='%s']/options/option[@name='%s']/@value", featureName, option ); //$NON-NLS-1$
		try {
			Attribute attribute = (Attribute) XPath.selectSingleNode( ((Setting) this).xml, xpath );
			if ( attribute == null ) { throw new SettingException( String.format( "The option adressed by '%s::%s' did not exist.", featureName, option ) ); } //$NON-NLS-1$
			return attribute;
		} catch ( JDOMException e ) {
			throw new SettingException( String.format( "Invalid '%s::%s'.", featureName, option ) ); //$NON-NLS-1$
		} catch ( NullPointerException e ) {
			throw new SettingException( "No setting is not loaded." ); //$NON-NLS-1$
		}
	}
	/**
	 * Forderte eine Feature abhänige Option als Wahrheitswert aus den
	 * Einstellungen an.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die angforderte Option.
	 * @return Den Wert der angforderte Option.
	 * @throws SettingException Wenn die Anfrage fehlerhaft oder der Wert kein
	 *           Wahrheitswert ist.
	 * @see Setting#getOption(String, String)
	 */
	public boolean getOptionAsBoolean ( String featureName, String option ) throws SettingException {
		try {
			return getOption( featureName, option ).getBooleanValue();
		} catch ( DataConversionException e ) {
			throw new SettingException( "The value adressed by '%s::%s' is no boolean." ); //$NON-NLS-1$
		}
	}
	/**
	 * Forderte eine Feature abhänige Option als Ganzezahl aus den Einstellungen
	 * an.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die angforderte Option.
	 * @return Den Wert der angforderte Option.
	 * @throws SettingException Wenn die Anfrage fehlerhaft oder der Wert keine
	 *           Ganzezahl ist.
	 * @see Setting#getOption(String, String)
	 */
	public int getOptionAsInteger ( String featureName, String option ) throws SettingException {
		try {
			return getOption( featureName, option ).getIntValue();
		} catch ( DataConversionException e ) {
			throw new SettingException( "The value adressed by '%s::%s' is no intenger." ); //$NON-NLS-1$
		}
	}
	/**
	 * Returns the path to the default settings.
	 * 
	 * @return The path to the default settings.
	 */
	public String getDefaultPath () {
		return ((Setting) this).program.getRessourcePath() + "/settings"; //$NON-NLS-1$
	}
	/**
	 * Forderte eine Feature abhänige Option als Dezimal aus den Einstellungen an.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die angforderte Option.
	 * @return Den Wert der angforderte Option.
	 * @throws SettingException Wenn die Anfrage fehlerhaft oder der Wert keine
	 *           Dezimal ist.
	 * @see Setting#getOption(String, String)
	 */
	public double getOptionAsDouble ( String featureName, String option ) throws SettingException {
		try {
			return getOption( featureName, option ).getDoubleValue();
		} catch ( DataConversionException e ) {
			throw new SettingException( "The value adressed by '%s::%s' is no decimal number." ); //$NON-NLS-1$
		}
	}
	/**
	 * Forderte eine Feature abhänige Option als Text aus den Einstellungen an.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die angforderte Option.
	 * @return Den Wert der angforderte Option.
	 * @throws SettingException Wenn die Anfrage fehlerhaft ist.
	 * @see Setting#getOption(String, String)
	 */
	public String getOptionAsString ( String featureName, String option ) throws SettingException {
		return getOption( featureName, option ).getValue();
	}
	/**
	 * Return a list of possible profiles in the userdir.
	 * 
	 * @return The list of possible profiles in the userdir.
	 */
	public List getProfiles () {
		File profileDir;
		File profileParentDir = new File( String.format( "%s/%s", System.getProperty( "user.home" ), ((Setting) this).program.getProgramName() ) ); //$NON-NLS-1$  //$NON-NLS-2$
		List list = new LinkedList();
		try {
        	String[] tempArray = profileParentDir.list();
        	for (int i = 0; i < tempArray.length; i++ ) {
        		String profile = tempArray[i];
				profileDir = new File( String.format( "%s%s.xml", ((Setting) this).getProfileSettingPath( profile ), ((Setting) this).program.getSettingMainFeature() ) ); //$NON-NLS-1$
				if ( profileDir.isFile() ) {
					list.add( profile );
				}
			}
		} catch ( Exception e ) {}
		return list;
	}
	/**
	 * Returns the "default" profile path.
	 * 
	 * @see Base.miscellaneous.setting.Setting#getProfileSettingPath(String)
	 * @return The default path to the profile settings.
	 */
	public String getProfileSettingPath () {
		return Setting.getProfileSettingPath( ((Setting) this).program );
	}
	/**
	 * Returns the generated path to the profile settings.
	 * 
	 * @param profile The name of the profile which will be used.
	 * @return The generated path to the profile settings.
	 */
	public String getProfileSettingPath ( String profile ) {
		return Setting.getProfileSettingPath( profile, ((Setting) this).program );
	}
	/**
	 * Returns the default profile path.
	 * 
	 * @param program An instace of a program which own this setting.
	 * @return The generated path to the profile settings.
	 */
	public static String getProfileSettingPath ( SettableProgram program ) {
		return Setting.getProfileSettingPath( null, program );
	}
	/**
	 * Returns the generated path to the profile settings.
	 * 
	 * @param profile The name of the profile which will be used. If the name
	 *          NULL, "default" will be used.
	 * @param program An instance of a program which own this setting.
	 * @return The generated path to the profile settings.
	 */
	public static String getProfileSettingPath ( String profile, SettableProgram program ) {
		if ( profile == null ) {
			profile = "default"; //$NON-NLS-1$
		}
		return String.format( "%s/%s/%s/settings/", System.getProperty( "user.home" ), program.getProgramName(), profile ); //$NON-NLS-1$  //$NON-NLS-2$
	}
	/**
	 * Save the setting of a passed feature.
	 * 
	 * @param feature The feature which will be saved.
	 * @param mkdir Flag welches angibt ob Verzeichnisse angelegt werden sollen,
	 *          wenn sie fehlen.
	 * @throws SaveException Is thrown if the feature coudn't save to disk.
	 */
	private void save ( Element feature, boolean mkdir ) throws SaveException {
		String featureName = feature.getAttributeValue( "name" ); //$NON-NLS-1$
		File file = new File( String.format( "%s/%s.xml", ((Setting) this).path, featureName ) ); //$NON-NLS-1$
		Document document = new Document();
		document.addContent( ( (Element) feature.clone() ).detach() );
		FileSaver.save( document, file, mkdir );
	}
	/**
	 * Sichert alle geladenen Features.
	 * 
	 * @param mkdir Flag welches angibt ob Verzeichnisse angelegt werden sollen,
	 *          wenn sie fehlen.
	 * @throws SettingException If it was not possible to save or copy all files.
	 */
	public void save ( boolean mkdir ) throws SettingException {
		File targetFile;
		InputStream orgin;
		OutputStream copy;
		String dtdPath = "/validation.dtd"; //$NON-NLS-1$
		try {
			// Save all features.
			List features = XPath.selectNodes( ((Setting) this).xml, "/config/feature" ); //$NON-NLS-1$
			Iterator iterator = features.iterator();
    		while ( iterator.hasNext() ) {
				((Setting) this).save( (Element) iterator.next(), mkdir );
			}
			// Test for the needed DTD in new diretory.
			targetFile = new File( ((Setting) this).path + dtdPath );
			if ( !targetFile.exists() ) {
				try {
					String originString = ((Setting) this).program.getRessourcePath() + "/settings" + dtdPath;
					orgin = ((Setting) this).getClass().getResourceAsStream( originString ); //$NON-NLS-1$
					copy = new FileOutputStream( targetFile );
					// System.out.println("DEBUG: Copy " + originString + " to " + targetFile.getAbsolutePath() + "... ");
					// Copy the needed DTD in new diretory.
					byte[] buf = new byte[1024];
					int len;
					while ( ( len = orgin.read( buf ) ) > 0 ) {
						copy.write( buf, 0, len );
					}
					orgin.close();
					copy.close();
					// System.out.println("Successful!");
				} catch ( IOException e ) {
					e.printStackTrace();
					throw new SettingException( "Coudn't copy the needed DTD for further validation." ); //$NON-NLS-1$
				}
			}
		} catch ( SaveException e ) {
			throw new SettingException( "Coudn't save the features." ); //$NON-NLS-1$
		} catch ( JDOMException e ) {
			throw new SettingException( "Unable to get the settings from any feature." ); //$NON-NLS-1$
		}
	}
	/**
	 * Gibt eine Option einen neuen Wert.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die zu ändernde Option.
	 * @param newValue Der neue Wahrheitswert der Option
	 * @throws SettingException Wenn die Anfrage fehlerhaft ist.
	 * @see Setting#setOption(String, String, String)
	 */
	public void setOption ( String featureName, String option, boolean newValue ) throws SettingException {
		((Setting) this).setOption( featureName, option, Boolean.toString( newValue ) );
		((Setting) this).saved = false;
	}
	/**
	 * Gibt eine Option einen neuen Wert.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die zu ändernde Option.
	 * @param newValue Der neue Ganzezahl-Wert der Option
	 * @throws SettingException Wenn die Anfrage fehlerhaft ist.
	 * @see Setting#setOption(String, String, String)
	 */
	public void setOption ( String featureName, String option, int newValue ) throws SettingException {
		((Setting) this).setOption( featureName, option, Integer.toString( newValue ) );
		((Setting) this).saved = false;
	}
	/**
	 * Gibt eine Option einen neuen Wert.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die zu ändernde Option.
	 * @param newValue Der neue Dezimal-Wert der Option
	 * @throws SettingException Wenn die Anfrage fehlerhaft ist.
	 * @see Setting#setOption(String, String, String)
	 */
	public void setOption ( String featureName, String option, double newValue ) throws SettingException {
		((Setting) this).setOption( featureName, option, Double.toString( newValue ) );
		((Setting) this).saved = false;
	}
	/**
	 * Gibt eine Option einen neuen Wert.
	 * 
	 * @param featureName Das Feature welches die Option enthalten soll.
	 * @param option Die zu ändernde Option.
	 * @param newValue Der neue Text-Wert der Option
	 * @throws SettingException Wenn die Anfrage fehlerhaft ist.
	 * @see Setting#getOption(String, String)
	 */
	public void setOption ( String featureName, String option, String newValue ) throws SettingException {
		getOption( featureName, option ).setValue( newValue );
		((Setting) this).saved = false;
	}
	/**
	 * Setzt den Pfad zu den Einstellungs-Dateien.
	 * 
	 * @param path Der Pfad zu den Einstellungs-Dateien.
	 * @throws SettingException If the new path is an invalid one.
	 */
	public void setPath ( File path ) throws SettingException {
		if ( path == null ) {
			throw new SettingException( "Coudn't set an empty path for loading and/or saving." ); //$NON-NLS-1$
		} else if ( !path.isDirectory() ) { throw new SettingException( "The path must point to a directory." ); } //$NON-NLS-1$
		((Setting) this).path = path;
	}
	/**
	 * @see Base.miscellaneous.XMLizeable#toXML()
	 */
	public Element toXML () {
		return ( (Document) ((Setting) this).xml.clone() ).detachRootElement();
	}
	/* === GETTERS === */
	/**
	 * Return den Pfad zu den Einstellungs-Dateien.
	 * 
	 * @return Der Pfad zu den Einstellungs-Dateien.
	 */
	public File getPath () {
		return ((Setting) this).path;
	}
	/* === SETTERS === */
	/* === CONVENTION === */
	/**
	 * Returns the absolute path to the settings.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString () {
		return getPath().getAbsolutePath();
	}
	/**
	 * Return the save flag which indicate that the settings are unchanged since
	 * the last saving/load.
	 * 
	 * @return TRUE if all setting are saved since the last saving/load process,
	 *         else FALSE.
	 */
	public boolean isSaved () {
		return ((Setting) this).saved;
	}
}