package zplusplus.ast;

public class PrintStatement extends Statement {

    private final Expression expression;

    public PrintStatement(Expression expression, int lineNumber) {
        super(lineNumber);

        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
