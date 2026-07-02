package zplusplus.ast;

/**
 * Class to handle assignments
 *
 * @author Zubair Abdul Matin
 */
public class AssignmentStatement extends Statement {

    private final String name;  // name of variable
    private final Expression expression;    // expresssion

    public AssignmentStatement(String name, Expression expression, int lineNumber) {
        super(lineNumber);

        this.name = name;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }
    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return name + " = " + expression.toString();
    }
}
