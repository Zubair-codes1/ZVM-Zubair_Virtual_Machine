import java.util.Stack;

/**
 * Class to handle the System and Control opcodes
 *
 * @author Zubair Abdul Matin
 */
public class ControlHandler implements InstructionHandler {

    /**
     * Executes system and control opcode functions
     * @param instruction instruction
     * @param opcode opcode
     * @param virtualMachine virutal machine state
     * @throws VirtualMachineException exception
     */
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        switch (opcode) {
            case HALT -> virtualMachine.setIsRunning(false);
            case NOOP -> { }
            case DUMP_STACK -> handleDumpStack(virtualMachine);
            default -> throw new VirtualMachineException("ERROR: Unknown Opcode " + opcode);

        }
    }

    /**
     * Dump stack functionality helper method
     * @param virtualMachine virutal machine
     * @throws VirtualMachineException exception
     */
    private void handleDumpStack(VirtualMachine virtualMachine) throws VirtualMachineException {
        System.out.println("#-------STACK DUMP START-------#");
        Stack<Integer> stack = virtualMachine.getStack();
        for (int i = stack.size() - 1; i >= 0; i--) {
            Integer value = stack.get(i);

            String label = (i == stack.size() - 1) ? "[TOP]" : "";
            System.out.println("Index " + i + ": " + label + value);
        }
        System.out.println("#-------STACK DUMP END-------#");
    }

}
