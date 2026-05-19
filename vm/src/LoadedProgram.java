import java.util.ArrayList;

/**
 * Stores the instructions and constant pool in a record
 * @param instructions all instructions
 * @param constantPool all constants
 * @author Zubair Abdul Matin
 */
public record LoadedProgram(ArrayList<Instruction> instructions, ArrayList<String> constantPool) {
}
