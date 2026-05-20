import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    public LexerTest() {}

    @Test
    void testPushInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("PUSH 100", 1);
        assertEquals(TokenType.OPCODE, tokens.get(0).type());
        assertEquals("PUSH", tokens.get(0).tokenValue());
        assertEquals(TokenType.INTEGER, tokens.get(1).type());
        assertEquals("100", tokens.get(1).tokenValue());
    }

    @Test
    void testPopInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("POP", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("POP", tokens.getFirst().tokenValue());
    }

    @Test
    void testNoopInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("NOOP", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("NOOP", tokens.getFirst().tokenValue());
    }

    @Test
    void testAddInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("ADD", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("ADD", tokens.getFirst().tokenValue());
    }

    @Test
    void testMultInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("MULT", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("MULT", tokens.getFirst().tokenValue());
    }

    @Test
    void testLShiftInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("LSHIFT", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("LSHIFT", tokens.getFirst().tokenValue());
    }

    @Test
    void testIncLocalInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("INC_LOCAL 0", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("INC_LOCAL", tokens.getFirst().tokenValue());
        assertEquals(TokenType.INTEGER, tokens.get(1).type());
        assertEquals("0", tokens.get(1).tokenValue());
    }

    @Test
    void testDecLocalInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("DEC_LOCAL 0", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("DEC_LOCAL", tokens.getFirst().tokenValue());
        assertEquals(TokenType.INTEGER, tokens.get(1).type());
        assertEquals("0", tokens.get(1).tokenValue());
    }

    @Test
    void testEQInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("EQ", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("EQ", tokens.getFirst().tokenValue());
    }

    @Test
    void testNEQInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("NEQ", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("NEQ", tokens.getFirst().tokenValue());
    }

    @Test
    void testGTInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("GT", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("GT", tokens.getFirst().tokenValue());
    }

    @Test
    void testAndInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("AND", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("AND", tokens.getFirst().tokenValue());
    }

    @Test
    void testOrInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("OR", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("OR", tokens.getFirst().tokenValue());
    }

    @Test
    void testJumpInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("JUMP :hello", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("JUMP", tokens.getFirst().tokenValue());
        assertEquals(TokenType.LABEL_REFERENCE, tokens.get(1).type());
        assertEquals(":hello", tokens.get(1).tokenValue());
    }

    @Test
    void testCallInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("CALL :function", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("CALL", tokens.getFirst().tokenValue());
        assertEquals(TokenType.LABEL_REFERENCE, tokens.get(1).type());
        assertEquals(":function", tokens.get(1).tokenValue());
    }

    @Test
    void testPrintCharInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("PRINT_CHAR", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals("PRINT_CHAR", tokens.getFirst().tokenValue());
    }

    @Test
    void testCommentStripping() {
        List<Token> tokens = Lexer.getInstance().tokenize("PUSH 100 ; comment", 1);
        assertEquals(2, tokens.size()); // comment should be stripped
    }

    @Test
    void testBlankLine() {
        List<Token> tokens = Lexer.getInstance().tokenize("   ", 1);
        assertTrue(tokens.isEmpty());
    }

    @Test
    void testLabelDefinition() {
        List<Token> tokens = Lexer.getInstance().tokenize(":myLabel", 1);
        assertEquals(TokenType.LABEL_DEFINITION, tokens.getFirst().type());
        assertEquals(":myLabel", tokens.getFirst().tokenValue());
    }

    @Test
    void testStoreInstruction() {
        List<Token> tokens = Lexer.getInstance().tokenize("STORE secret", 1);
        assertEquals(TokenType.OPCODE, tokens.getFirst().type());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).type());
        assertEquals("secret", tokens.get(1).tokenValue());
    }

    @Test
    void testHashCommentStripping() {
        List<Token> tokens = Lexer.getInstance().tokenize("PUSH 100 # comment", 1);
        assertEquals(2, tokens.size());
    }

    @Test
    void testLineNumberTracked() {
        List<Token> tokens = Lexer.getInstance().tokenize("PUSH 100", 5);
        assertEquals(5, tokens.getFirst().lineNumber());
    }

    @Test
    void testCommentOnlyLine() {
        List<Token> tokens = Lexer.getInstance().tokenize("; this is a comment", 1);
        assertTrue(tokens.isEmpty());
    }
}
