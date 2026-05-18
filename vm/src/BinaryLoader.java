import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Loads binary files and creates a list of instructions and constants
 *
 * @author Zubair Abdul Matin
 */
public class BinaryLoader {

    // reverse OpCode table
    private HashMap<Integer, OpCode> reverseOpCodeTable = new HashMap<>();

    /**
     * Constructor for Binary loader
     */
    public BinaryLoader() {
        setOpCodeTable();
    }

    /**
     * Function to set up the reverse opcode table
     */
    private void setOpCodeTable() {
        for (OpCode op : OpCode.values()) {
            reverseOpCodeTable.put(op.getValue(), op);
        }
    }

    /**
     * Load program into a record containing an array of instructions and constants
     * @param filePath file path
     * @return record contaning two lists of instructions and constants
     */
    public LoadedProgram loadProgram(String filePath) {
        try (DataInputStream input = new DataInputStream(new FileInputStream(filePath))) {
            short magicNumber = input.readShort();
            if (Short.toUnsignedInt(magicNumber) != 0xCAFE) {
                throw new VirtualMachineException("[VM ERROR] Invalid magic number: " + magicNumber);

            }
            Byte version = input.readByte();
            Integer entryPoint = input.readInt();
            Integer instructionCount = input.readInt();
            Integer constantPoolCount = input.readInt();

            ArrayList<String> constantPool = new ArrayList<>();

            for (int i = 1; i <= constantPoolCount; i++) {
                int amount =  input.readUnsignedByte();
                StringBuilder constantName = new StringBuilder();
                for (int j = 1; j <= amount; j++) {
                    constantName.append((char) input.readByte());
                }
                String constant = constantName.toString();
                if (!constant.isEmpty()) {
                    constantPool.add(constant);
                }
            }

            ArrayList<Instruction> instructions = new ArrayList<>();

            for  (int i = 1; i <= instructionCount; i++) {
                int opcodeHex =  input.readUnsignedByte();
                byte padding1 = input.readByte();
                byte padding2 = input.readByte();
                byte padding3 = input.readByte();
                int operand = input.readInt();
                int lineNumber = input.readInt();

                OpCode opCode = reverseOpCodeTable.get(opcodeHex);
                Instruction instruction = new Instruction(opCode, operand, lineNumber);
                instructions.add(instruction);
            }

            return new LoadedProgram(instructions, constantPool);

        } catch (IOException e) {
            throw new VirtualMachineException("[VM ERROR]: " + e.getMessage());
        }
    }
}
