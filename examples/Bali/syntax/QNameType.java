// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class QNameType extends AST_TypeName {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public AST_QualifiedName getAST_QualifiedName () {
        
        return (AST_QualifiedName) arg [0] ;
    }

    public Dims getDims () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (Dims) node : null ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public QNameType setParms (AST_QualifiedName arg0, AstOptNode arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* AST_QualifiedName */
        arg [1] = arg1 ;            /* [ LOOKAHEAD(2) Dims ] */
        
        InitChildren () ;
        return (QNameType) this ;
    }

}
