import java.util.*;

/**
 * Lexer breaks down the program into a list of tokens.
 *
 * @author Zubair Abdul Matin
 */
public class Lexer {

    // singleton
    private static final Lexer INSTANCE = new Lexer();

    // private constructor for singleton
    private Lexer() {}

    /**
     * Gets lexer singlton
     * @return Lexer singlton
     */
    public static Lexer getInstance() {
        return INSTANCE;
    }

    /**
     * Cleans the inputted line and then converts
     * it into a list of tokens
     *
     * @param input line inputted
     * @param lineNumber current line number
     * @return list of tokens
     */
    public List<Token> tokenize(String input, int lineNumber) {
        // removing comments
        int commentStartIndex = input.indexOf("//");
        if (commentStartIndex != -1) {
            input = input.substring(0, commentStartIndex);
        }

        // cleaning input and checking for empty line
        String cleanedInput = input.strip();

        if (cleanedInput.isEmpty()) {
            return new ArrayList<>();
        }

        // getting the tokens
        List<Token> tokens = new ArrayList<>();
        int currentPointer = 0;

        // running loop to get tokens
        while (currentPointer < cleanedInput.length()) {
            currentPointer = scanToken(tokens, cleanedInput, lineNumber, currentPointer);
        }

        return tokens;
    }

    /**
     * Scans input and makes tokens
     *
     * @param tokens list of tokens
     * @param input the cleaned input that is being lexed
     * @param lineNumber the current line number of the input line
     * @param currentPointer current pointer
     * @return current pointer
     */
    private int scanToken(List<Token> tokens, String input, int lineNumber, int currentPointer) {
        char c = input.charAt(currentPointer);
        currentPointer += 1; // next character

        switch (c) {
            case '(': tokens.add(new Token(TokenType.LEFT_PAREN, "(", lineNumber)); break;
            case ')': tokens.add(new Token(TokenType.RIGHT_PAREN, ")", lineNumber)); break;
            case '{': tokens.add(new Token(TokenType.LEFT_BRACE, "{", lineNumber)); break;
            case '}': tokens.add(new Token(TokenType.RIGHT_BRACE, "}", lineNumber)); break;
            case ',': tokens.add(new Token(TokenType.COMMA, ",", lineNumber)); break;
            case ';': tokens.add(new Token(TokenType.SEMICOLON, ";", lineNumber)); break;

            // Skip any spaces that were left behind
            case ' ': break;
            case '\r': break;
            case '\t': break;

            // comparison operators
            case '=':
                if (match(input, '=', currentPointer)) {
                    tokens.add(new Token(TokenType.EQUAL_EQUAL, "==", lineNumber));
                    currentPointer += 1;
                }else {
                    tokens.add(new Token(TokenType.ASSIGNMENT, "=", lineNumber));
                }
                break;

            case '>':
                if (match(input, '=', currentPointer)) {
                    tokens.add(new Token(TokenType.GREATER_OR_EQUAL, ">=", lineNumber));
                    currentPointer += 1;
                }else {
                    tokens.add(new Token(TokenType.GREATER_THAN, ">", lineNumber));
                }
                break;

            case '<':
                if (match(input, '=', currentPointer)) {
                    tokens.add(new Token(TokenType.LESS_OR_EQUAL, "<=", lineNumber));
                    currentPointer += 1;
                }else {
                    tokens.add(new Token(TokenType.LESS_THAN, "<", lineNumber));
                }
                break;

            // math operations
            case '+': tokens.add(new Token(TokenType.PLUS, "+", lineNumber)); break;
            case '-': tokens.add(new Token(TokenType.MINUS, "-", lineNumber)); break;
            case '*': tokens.add(new Token(TokenType.MULTIPLY, "*", lineNumber)); break;
            case '/': tokens.add(new Token(TokenType.DIVIDE, "/", lineNumber)); break;
            case '%': tokens.add(new Token(TokenType.MODULO, "%", lineNumber)); break;

            case '&':
                if (match(input, '&', currentPointer)) {
                    tokens.add(new Token(TokenType.LOGICAL_AND, "&&", lineNumber));
                    currentPointer += 1;
                }else {
                    tokens.add(new Token(TokenType.BITWISE_AND, "&", lineNumber));
                }
                break;

            case '|':
                if (match(input, '|', currentPointer)) {
                    tokens.add(new Token(TokenType.LOGICAL_OR, "||", lineNumber));
                    currentPointer += 1;
                }else {
                    tokens.add(new Token(TokenType.BITWISE_OR, "|", lineNumber));
                }
                break;

            case '!':
                if (match(input, '=', currentPointer)) {
                    tokens.add(new Token(TokenType.NOT_EQUAL, "!=", lineNumber));
                    currentPointer += 1;
                }else {
                    tokens.add(new Token(TokenType.LOGICAL_NOT, "!", lineNumber));
                }
                break;

            case '~': tokens.add(new Token(TokenType.BITWISE_NOT, "~", lineNumber)); break;

            // check for other opcodes
            default:

                break;
        }

        return currentPointer;

    }

    /**
     * Checks if the next value in the input matches an expected value.
     * Returns true if they do match and false otherwise.
     * @param input line input
     * @param expected expected character
     * @param currentPointer current pointer
     * @return true if they match, false otherwise
     */
    private boolean match(String input, char expected, int currentPointer) {
        return input.charAt(currentPointer) == expected;
    }
}
