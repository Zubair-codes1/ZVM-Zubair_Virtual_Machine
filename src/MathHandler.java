public class MathHandler implements InstructionHandler {
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: Math commands need two operands!");
        }

        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();

        switch (opcode) {
            case ADD -> virtualMachine.getStack().push(left + right);
            case SUB -> virtualMachine.getStack().push(left - right);
            case MULT -> virtualMachine.getStack().push(left * right);
            case DIV -> {
                if  (right == 0) {
                    throw new VirtualMachineException("Error: Division by zero!");
                }
                virtualMachine.getStack().push(left / right);
            }
            case MOD -> {
                if  (right == 0) {
                    throw new VirtualMachineException("Error: Division by zero!");
                }
                virtualMachine.getStack().push(left % right);
            }
            case LSHIFT -> virtualMachine.getStack().push(left << right);
            case RSHIFT -> virtualMachine.getStack().push(left >>> right);
            default -> throw new VirtualMachineException("Error: Unknown opcode: " + opcode);
        }
    }
}
