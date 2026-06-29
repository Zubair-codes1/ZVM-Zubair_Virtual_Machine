/**
 * Holds information about each token such as token type,
 * the token value (actual string value) and its line number.
 * @param type type of token
 * @param tokenValue actual value held in token
 * @param lineNumber token line number
 *
 * @author Zubair Abdul Matin
 */

public record Token(TokenType type, String tokenValue, int lineNumber) {
}