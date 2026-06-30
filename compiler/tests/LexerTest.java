import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Handles all the tests for the Lexer.
 *
 * @author Zubair Abdul Matin
 */
public class LexerTest {

    // tokens helper method
    private List<Token> getTokens(String input) {
        return Lexer.getInstance().tokenize(input);
    }

    // ==========================================
    // 1. DELIMITERS & BASIC PUNCTUATION
    // ==========================================

    @Test
    public void testParentheses() {
        List<Token> tokens = getTokens("()");
        assertEquals(3, tokens.size()); // (, ), EOF
        assertEquals(TokenType.LEFT_PAREN, tokens.get(0).type());
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(1).type());
    }

    @Test
    public void testBraces() {
        List<Token> tokens = getTokens("{}");
        assertEquals(3, tokens.size()); // {, }, EOF
        assertEquals(TokenType.LEFT_BRACE, tokens.get(0).type());
        assertEquals(TokenType.RIGHT_BRACE, tokens.get(1).type());
    }

    @Test
    public void testPunctuation() {
        List<Token> tokens = getTokens(",;");
        assertEquals(3, tokens.size()); // ,, ;, EOF
        assertEquals(TokenType.COMMA, tokens.get(0).type());
        assertEquals(TokenType.SEMICOLON, tokens.get(1).type());
    }

    // ==========================================
    // 2. MATH OPERATORS
    // ==========================================

    @Test
    public void testBasicArithmeticOperators() {
        List<Token> tokens = getTokens("+ - * / %");
        assertEquals(6, tokens.size()); // 5 ops + EOF
        assertEquals(TokenType.PLUS, tokens.get(0).type());
        assertEquals(TokenType.MINUS, tokens.get(1).type());
        assertEquals(TokenType.MULTIPLY, tokens.get(2).type());
        assertEquals(TokenType.DIVIDE, tokens.get(3).type());
        assertEquals(TokenType.MODULO, tokens.get(4).type());
    }

    // ==========================================
    // 3. LOGICAL & BITWISE OPERATORS
    // ==========================================

    @Test
    public void testAndOperators() {
        List<Token> tokens = getTokens("& &&");
        assertEquals(3, tokens.size()); // &, &&, EOF
        assertEquals(TokenType.BITWISE_AND, tokens.get(0).type());
        assertEquals(TokenType.LOGICAL_AND, tokens.get(1).type());
    }

    @Test
    public void testOrOperators() {
        List<Token> tokens = getTokens("| ||");
        assertEquals(3, tokens.size()); // |, ||, EOF
        assertEquals(TokenType.BITWISE_OR, tokens.get(0).type());
        assertEquals(TokenType.LOGICAL_OR, tokens.get(1).type());
    }

    @Test
    public void testNotOperators() {
        List<Token> tokens = getTokens("! ~");
        assertEquals(3, tokens.size()); // !, ~, EOF
        assertEquals(TokenType.LOGICAL_NOT, tokens.getFirst().type());
        assertEquals(TokenType.BITWISE_NOT, tokens.get(1).type());
    }

    @Test
    public void testBitwiseXor() {
        List<Token> tokens = getTokens("^");
        assertEquals(2, tokens.size()); // ^, EOF
        assertEquals(TokenType.BITWISE_XOR, tokens.getFirst().type());
    }

    // ==========================================
    // 4. COMPARISON OPERATORS
    // ==========================================

    @Test
    public void testEqualityOperators() {
        List<Token> tokens = getTokens("= == !=");
        assertEquals(4, tokens.size()); // =, ==, !=, EOF
        assertEquals(TokenType.ASSIGNMENT, tokens.getFirst().type());
        assertEquals(TokenType.EQUAL_EQUAL, tokens.get(1).type());
        assertEquals(TokenType.NOT_EQUAL, tokens.get(2).type());
    }

    @Test
    public void testRelationalOperators() {
        List<Token> tokens = getTokens("< <= > >=");
        assertEquals(5, tokens.size()); // <, <=, >, >=, EOF
        assertEquals(TokenType.LESS_THAN, tokens.getFirst().type());
        assertEquals(TokenType.LESS_OR_EQUAL, tokens.get(1).type());
        assertEquals(TokenType.GREATER_THAN, tokens.get(2).type());
        assertEquals(TokenType.GREATER_OR_EQUAL, tokens.get(3).type());
    }

    // ==========================================
    // 5. LITERALS & IDENTIFIERS
    // ==========================================

    @Test
    public void testNumbers() {
        List<Token> tokens = getTokens("123 45");
        assertEquals(3, tokens.size()); // 123, 45, EOF
        assertEquals(TokenType.INT, tokens.get(0).type());
        assertEquals("123", tokens.get(0).tokenValue());
        assertEquals(TokenType.INT, tokens.get(1).type());
        assertEquals("45", tokens.get(1).tokenValue());
    }

    @Test
    public void testIdentifiersAndKeywords() {
        List<Token> tokens = getTokens("while my_variable if");
        assertEquals(4, tokens.size()); // while, my_variable, if, EOF
        assertEquals(TokenType.WHILE, tokens.get(0).type());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type());
        assertEquals("my_variable", tokens.get(1).tokenValue());
        assertEquals(TokenType.IF, tokens.get(2).type());
    }

    @Test
    public void testStringLiterals() {
        List<Token> tokens = getTokens("\"hello world\"");
        assertEquals(2, tokens.size()); // string, EOF
        assertEquals(TokenType.STRING, tokens.getFirst().type());
        assertEquals("hello world", tokens.getFirst().tokenValue());
    }

    @Test
    public void testBooleanTrueLiteral() {
        List<Token> tokens = getTokens("true");
        assertEquals(2, tokens.size()); // TRUE, EOF
        assertEquals(TokenType.TRUE, tokens.getFirst().type());
    }

    @Test
    public void testBooleanFalseLiteral() {
        List<Token> tokens = getTokens("false");
        assertEquals(2, tokens.size()); // FALSE, EOF
        assertEquals(TokenType.FALSE, tokens.getFirst().type());
    }

    @Test
    public void testBooleansMixedWithIdentifiers() {
        // makes sure similiar identifiers don't get mixed up with true or false
        List<Token> tokens = getTokens("true trueValue false false_flag");
        assertEquals(5, tokens.size()); // TRUE, IDENTIFIER, FALSE, IDENTIFIER, EOF

        assertEquals(TokenType.TRUE, tokens.getFirst().type());

        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type());
        assertEquals("trueValue", tokens.get(1).tokenValue());

        assertEquals(TokenType.FALSE, tokens.get(2).type());

        assertEquals(TokenType.IDENTIFIER, tokens.get(3).type());
        assertEquals("false_flag", tokens.get(3).tokenValue());
    }

    // ==========================================
    // 6. WHITESPACE, COMMENTS & BOUNDARIES
    // ==========================================

    @Test
    public void testCommentsAreSkipped() {
        List<Token> tokens = getTokens("x = 5; // This is a comment");
        assertEquals(5, tokens.size()); // x, =, 5, ;, EOF
        assertEquals(TokenType.SEMICOLON, tokens.get(3).type());
    }

    @Test
    public void testNoSpacesBoundaryIsolation() {
        // Confirms text isn't clumped into symbols or strings weirdly
        List<Token> tokens = getTokens("if(x==5);");
        assertEquals(8, tokens.size()); // if, (, x, ==, 5, ), ;, EOF
        assertEquals(TokenType.IF, tokens.get(0).type());
        assertEquals(TokenType.LEFT_PAREN, tokens.get(1).type());
        assertEquals(TokenType.IDENTIFIER, tokens.get(2).type());
        assertEquals(TokenType.EQUAL_EQUAL, tokens.get(3).type());
    }

    @Test
    public void testLineNumberTracking() {
        String code = "x = 1;\n\nif y";
        List<Token> tokens = getTokens(code);

        // "x" is on line 1
        assertEquals(1, tokens.get(0).lineNumber());
        // "if" is on line 3 (due to double newline)
        assertEquals(TokenType.IF, tokens.get(4).type());
        assertEquals(3, tokens.get(4).lineNumber());
    }

    // ==========================================
    // 7. ERROR HANDLING
    // ==========================================

    @Test
    public void testInvalidCharacterError() {
        List<Token> tokens = getTokens("@");
        assertEquals(2, tokens.size()); // ERROR, EOF
        assertEquals(TokenType.ERROR, tokens.getFirst().type());
        assertEquals("@", tokens.getFirst().tokenValue());
    }
}
