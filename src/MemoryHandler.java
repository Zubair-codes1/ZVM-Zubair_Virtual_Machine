public class MemoryHandler implements InstructionHandler {
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {

        switch (opcode) {
            case LOAD -> handleLoad(instruction, virtualMachine);
            case STORE -> handleStore(instruction, virtualMachine);
        }
    }

    private void handleLoad(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (instruction.operand() >= virtualMachine.getConstantPool().size()) {
            throw new VirtualMachineException("Error: Load command overflow!");
        }

        String variableName = virtualMachine.getConstantPool().get(instruction.operand());
        if (!virtualMachine.getGlobalVariables().containsKey(variableName)) {
            throw new VirtualMachineException("Error: Variable not found!");
        }
        int value = virtualMachine.getGlobalVariables().get(variableName);
        virtualMachine.getStack().push(value);
    }

    private void handleStore(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (instruction.operand() >= virtualMachine.getConstantPool().size()) {
            throw new VirtualMachineException("Error: Store command requires variables!");
        }

        String variableName = virtualMachine.getConstantPool().get(instruction.operand());
        if (virtualMachine.getStack().isEmpty()) {
            throw new VirtualMachineException("Error: Store command requires data!");
        }
        int value = virtualMachine.getStack().pop();
        virtualMachine.getGlobalVariables().put(variableName, value);
    }
}
