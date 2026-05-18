/**
 * Class to handle all exceptions received from the assembler.
 * Inherits from RuntimeException
 *
 * @author Zubair Abdul Matin
 */
public class AssemblerException extends RuntimeException {
    /**
     * Constructor to create an exception
     * @param message message from the exception
     */
    public AssemblerException(String message) {
        super(message);
    }
}
