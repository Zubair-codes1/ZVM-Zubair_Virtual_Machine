package zplusplus.ast;
import zplusplus.lexer.Token;

/**
 * Binary expression class to hold expressions
 * where an operator acts on two other expressions
 *
 * @author Zubair Abdul Matin
 */
public class BinaryExpression extends Expression {

    // expressions on both sides of the operator
    public Expression left;
    public Expression right;

    // operator
    public final Token operator;

    /**
     * Binary Expression constructor,
     * initialises the left and right expressions
     * along with the token of the operator in between them.
     *
     * @param left left expression
     * @param operator operator token
     * @param right right expression
     * @param lineNumber line number of expression node
     */
    public BinaryExpression(Expression left, Token operator, Expression right, int lineNumber) {
        super(lineNumber);

        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    /**
     * Left expression getter
     * @return left
     */
    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Token getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return left.toString() + " " + operator.toString() + " " + right.toString();
    }
}
