package zplusplus.exceptions;

/**
 * Handles syntax exceptions
 *
 * @author Zubair Abdul Matin
 */
public class SyntaxException extends CompilerException {
    public SyntaxException(String message, int lineNumber) {
        super(message, lineNumber);
    }
}
