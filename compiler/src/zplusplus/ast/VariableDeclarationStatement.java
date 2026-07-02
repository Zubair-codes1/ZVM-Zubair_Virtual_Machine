package zplusplus.ast;

/**
 * Class to handle variable declarations
 *
 * @author Zubair Abdul Matin
 */
public class VariableDeclarationStatement extends Statement {

    private final String typeName;
    private final String varName;
    private final Expression initializer;

    public VariableDeclarationStatement(String typeName, String varName, Expression initializer, int lineNumber) {
        super(lineNumber);

        this.typeName = typeName;
        this.varName = varName;
        this.initializer = initializer;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getVarName() {
        return varName;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    public String toString() {
        if (initializer != null) {
            return typeName + " " + varName + " = " + initializer.toString();
        }
        return typeName + " " + varName + ";";
    }
}
