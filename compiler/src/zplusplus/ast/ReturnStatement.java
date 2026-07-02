package zplusplus.ast;

/**
 * Class for return statement nodes,
 * holds only the return value of the expression,
 * expression value can be null
 *
 * @author Zubair Abdul Matin
 */
public class ReturnStatement extends Statement {

    public final Expression returnValue;

    public ReturnStatement(Expression returnValue, int lineNumber) {
        super(lineNumber);

        this.returnValue = returnValue;
    }

    public Expression getReturnValue() {
        return returnValue;
    }

    @Override
    public String toString() {
        if (returnValue == null) {
            return "return;";
        }
        return "return " + returnValue.toString() + ";";
    }
}
