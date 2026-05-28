import java.util.*;

/**
 * Parser class parses all the lines in the file and returns a
 * ParsedLine record for each one. Singleton class
 *
 * @author Zubair Abdul Matin
 */
public class Parser {
    // parser singleton
    private static final Parser parser = new Parser();
    // list of errors
    private List<String> errors = new ArrayList<>();
    // lexer instance (singleton)
    private Lexer lexer = Lexer.getInstance();

    // private parser constructor
    private Parser () {}

    /**
     * Gets Parser singleton
     * @return parser singleton
     */
    public static Parser getInstance() {
        return parser;
    }

    /**
     * Parses all the lines
     * @param lines list of lines
     * @return list of parsed lines
     */
    public List<ParsedLine> parse(List<String> lines) {
        errors.clear();

        List<ParsedLine> parsedLines = new ArrayList<>();
        int lineNumber = 1;
        for (String line : lines) {
            List<Token> tokens = lexer.tokenize(line, lineNumber);
            ParsedLine parsedLine = parseLine(tokens, lineNumber);
            if (parsedLine != null) { parsedLines.add(parsedLine); }
            lineNumber++;
        }

        if (!errors.isEmpty()) {
            for (String error : errors) {
                System.err.println("Error: " + error);
            }
            throw new AssemblerException("Found errors");
        }

        return parsedLines;
    }

    /**
     * Parses one line
     * @param tokens list of tokens
     * @param lineNumber line number for one line
     * @return a single parsed line
     */
    private ParsedLine parseLine(List<Token> tokens, int lineNumber) {
        if (tokens.isEmpty()) {
            return null;
        }

        Token firstToken = tokens.getFirst();
        Token secondToken = null;
        if (tokens.size() > 1) {
            secondToken = tokens.get(1);
        }

        TokenType operandType = secondToken != null ? secondToken.type() : null;
        String value = secondToken != null ? secondToken.tokenValue() : null;

        if (firstToken.type().equals(TokenType.LABEL_DEFINITION)) {
            return new ParsedLine(
                    firstToken.tokenValue(), null,
                    null, true,
                    firstToken.tokenValue(), lineNumber
            );
        }

        if (firstToken.type().equals(TokenType.OPCODE)) {
            checkErrors(firstToken, secondToken, lineNumber);
            return new ParsedLine(
                    firstToken.tokenValue(), operandType,
                    value, false,
                    null, lineNumber
            );
        }

        String errorMessage = "Line " + lineNumber + ": Expected opcode or label, got " + firstToken.type();
        errors.add(errorMessage);
        return null;
    }

    /**
     * Checks for an opcode error
     * @param opcode opcode token
     * @param operand opcode operand
     */
    private void checkErrors(Token opcode, Token operand, int lineNumber) {
        if (
                (opcode.tokenValue().equals("PUSH") || opcode.tokenValue().equals("PUSH_STR")) &&
                (operand == null || (!operand.type().equals(TokenType.INTEGER) && !operand.type().equals(TokenType.STRING)))
        ) {
            errors.add("PUSH/PUSH_STR requires a valid integer or string operand at line " + lineNumber);
        }

        if (
                (opcode.tokenValue().equals("JUMP") || opcode.tokenValue().equals("JIF") ||
                opcode.tokenValue().equals("JIT") || opcode.tokenValue().equals("CALL")) &&
                (operand == null || !operand.type().equals(TokenType.LABEL_REFERENCE))
        ) {
            errors.add("Branching requires a label reference at line " + lineNumber);
        }

        if (
                (opcode.tokenValue().equals("LOAD") || opcode.tokenValue().equals("STORE")) &&
                (operand == null || !operand.type().equals(TokenType.IDENTIFIER))
        ) {
            errors.add("Loading and Storing require identifiers at line " + lineNumber);
        }

        if (
                (opcode.tokenValue().equals("LOAD_LOCAL") || opcode.tokenValue().equals("STORE_LOCAL")
                 || opcode.tokenValue().equals("INC_LOCAL") || opcode.tokenValue().equals("DEC_LOCAL")
                 || opcode.tokenValue().equals("ALLOC") || opcode.tokenValue().equals("LOAD_HEAP") ||
                 opcode.tokenValue().equals("STORE_HEAP"))
                && (operand == null || !operand.type().equals(TokenType.INTEGER))
        ) {
            errors.add("Local and heap instructions require integer operand at line " + lineNumber);
        }
    }
}
