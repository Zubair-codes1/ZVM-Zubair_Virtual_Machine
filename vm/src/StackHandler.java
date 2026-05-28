import java.util.Stack;

/**
 * Class to handle stack operations
 *
 * @author Zubair Abdul Matin
 */
public class StackHandler implements InstructionHandler {

    /**
     * Executes stack operations
     * @param instruction instruction
     * @param opcode opcode
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        Stack<Integer> programStack = virtualMachine.getStack();
        switch (opcode) {
            case PUSH -> programStack.push(instruction.operand());
            case POP -> handlePop(programStack);
            case DUP -> handleDup(programStack);
            case SWAP -> handleSwap(programStack);
            case OVER -> programStack.push(programStack.get(programStack.size() - 2));
            case PUSH_STR -> handlePushStr();
        }
    }

    /**
     * Handles Pop
     * @param programStack program stack
     * @throws VirtualMachineException exception
     */
    private void handlePop(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.isEmpty()) { throw new VirtualMachineException("Error: No data to pop!"); }
        programStack.pop();
    }

    /**
     * Handles DUP
     * @param programStack program stack
     * @throws VirtualMachineException exception
     */
    private void handleDup(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.isEmpty()) { throw new VirtualMachineException("Error: No data to duplicate!"); }
        programStack.push(programStack.peek());
    }

    /**
     * Handles SWAP
     * @param programStack program stack
     * @throws VirtualMachineException exception
     */
    private void handleSwap(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.size() < 2) { throw new VirtualMachineException("Error: Nothing to swap!"); }
        int first = programStack.pop();
        int second = programStack.pop();
        programStack.push(first);
        programStack.push(second);
    }

    /**
     * Handles OVER
     * @param programStack prgram stack
     * @throws VirtualMachineException exception
     */
    private void handleOver(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.size() < 2) { throw new VirtualMachineException("Error: Nothing to over!"); }
        programStack.push(programStack.get(programStack.size() - 2));
    }

    /**
     * Handles pushing strings
     * @throws VirtualMachineException exception
     */
    private void handlePushStr() throws VirtualMachineException {

    }
}
