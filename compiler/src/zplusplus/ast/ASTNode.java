package zplusplus.ast;

/**
 * Base class for all Abstract Syntax Tree nodes
 *
 * @author Zubair Abdul Matin
 */
public abstract class ASTNode {

    private final int lineNumber;

    /**
     * Constructor to make an AST node,
     * only accessible by the subclasses
     * @param lineNumber line number of the node
     */
    protected ASTNode(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Getting the line number of the AST node
     * @return line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Displays the node in a clean manner for
     * debugging purposes
     * @return the nodes data formatted into a string
     */
    @Override
    public abstract String toString();
}
