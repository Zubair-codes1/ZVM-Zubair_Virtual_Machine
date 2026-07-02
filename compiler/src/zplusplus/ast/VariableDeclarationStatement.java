package zplusplus.ast;

/**
 * Class to handle variable declarations
 *
 * @author Zubair Abdul Matin
 */
public class VariableDeclarationStatement extends Statement {

    private final String typeName;
    private final String varName;

    public VariableDeclarationStatement(String typeName, String varName, int lineNumber) {
        super(lineNumber);

        this.typeName = typeName;
        this.varName = varName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getVarName() {
        return varName;
    }

    @Override
    public String toString() {
        return typeName + " " + varName + ";";
    }
}
