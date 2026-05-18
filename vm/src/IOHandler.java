import java.util.Scanner;

public class IOHandler implements InstructionHandler {
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        switch (opcode) {
            case PRINT -> handlePrint(virtualMachine, OpCode.PRINT);
            case PRINT_CHAR -> handlePrint(virtualMachine, OpCode.PRINT_CHAR);
            case INPUT -> handleInput(virtualMachine);
        }
    }

    private void handlePrint(VirtualMachine virtualMachine, OpCode type) throws VirtualMachineException {
        if (virtualMachine.getStack().isEmpty()) {
            throw new VirtualMachineException("Error: No data to print!");
        }

        int value = virtualMachine.getStack().pop();

        if (type == OpCode.PRINT_CHAR) {
            if (value >=0  && value <= 255) {
                char character = (char) value;
                System.out.println("VM OUTPUT: " + character);
            }

        }else if (type == OpCode.PRINT) {
            System.out.println("VM OUTPUT: " + value);
        }
    }

    private void handleInput(VirtualMachine virtualMachine) throws VirtualMachineException {
        Scanner sc = new Scanner(System.in);
        System.out.print("> INPUT REQUIRED (Integer): ");

        // Check if the next token is actually a number
        while (!sc.hasNextInt()) {
            System.out.println("Error: That's not a valid integer. Try again.");
            System.out.print("> ");
            sc.next(); // Clear the "bad" input from the buffer
        }
        int value = sc.nextInt();
        virtualMachine.getStack().push(value);
    }
}
