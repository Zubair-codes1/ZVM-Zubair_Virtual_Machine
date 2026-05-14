import java.util.Stack;

public class StackHandler implements InstructionHandler {
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        Stack<Integer> programStack = virtualMachine.getStack();
        switch (opcode) {
            case PUSH -> programStack.push(instruction.operand());
            case POP -> handlePop(programStack);
            case DUP -> handleDup(programStack);
            case SWAP -> handleSwap(programStack);
            case OVER -> programStack.push(programStack.get(programStack.size() - 2));
        }
    }

    private void handlePop(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.isEmpty()) { throw new VirtualMachineException("Error: No data to pop!"); }
        programStack.pop();
    }

    private void handleDup(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.isEmpty()) { throw new VirtualMachineException("Error: No data to duplicate!"); }
        programStack.push(programStack.peek());
    }

    private void handleSwap(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.size() < 2) { throw new VirtualMachineException("Error: Nothing to swap!"); }
        int first = programStack.pop();
        int second = programStack.pop();
        programStack.push(first);
        programStack.push(second);
    }

    private void handleOver(Stack<Integer> programStack) throws VirtualMachineException {
        if (programStack.size() < 2) { throw new VirtualMachineException("Error: Nothing to over!"); }
        programStack.push(programStack.get(programStack.size() - 2));
    }
}
