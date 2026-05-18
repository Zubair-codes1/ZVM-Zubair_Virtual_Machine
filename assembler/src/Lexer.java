import java.util.*;

public class Lexer {

    private static final Lexer INSTANCE = new Lexer();

    private Lexer() {}

    public static Lexer getInstance() {
        return INSTANCE;
    }

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

        String[] split = cleanedInput.split("\\s+");
        String opCode = split[0];
        tokens.add(new Token(TokenType.OPCODE, opCode, lineNumber));

        if (split.length > 1) {
            if (split[1].startsWith(":")) {
                tokens.add(new Token(TokenType.LABEL_REFERENCE, split[1], lineNumber));
            }else {
                try {
                    int num = Integer.parseInt(split[1]);
                    tokens.add(new Token(TokenType.INTEGER, split[1], lineNumber));
                }catch (NumberFormatException e) {
                    tokens.add(new Token(TokenType.IDENTIFIER, split[1], lineNumber));
                }
            }
        }

        return tokens;

    }

}
