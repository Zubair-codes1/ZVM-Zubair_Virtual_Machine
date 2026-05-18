import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Writes to a binary file based on the bytecode structure
 *
 * @author Zubair Abdul Matin
 */
public class BinaryWriter {
    // singleton instance
    private static final BinaryWriter INSTANCE = new BinaryWriter();

    /**
     * binary writer singleton constructor
     */
    private BinaryWriter() {
    }

    /**
     * Getting the singleton instance
     * @return binary writer
     */
    public static BinaryWriter getInstance() {
        return INSTANCE;
    }

    /**
     * Writes the instructions in bytecode format into the specific binary file
     * @param instructions encoded instructions
     * @param constantPool constant pool (from symbol table)
     * @param outputPath the file path
     */
    public void write(List<EncodedInstruction> instructions, List<String> constantPool, String outputPath) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputPath))) {
            dos.writeShort(0xCAFE);
            dos.writeByte(1);
            dos.writeInt(0);
            dos.writeInt(instructions.size());
            dos.writeInt(constantPool.size());

            for (String constant : constantPool) {
                dos.writeByte(constant.length());
                for (Byte b: constant.getBytes()) {
                    dos.writeByte(b);
                }
            }

            for (EncodedInstruction instruction : instructions) {
                dos.writeByte(instruction.opcode());
                dos.writeByte(0);
                dos.writeByte(0);
                dos.writeByte(0);
                dos.writeInt(instruction.operand());
                dos.writeInt(instruction.lineNumber());
            }
        } catch (IOException e) {
            throw new AssemblerException("Failed to write binary file: " + e.getMessage());
        }
    }
}
