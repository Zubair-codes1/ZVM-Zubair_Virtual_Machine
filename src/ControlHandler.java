import java.util.Stack;

public class ControlHandler implements InstructionHandler {
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        switch (opcode) {
            case HALT -> virtualMachine.setIsRunning(false);
            case NOOP -> { }
            case DUMP_STACK -> handleDumpStack(virtualMachine);
            default -> throw new VirtualMachineException("ERROR: Unknown Opcode " + opcode);

        }
    }

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
