package zplusplus.ast;
import zplusplus.lexer.Token;

/**
 * Class to handle unary expressions / operators nodes
 * (operators that take one expression e.g. !x)
 *
 * @author Zubair Abdul Matin
 */
public class UnaryExpression extends Expression {
    // operator
    private final Token operator;
    // expression (right side of operator)
    private final Expression rightExpression;

    /**
     * Constructor to intitialise a unary expression node
     *
     * @param operator token of the operator
     * @param rightExpression expression to the right of the operator
     * @param lineNumber line number of the token
     */
    public UnaryExpression(Token operator, Expression rightExpression, int lineNumber) {
        super(lineNumber);

        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    /**
     * Operator getter
     * @return operator
     */
    public Token getOperator() {
        return operator;
    }

    /**
     * Expression getter
     * @return expression
     */
    public Expression getRightExpression() {
        return rightExpression;
    }

    /**
     * Prints unary expression in a good format
     * for debugging.
     * @return operator + expression
     */
    @Override
    public String toString() {
        return operator.toString() + " " + rightExpression.toString();
    }
}
