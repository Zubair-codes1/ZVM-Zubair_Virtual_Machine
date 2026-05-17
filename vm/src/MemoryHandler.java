public class MemoryHandler implements InstructionHandler {
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {

        switch (opcode) {
            case LOAD -> handleLoad(instruction, virtualMachine);
            case STORE -> handleStore(instruction, virtualMachine);
            case LOAD_LOCAL -> handleLoadLocal(instruction, virtualMachine);
            case STORE_LOCAL -> handleStoreLocal(instruction, virtualMachine);
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

        if (virtualMachine.getGlobalVariables().isEmpty()) {
            throw new VirtualMachineException("Error: Variables not found!");
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

    private void handleLoadLocal(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        Frame activeFrame = virtualMachine.getActiveFrame();
        if (activeFrame == null) {
            throw new VirtualMachineException("Error: Load command requires active frame!");
        }

        if (instruction.operand() >= activeFrame.getLocalVariables().size()) {
            for (int i = activeFrame.getLocalVariables().size(); i <= instruction.operand(); i++) {
                activeFrame.getLocalVariables().add(0);
            }
        }
        if (activeFrame.getLocalVariables().get(instruction.operand()) == null) {
            throw new VirtualMachineException("Error: Load local command requires local variable!");
        }

        int value = activeFrame.getLocalVariables().get(instruction.operand());
        virtualMachine.getStack().push(value);
    }

    private void handleStoreLocal(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        Frame activeFrame = virtualMachine.getActiveFrame();
        if (activeFrame == null) {
            throw new VirtualMachineException("Error: Store command requires active frame!");
        }
        int value = virtualMachine.getStack().pop();

        if (instruction.operand() >= activeFrame.getLocalVariables().size()) {
            for (int i = activeFrame.getLocalVariables().size(); i <= instruction.operand(); i++) {
                activeFrame.getLocalVariables().add(0);
            }
        }
        activeFrame.getLocalVariables().set(instruction.operand(), value);

    }
}
