import java.util.*;

/**
 * A Lexer singleton class that takes one line of
 * code and converts it into a list of tokens.
 *
 * @author Zubair Abddul Matin
 */
public class Lexer {

    // singleton
    private static final Lexer INSTANCE = new Lexer();

    // private constructor for singleton
    private Lexer() {}

    /**
     * Getting the singleton lexer
     * @return Lexer singleton
     */
    public static Lexer getInstance() {
        return INSTANCE;
    }

    /**
     * Takes a string input and its line number and produces tokens from it
     * @param input string input
     * @param lineNumber line number of input
     * @return list of tokens
     */
    public List<Token> tokenize(String input, int lineNumber) {
        int semicolonIndex = input.indexOf(';');
        if (semicolonIndex != -1) { input = input.substring(0, semicolonIndex); }

        int hashIndex = input.indexOf('#');
        if (hashIndex != -1) { input = input.substring(0, hashIndex); }
        String cleanedInput = input.strip();

        if (cleanedInput.isEmpty()) {
            return new ArrayList<>();
        }

        List<Token> tokens = new ArrayList<>();
        if(cleanedInput.charAt(0) == ':') {
            tokens.add(new Token(TokenType.LABEL_DEFINITION, cleanedInput, lineNumber));
            return tokens;
        }

        // Extract the opcode (first word up to the first whitespace)
        int firstSpace = cleanedInput.indexOf(' ');
        String opCode;
        String operandStr = null;

        if (firstSpace == -1) {
            opCode = cleanedInput;
        } else {
            opCode = cleanedInput.substring(0, firstSpace).strip();
            operandStr = cleanedInput.substring(firstSpace).strip(); // Everything else is the raw operand
        }

        tokens.add(new Token(TokenType.OPCODE, opCode, lineNumber));

        // If there is an operand substring left to process...
        if (operandStr != null && !operandStr.isEmpty()) {
            if (operandStr.startsWith(":")) {
                tokens.add(new Token(TokenType.LABEL_REFERENCE, operandStr, lineNumber));
            } else if (operandStr.startsWith("\"") && operandStr.endsWith("\"")) {
                // Strip the quotes and treat as a clean string literal
                String cleanString = operandStr.substring(1, operandStr.length() - 1);
                tokens.add(new Token(TokenType.STRING, cleanString, lineNumber));
            } else {
                try {
                    Integer.parseInt(operandStr);
                    tokens.add(new Token(TokenType.INTEGER, operandStr, lineNumber));
                } catch (NumberFormatException e) {
                    tokens.add(new Token(TokenType.IDENTIFIER, operandStr, lineNumber));
                }
            }
        }

        return tokens;

    }

}
