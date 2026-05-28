import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Code generator class that maps each opcode to a hex value
 * Produces Encoded instructions with the opcode and operand as integers
 *
 * @author Zubair Abdul Matin
 */
public class CodeGenerator {
    // instance of code generator singleton
    private static final CodeGenerator INSTANCE = new CodeGenerator();
    // stores opcodes and their hex values
    private Map<String, Integer> opCodeMapper;

    /**
     * Private singleton constructor
     */
    private CodeGenerator() {
        this.opCodeMapper =  new HashMap<>();
        opCodeMapPopulate();
    }

    // populating the opCodeMapper
    private void opCodeMapPopulate() {
        // System and Control
        opCodeMapper.put("HALT", 0x00);
        opCodeMapper.put("NOOP", 0x01);
        opCodeMapper.put("DUMP_STACK", 0x02);

        // Stack Manipulation
        opCodeMapper.put("PUSH", 0x10);
        opCodeMapper.put("POP", 0x11);
        opCodeMapper.put("DUP", 0x12);
        opCodeMapper.put("SWAP", 0x13);
        opCodeMapper.put("OVER", 0x14);
        opCodeMapper.put("PUSH_STR", 0x15);

        // Arithmetic Operations
        opCodeMapper.put("ADD", 0x20);
        opCodeMapper.put("SUB", 0x21);
        opCodeMapper.put("MULT", 0x22);
        opCodeMapper.put("DIV", 0x23);
        opCodeMapper.put("MOD", 0x24);
        opCodeMapper.put("LSHIFT", 0x25);
        opCodeMapper.put("RSHIFT", 0x26);
        opCodeMapper.put("INC_LOCAL", 0x27);
        opCodeMapper.put("DEC_LOCAL", 0x28);

        // Logic and Comparison
        opCodeMapper.put("EQ", 0x30);
        opCodeMapper.put("NEQ", 0x31);
        opCodeMapper.put("GT", 0x32);
        opCodeMapper.put("LT", 0x33);
        opCodeMapper.put("GTE", 0x34);
        opCodeMapper.put("LTE", 0x35);
        opCodeMapper.put("AND", 0x36);
        opCodeMapper.put("OR", 0x37);
        opCodeMapper.put("XOR", 0x38);
        opCodeMapper.put("NOT", 0x39);

        // Branching and Subroutines
        opCodeMapper.put("JUMP", 0x40);
        opCodeMapper.put("JIT", 0x41);
        opCodeMapper.put("JIF", 0x42);
        opCodeMapper.put("CALL", 0x43);
        opCodeMapper.put("RET", 0x44);

        // Memory
        opCodeMapper.put("LOAD", 0x50);
        opCodeMapper.put("STORE", 0x51);
        opCodeMapper.put("LOAD_LOCAL", 0x52);
        opCodeMapper.put("STORE_LOCAL", 0x53);
        opCodeMapper.put("ALLOC", 0x54);
        opCodeMapper.put("LOAD_HEAP", 0x55);
        opCodeMapper.put("STORE_HEAP", 0x56);

        // I/O
        opCodeMapper.put("PRINT", 0x60);
        opCodeMapper.put("PRINT_CHAR", 0x61);
        opCodeMapper.put("INPUT", 0x62);
        opCodeMapper.put("PRINT_STR",  0x63);
    }

    /**
     * Getting the instance of the code generator
     * @return singleton instance
     */
    public static CodeGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * generates encoded instructions from the parsed lines
     * @param parsedLines parsed lines
     * @param symbolTable symbol table
     * @return encoded instructions
     */
    public List<EncodedInstruction> generateInstructions(List<ParsedLine> parsedLines, SymbolTable symbolTable) {
        List<EncodedInstruction> instructions = new ArrayList<>();
        for (ParsedLine parsedLine:  parsedLines) {
            if (parsedLine == null) {
                continue;
            }else {
                EncodedInstruction encodedInstruction = encodedLine(parsedLine, symbolTable);
                if (encodedInstruction != null) {
                    instructions.add(encodedInstruction);
                }
            }
        }

        return instructions;
    }

    /**
     * Helper method for each line
     * @param parsedLine one parsed line
     * @param symbolTable symbol table
     * @return one encoded instruction
     */
    private EncodedInstruction encodedLine(ParsedLine parsedLine, SymbolTable symbolTable) {
        if (parsedLine.isLabelDefinition()) {
            return null;
        }

        int opcode;
        if (opCodeMapper.containsKey(parsedLine.opcode())) {
            opcode = opCodeMapper.get(parsedLine.opcode());
        }else {
            throw new AssemblerException("Unknown opcode " + parsedLine.opcode());
        }

        int operand = 0;

        if (parsedLine.operandType() != null) {
            if (parsedLine.operandType().equals(TokenType.INTEGER)) {
                operand = Integer.parseInt(parsedLine.operandValue());
            } else if (parsedLine.operandType().equals(TokenType.IDENTIFIER) || parsedLine.operandType().equals(TokenType.STRING)) {
                operand = symbolTable.resolveConstantPool(parsedLine.operandValue());
            }else if (parsedLine.operandType().equals(TokenType.LABEL_REFERENCE)) {
                operand = symbolTable.resolveLabel(parsedLine.operandValue());
            }
        }

        return new EncodedInstruction(opcode, operand, parsedLine.lineNumber());
    }

}
