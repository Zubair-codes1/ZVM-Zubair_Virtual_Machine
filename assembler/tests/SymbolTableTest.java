import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SymbolTableTest {

    public SymbolTableTest() {}

    @Test
    void testLabelResolvesCorrectly() {
        List<String> lines = new ArrayList<>();
        lines.add("PUSH 10");
        lines.add("PUSH 20");
        lines.add(":myLabel");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);

        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);

        assertEquals(2, symbolTable.resolveLabel(":myLabel"));
    }

    @Test
    void testUnknownLabelThrows() {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("   ");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);
        assertThrows(AssemblerException.class, () -> {
            symbolTable.resolveLabel(":nonexistent");
        });
    }

    @Test
    void testLabelIndexIsCorrect() {
        List<String> lines = new ArrayList<>();
        lines.add("PUSH 10");
        lines.add("PUSH 20");
        lines.add("PUSH 30");
        lines.add(":myLabel");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);
        assertEquals(3, symbolTable.resolveLabel(":myLabel"));
    }

    @Test
    void testConstantPoolBuildsCorrectly() {
        List<String> lines = new ArrayList<>();
        lines.add("STORE secret");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);
        assertTrue(symbolTable.getConstantPool().contains("secret"));
    }

    @Test
    void testConstantPoolIndexIsCorrect() {
        List<String> lines = new ArrayList<>();
        lines.add("STORE secret");
        lines.add("STORE hello");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);

        assertEquals("secret", symbolTable.getConstantPool().getFirst());
        assertEquals("hello", symbolTable.getConstantPool().get(1));
    }

    @Test
    void testUnknownConstantThrows() {
        List<String> lines = new ArrayList<>();
        lines.add("");
        lines.add("   ");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);
        assertThrows(AssemblerException.class, () -> {
            symbolTable.resolveLabel(":nonexistent");
        });
    }

    @Test
    void testDuplicateConstantsNotAdded() {
        List<String> lines = new ArrayList<>();
        lines.add("STORE secret");
        lines.add("STORE secret");
        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);
        assertEquals(1, symbolTable.getConstantPool().size());
    }
}
