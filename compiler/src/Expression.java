/**
 * Class to hold the expressions, all other
 * concrete expression classes inherit from this one
 *
 * @author Zubair Abdul Matin
 */
public abstract class Expression extends ASTNode {

    protected Expression(int lineNumber) {
        super(lineNumber);
    }
}