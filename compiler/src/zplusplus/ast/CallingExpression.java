package zplusplus.ast;

import java.util.List;

/**
 * Class for call expression nodes
 *
 * @author Zubair Abdul Matin
 */
public class CallingExpression extends Expression {

    public Expression callee;
    public List<Expression> arguments;

    public CallingExpression(Expression callee, List<Expression> arguments, int lineNumber) {
        super(lineNumber);

        this.callee = callee;
        this.arguments = arguments;
    }

    public Expression getCallee() {
        return callee;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(callee.toString());
        sb.append(".(");
        for (int i = 0; i < arguments.size(); i++) {
            sb.append(arguments.get(i).toString());

            if (i < arguments.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(");");
        return sb.toString();
    }
}
