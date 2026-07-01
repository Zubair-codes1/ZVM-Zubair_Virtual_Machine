package zplusplus.ast;

/**
 * Base class for all statement AST nodes.
 *
 * @author Zubair Abdul Matin
 */
public abstract class Statement extends ASTNode{

    /**
     * Constructor for statement class,
     * concrete statement nodes inherit from this class
     *
     * @param lineNumber line number
     */
    protected Statement(int lineNumber) {
        super(lineNumber);
    }
}
