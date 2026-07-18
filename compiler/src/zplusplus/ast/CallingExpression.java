package zplusplus.ast;

import java.util.List;

/**
 * Class for call expression nodes
 *
 * @author Zubair Abdul Matin
 */
public class CallingExpression extends Expression {

    public String callee;
    public List<Expression> arguments;

    /**
     * Constructor for function call AST node
     * @param callee function name
     * @param arguments list of arguments
     * @param lineNumber line number of function call
     */
    public CallingExpression(String callee, List<Expression> arguments, int lineNumber) {
        super(lineNumber);

        this.callee = callee;
        this.arguments = arguments;
    }

    public String getCallee() {
        return callee;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(callee);
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
