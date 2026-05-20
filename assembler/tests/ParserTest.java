import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    public ParserTest() {}

    @Test
    void testParsesOpcodeWithNoOperand() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("HALT");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        assertFalse(parsedLines.getFirst().isLabelDefinition());
        assertNull(parsedLines.getFirst().operandType());
        assertNull(parsedLines.getFirst().operandValue());
        assertEquals("HALT", parsedLines.getFirst().opcode());

    }

    @Test
    void testParsesOpcodeWithIntegerOperand() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("PUSH 100");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        assertFalse(parsedLines.getFirst().isLabelDefinition());
        assertNotNull(parsedLines.getFirst().operandType());
        assertEquals(TokenType.INTEGER, parsedLines.getFirst().operandType());
        assertEquals("100", parsedLines.getFirst().operandValue());
        assertEquals("PUSH", parsedLines.getFirst().opcode());
    }

    @Test
    void testParsesOpcodeWithIdentifierOperand() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("STORE secret");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        assertFalse(parsedLines.getFirst().isLabelDefinition());
        assertNotNull(parsedLines.getFirst().operandType());
        assertEquals(TokenType.IDENTIFIER, parsedLines.getFirst().operandType());
        assertEquals("secret", parsedLines.getFirst().operandValue());
        assertEquals("STORE", parsedLines.getFirst().opcode());
    }

    @Test
    void testParsesOpcodeWithLabelReference() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("CALL :myFunc");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        assertFalse(parsedLines.getFirst().isLabelDefinition());
        assertNotNull(parsedLines.getFirst().operandType());
        assertEquals(TokenType.LABEL_REFERENCE, parsedLines.getFirst().operandType());
        assertEquals(":myFunc", parsedLines.getFirst().operandValue());
        assertEquals("CALL", parsedLines.getFirst().opcode());
    }

    @Test
    void testParsesLabelDefinition() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(":myLabel");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        assertTrue(parsedLines.getFirst().isLabelDefinition());
        assertNull(parsedLines.getFirst().operandValue());
        assertNull(parsedLines.getFirst().operandType());
        assertEquals(":myLabel", parsedLines.getFirst().opcode());
    }

    @Test
    void testBlankLinesAreSkipped() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("    ");
        lines.add("         ");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        assertTrue(parsedLines.isEmpty());
    }

    @Test
    void testPushWithNoOperandThrows() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("PUSH");
        assertThrows(AssemblerException.class, () -> {
            Parser.getInstance().parse(lines);
        });
    }

    @Test
    void testCallWithNoLabelThrows() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("CALL");
        assertThrows(AssemblerException.class, () -> {
            Parser.getInstance().parse(lines);
        });
    }

    @Test
    void testLoadWithNoIdentifierThrows() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("LOAD");
        assertThrows(AssemblerException.class, () -> {
            Parser.getInstance().parse(lines);
        });
    }

    @Test
    void testLocalInstructionWithNoIntegerThrows() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("INC_LOCAL");
        assertThrows(AssemblerException.class, () -> {
            Parser.getInstance().parse(lines);
        });
    }
}
