package zplusplus.ast;

/**
 * Class to explicitly wrap things in parentheses
 *
 * @author Zubair Abdul Matin
 */
public class GroupingExpression extends Expression {
    private final Expression expression;

    public GroupingExpression(Expression expression, int lineNumber) {
        super(lineNumber);

        this.expression = expression;
    }

    public  Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "(" + expression.toString() + ")";
    }
}
