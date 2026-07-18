package zplusplus.ast;

public class BreakStatement extends Statement {

    public BreakStatement(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public String toString() {
        return "break;";
    }
}
