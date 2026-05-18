/**
 * Token record class
 * @param type the type of token - defined by TokenType Enum
 * @param tokenValue the actual value of the token e.g. PUSH, POP
 * @param lineNumber the line number at which it appears
 *
 * @author Zubair Abdul Matin
 */
public record Token(TokenType type, String tokenValue, int lineNumber) {
}
