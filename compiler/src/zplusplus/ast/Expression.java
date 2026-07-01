package zplusplus.ast;

/**
 * Class to hold the expressions, all other
 * concrete expression classes inherit from this one
 *
 * @author Zubair Abdul Matin
 */
public abstract class Expression extends ASTNode {

    /**
     * Constructor for expression class,
     * concrete expresssions inherit from this
     * @param lineNumber line number of expression node
     */
    protected Expression(int lineNumber) {
        super(lineNumber);
    }
}
