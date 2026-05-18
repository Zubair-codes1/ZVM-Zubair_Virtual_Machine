import java.util.Stack;

/**
 * Handles branching
 *
 * @author Zubair Abdul Maitn
 */
public class BranchingHandler implements InstructionHandler {

    /**
     * Executes the functions
     * @param instruction instruction
     * @param opcode opcode
     * @param virtualMachine virtual machine state
     * @throws VirtualMachineException exception
     */
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {

        Stack<Integer> programStack = virtualMachine.getStack();

        switch (opcode) {
            case JUMP -> virtualMachine.setProgramCounter(instruction.operand() - 1);
            case JIT -> handleBooleanJump(instruction, OpCode.JIT, virtualMachine);
            case JIF -> handleBooleanJump(instruction, OpCode.JIF, virtualMachine);
            case CALL -> handleCall(instruction, virtualMachine);
            case RET -> handleRet(virtualMachine);
        }
    }

    /**
     * Hanles boolean jump types
     * @param instruction instruction
     * @param type opcode
     * @param virtualMachine virtual machine state
     * @throws VirtualMachineException exception
     */
    private void handleBooleanJump(Instruction instruction, OpCode type, VirtualMachine virtualMachine) throws VirtualMachineException {
        int trueOrFalse = getTrueOrFalse(instruction, virtualMachine);

        if (type.equals(OpCode.JIT)) {
            if (trueOrFalse == 1) {
                virtualMachine.setProgramCounter(instruction.operand() - 1);
            }
        }else if (type.equals(OpCode.JIF)) {
            if (trueOrFalse == 0) {
                virtualMachine.setProgramCounter(instruction.operand() - 1);
            }
        }
    }

    /**
     * Helper function for handleBooleanJump
     * @param instruction instruction
     * @param virtualMachine Virtual machine state
     * @return true or false
     */
    private int getTrueOrFalse(Instruction instruction, VirtualMachine virtualMachine) {
        if (instruction.operand() >= virtualMachine.getExecutableInstructions().size()) {
            throw new VirtualMachineException("Error: Jumping into the void");
        }

        Stack<Integer> programStack = virtualMachine.getStack();
        if (programStack.isEmpty()) {
            throw new VirtualMachineException("Error: Jumping into nothing");
        }

        int trueOrFalse = programStack.pop();
        if (trueOrFalse != 0 && trueOrFalse != 1) {
            throw new VirtualMachineException("Error: Invalid boolean");
        }
        return trueOrFalse;
    }

    /**
     * CALL instruction
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleCall(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (instruction.operand() >= virtualMachine.getExecutableInstructions().size()) {
            throw new VirtualMachineException("Error: Jumping into the void");
        }

        Frame frame = new Frame(virtualMachine.getProgramCounter() + 1);

        virtualMachine.getCallStack().push(frame);
        virtualMachine.setProgramCounter(instruction.operand() - 1);
    }

    /**
     * RET instruction
     * @param virtualMachine virtual machine state
     * @throws VirtualMachineException exception
     */
    private void handleRet(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getCallStack().isEmpty()) {
            throw new VirtualMachineException("Error: Returning nothing");
        }

        Frame currentFrame = virtualMachine.getCallStack().pop();
        virtualMachine.setProgramCounter(currentFrame.getReturnAddress() - 1);
    }
}
