package zplusplus.ast;

/**
 * Expressions on their own lines are considered valid
 * statements such as a function call.
 */
public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression, int lineNumber) {
        super(lineNumber);

        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression.toString() + ";";
    }
}
