/**
 * Base class for all statement AST nodes.
 *
 * @author Zubair Abdul Matin
 */
public abstract class Statement extends ASTNode{

    protected Statement(int lineNumber) {
        super(lineNumber);
    }
}
