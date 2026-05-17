import java.util.Stack;

public class BranchingHandler implements InstructionHandler {
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

    private int getTrueOrFalse(Instruction instruction, VirtualMachine virtualMachine) {
        if (instruction.operand() >= virtualMachine.getProgramStorage().size()) {
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

    private void handleCall(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (instruction.operand() >= virtualMachine.getProgramStorage().size()) {
            throw new VirtualMachineException("Error: Jumping into the void");
        }

        Frame frame = new Frame(virtualMachine.getProgramCounter() + 1);

        virtualMachine.getCallStack().push(frame);
        virtualMachine.setProgramCounter(instruction.operand() - 1);
    }

    private void handleRet(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getCallStack().isEmpty()) {
            throw new VirtualMachineException("Error: Returning nothing");
        }

        Frame currentFrame = virtualMachine.getCallStack().pop();
        virtualMachine.setProgramCounter(currentFrame.getReturnAddress() - 1);
    }
}
