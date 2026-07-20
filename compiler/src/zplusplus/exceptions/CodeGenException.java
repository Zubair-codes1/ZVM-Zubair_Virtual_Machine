package zplusplus.exceptions;

/**
 * Code generation exception
 *
 * @author Zubair Abdul Matin
 */
public class CodeGenException extends CompilerException {
    public CodeGenException(String message, int lineNumber) {
        super(message, lineNumber);
    }
}
