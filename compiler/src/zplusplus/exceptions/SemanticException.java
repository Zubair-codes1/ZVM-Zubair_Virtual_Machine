package zplusplus.exceptions;

/**
 * Semantic Exception class
 *
 * @author Zubair Abdul Matin
 */
public class SemanticException extends CompilerException {
    public SemanticException(String message, int lineNumber) {
        super(message, lineNumber);
    }
}
