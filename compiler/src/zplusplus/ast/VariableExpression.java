package zplusplus.ast;

/**
 * Concrete class to handle all variable expressions
 *
 * @author Zubair Abdul Matin
 */
public class VariableExpression extends Expression {
    private final String name;

    /**
     * Constructor for the variable expression class
     * @param name name of the variable
     * @param lineNumber line number of the variable
     */
    public VariableExpression(String name, int lineNumber) {
        super(lineNumber);
        this.name = name;
    }

    /**
     * Gets the name of the variable
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Handles debugging of the variable expression
     * !complete
     * @return name
     */
    @Override
    public String toString() {
        return name;
    }
}
