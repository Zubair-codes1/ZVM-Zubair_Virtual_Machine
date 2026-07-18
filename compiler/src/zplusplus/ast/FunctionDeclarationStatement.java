package zplusplus.ast;

import java.util.List;

/**
 * Class for function declaration statement
 * includes return type, name, paramaters and body of statements
 *
 * @author Zubair Abdul Matin
 */
public class FunctionDeclarationStatement extends Statement {

    public final String returnType;
    public final String name;
    public final List<Parameter> parameters;
    public final Statement body;

    public FunctionDeclarationStatement(String returnType, String name, List<Parameter> parameters, Statement body, int lineNumber) {
        super(lineNumber);

        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.body = body;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "def " + returnType + " " + name + " (" + parameters.toString() + ") {\n" + body.toString() + "\n}";
    }
}
