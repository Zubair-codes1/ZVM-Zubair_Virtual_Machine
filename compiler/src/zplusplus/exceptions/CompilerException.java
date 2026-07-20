package zplusplus.exceptions;

/**
 * Class to handle all compiler exceptions
 *
 * @author Zubair Abdul Matin
 */
public class CompilerException extends RuntimeException {
    private String message;
    private int lineNumber;

    /**
     * Passes all compiler execeptions to the Runtime execption
     * @param message error message
     */
    public CompilerException(String message, int lineNumber) {
        super(message + " at line " + lineNumber);
    }

    public String getMessage() {
        return message;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
