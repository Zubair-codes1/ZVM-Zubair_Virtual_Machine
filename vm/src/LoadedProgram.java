import java.util.ArrayList;

public record LoadedProgram(ArrayList<Instruction> instructions, ArrayList<String> constantPool) {
}
