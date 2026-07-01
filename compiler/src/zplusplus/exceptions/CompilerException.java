package zplusplus.exceptions;

/**
 * Class to handle all compiler exceptions
 *
 * @author Zubair Abdul Matin
 */
public class CompilerException extends RuntimeException {
    /**
     * Passes all compiler execeptions to the Runtime execption
     * @param message error message
     */
    public CompilerException(String message) {
        super(message);
    }
}
