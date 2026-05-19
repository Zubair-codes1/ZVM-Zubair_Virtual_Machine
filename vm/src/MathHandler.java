import java.util.ArrayList;

/**
 * Class to handle math operations
 */
public class MathHandler implements InstructionHandler {

    /**
     * Executes functinoality
     * @param instruction instruction
     * @param opcode opcode
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {
        switch (opcode) {
            case ADD -> handleAdd(virtualMachine);
            case SUB -> handleSub(virtualMachine);
            case MULT -> handleMult(virtualMachine);
            case DIV -> handleDiv(virtualMachine);
            case MOD -> handleMod(virtualMachine);
            case LSHIFT -> handleLShift(virtualMachine);
            case RSHIFT -> handleRShift(virtualMachine);
            case INC_LOCAL -> handleIncDec(instruction, opcode, virtualMachine, OpCode.INC_LOCAL);
            case DEC_LOCAL -> handleIncDec(instruction, opcode, virtualMachine, OpCode.DEC_LOCAL);
            default -> throw new VirtualMachineException("Error: Unknown opcode: " + opcode);
        }
    }

    /**
     * Hnadles ADD functionality
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleAdd(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: ADD command needs two operands!");
        }
        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();

        virtualMachine.getStack().push(left + right);
    }

    /**
     * Handles SUB functionality
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleSub(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: SUB command needs two operands!");
        }
        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();
        virtualMachine.getStack().push(left - right);
    }

    /**
     * Handles MULT functionality
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleMult(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: MULT command needs two operands!");
        }
        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();
        virtualMachine.getStack().push(left * right);
    }

    /**
     * Handles DIV functionality
     * @param virtualMachine virutal machine
     * @throws VirtualMachineException exception
     */
    private void handleDiv(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: DIV command needs two operands!");
        }
        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();

        if  (right == 0) {
            throw new VirtualMachineException("Error: Division by zero!");
        }
        virtualMachine.getStack().push(left / right);
    }

    /**
     * Handles MOD functionality
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleMod(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: MOD command needs two operands!");
        }
        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();

        if (right == 0) {
            throw new VirtualMachineException("Error: Division by zero!");
        }
        virtualMachine.getStack().push(left % right);
    }

    /**
     * Handles LSHIFT functionality
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleLShift(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: LShift command needs two operands!");
        }
        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();
        virtualMachine.getStack().push(left << right);
    }

    /**
     * Handles RSHIFT functionality
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleRShift(VirtualMachine virtualMachine) throws VirtualMachineException {
        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: RShift command needs two operands!");
        }
        int right = virtualMachine.getStack().pop();
        int left = virtualMachine.getStack().pop();
        virtualMachine.getStack().push(left >> right);
    }

    /**
     * handles INC_LOCAL/DEC_LOCAL functionality
     * @param instruction instruction
     * @param opcode opcode
     * @param virtualMachine virtual machine
     * @param type type of instruction
     * @throws VirtualMachineException exception
     */
    private void handleIncDec(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine, OpCode type) throws VirtualMachineException {
        ArrayList<Integer> localVariables = getIntegers(instruction, virtualMachine);

        Integer currentValue = localVariables.get(instruction.operand());
        int safeValue = (currentValue == null) ? 0 : currentValue;
        if (type.equals(OpCode.INC_LOCAL)) {
            safeValue += 1;
        }else if (type.equals(OpCode.DEC_LOCAL)) {
            safeValue -= 1;
        }
        localVariables.set(instruction.operand(), safeValue);
    }

    /**
     * Gets integers from local variable
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @return a list of integers
     */
    private static ArrayList<Integer> getIntegers(Instruction instruction, VirtualMachine virtualMachine) {
        if (instruction.operand() == null) {
            throw new VirtualMachineException("Error: Null values for operand!");
        }

        Frame activeFrame = virtualMachine.getActiveFrame();
        if (activeFrame == null) {
            throw new VirtualMachineException("Error: Frame is null!");
        }
        ArrayList<Integer> localVariables = activeFrame.getLocalVariables();
        if (localVariables.size() <= instruction.operand()) {
            for (int i = localVariables.size(); i <= instruction.operand(); i++) {
                localVariables.add(0);
            }
        }
        return localVariables;
    }
}
