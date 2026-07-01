package zplusplus.ast;

/**
 * zplusplus.ast.Expression class for literals such as strings, integers
 * and booleans.
 *
 * @author Zubair Abdul Matin
 */
public class LiteralExpression extends Expression {
    // holds either an integer, boolean or string
    private final Object value;

    /**
     * Constructor for literal expressions
     *
     * @param value the actual value of the literal
     * @param lineNumber the line number of the literal
     */
    public LiteralExpression(Object value, int lineNumber) {
        super(lineNumber);
        this.value = value;
    }

    /**
     * Gets the literals value
     * @return literal value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the objects value as a string
     * @return string value of object
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
