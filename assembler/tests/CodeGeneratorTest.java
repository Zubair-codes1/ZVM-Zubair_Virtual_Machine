import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CodeGeneratorTest {

    public CodeGeneratorTest() {}

    @Test
    void testPushEncodedCorrectly() {
        List<String> lines = new ArrayList<>();
        lines.add("PUSH 100");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);

        List<EncodedInstruction> encodedInstructions = CodeGenerator.getInstance().generateInstructions(parsedLines, symbolTable);
        assertEquals(0x10, encodedInstructions.getFirst().opcode());
        assertEquals(100, encodedInstructions.getFirst().operand());

    }

    @Test
    void testHaltEncodedCorrectly() {
        List<String> lines = new ArrayList<>();
        lines.add("HALT");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);

        List<EncodedInstruction> encodedInstructions = CodeGenerator.getInstance().generateInstructions(parsedLines, symbolTable);
        assertEquals(0x00, encodedInstructions.getFirst().opcode());
        assertEquals(0, encodedInstructions.getFirst().operand());
    }

    @Test
    void testIdentifierResolvedToConstantPoolIndex() {
        List<String> lines = new ArrayList<>();
        lines.add("STORE secret");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);

        List<EncodedInstruction> encodedInstructions = CodeGenerator.getInstance().generateInstructions(parsedLines, symbolTable);
        assertEquals(0x51, encodedInstructions.getFirst().opcode());
        assertEquals(0, encodedInstructions.getFirst().operand());
    }

    @Test
    void testLabelReferenceResolvedToInstructionIndex() {
        List<String> lines = new ArrayList<>();
        lines.add("PUSH 10");
        lines.add(":myLabel");
        lines.add("CALL :myLabel");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);

        List<EncodedInstruction> encodedInstructions = CodeGenerator.getInstance().generateInstructions(parsedLines, symbolTable);
        assertEquals(0x43, encodedInstructions.get(1).opcode());
        assertEquals(1, encodedInstructions.get(1).operand());
    }

    @Test
    void testLabelDefinitionsNotEncoded() {
        List<String> lines = new ArrayList<>();
        lines.add("PUSH 10");
        lines.add(":myLabel");
        lines.add("CALL :myLabel");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);

        List<EncodedInstruction> encodedInstructions = CodeGenerator.getInstance().generateInstructions(parsedLines, symbolTable);
        assertEquals(2, encodedInstructions.size());
    }

    @Test
    void testLocalIntegerOperandEncodedCorrectly() {
        List<String> lines = new ArrayList<>();
        lines.add("STORE_LOCAL 3");

        List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.build(parsedLines);
        List<EncodedInstruction> encodedInstructions = CodeGenerator.getInstance().generateInstructions(parsedLines, symbolTable);
        assertEquals(3, encodedInstructions.getFirst().operand());
    }
}
