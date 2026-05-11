import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        VirtualMachine virtualMachine = new VirtualMachine();
        ArrayList<String> instructions = new ArrayList<>();
        instructions.add("PUSH 5");
        instructions.add("JUMP HELLo");
        instructions.add("PUSH 10");
        instructions.add("PUSH 11");
        instructions.add(":HElLO");
        instructions.add("PUSH 9");
        instructions.add("PUSH 10");
        instructions.add("Halt");
        virtualMachine.loadProgramIntoStorage(instructions);
        virtualMachine.executeProgram();
        virtualMachine.returnStackData();
    }
}
