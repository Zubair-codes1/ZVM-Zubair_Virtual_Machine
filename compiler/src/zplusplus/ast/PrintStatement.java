package zplusplus.ast;

public class PrintStatement extends Statement {

    public PrintStatement(int lineNumber) {
        super(lineNumber);
    }

    @Override
    public String toString() {
        return "print";
    }
}
