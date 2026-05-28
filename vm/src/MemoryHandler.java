/**
 * Class to handle memory instructions
 *
 * @author Zubair Abdul Matin
 */
public class MemoryHandler implements InstructionHandler {

    /**
     * Executes memory instructions
     * @param instruction instruction
     * @param opcode opcode
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    @Override
    public void execute(Instruction instruction, OpCode opcode, VirtualMachine virtualMachine) throws VirtualMachineException {

        switch (opcode) {
            case LOAD -> handleLoad(instruction, virtualMachine);
            case STORE -> handleStore(instruction, virtualMachine);
            case LOAD_LOCAL -> handleLoadLocal(instruction, virtualMachine);
            case STORE_LOCAL -> handleStoreLocal(instruction, virtualMachine);
            case ALLOC -> handleMemAlloc(instruction, virtualMachine);
            case LOAD_HEAP -> handleLoadHeap(instruction, virtualMachine);
            case STORE_HEAP -> handleStoreHeap(instruction, virtualMachine);

        }
    }

    /**
     * handles LOAD
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
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

    /**
     * handles STORE
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
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

    /**
     * handle LOAD_LOCAL
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
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

    /**
     * handles STORE_LOCAL
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
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

    /**
     * Handles memory allocation
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException virtual machine exception
     */
    private void handleMemAlloc(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (instruction.operand() == null) {
            throw new VirtualMachineException("Error: Mem alloc command requires an operand!");
        }
        int heapPointer = virtualMachine.getHeapPointer();
        virtualMachine.setHeapPointer(heapPointer + instruction.operand());
        virtualMachine.getStack().push(heapPointer);
    }

    /**
     * Stores a value at a given address in the heap
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleStoreHeap(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (instruction.operand() == null) {
            throw new VirtualMachineException("Error: Store heap command requires an operand!");
        }

        if (virtualMachine.getStack().size() < 2) {
            throw new VirtualMachineException("Error: Store heap command requires at least two stack values!");
        }

        int offset = instruction.operand();
        int value = virtualMachine.getStack().pop();
        int address = virtualMachine.getStack().pop();

        if (address + offset < 0 || address + offset + 3 >= virtualMachine.getHeap().length) {
            throw new VirtualMachineException("Error: Heap load out of bounds memory access!");
        }

        virtualMachine.getHeap()[address + offset] = (byte) ((value >> 24) & 0xFF);
        virtualMachine.getHeap()[address + offset + 1] = (byte) ((value >> 16) & 0xFF);
        virtualMachine.getHeap()[address + offset + 2] = (byte) ((value >> 8) & 0xFF);
        virtualMachine.getHeap()[address + offset + 3] = (byte) ((value) & 0xFF);
    }

    /**
     * Loads from the heap
     * @param instruction instruction
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception
     */
    private void handleLoadHeap(Instruction instruction, VirtualMachine virtualMachine) throws VirtualMachineException {
        if (instruction.operand() == null) {
            throw new VirtualMachineException("Error: Load heap command requires an operand!");
        }

        if (virtualMachine.getStack().isEmpty()) {
            throw new VirtualMachineException("Error: Load heap command requires at least one stack value!");
        }

        int address = virtualMachine.getStack().pop();
        int offset = instruction.operand();

        if (address + offset < 0 || address + offset + 3 >= virtualMachine.getHeap().length) {
            throw new VirtualMachineException("Error: Heap load out of bounds memory access!");
        }

        int originalValue = (virtualMachine.getHeap()[address + offset]     << 24) |
                ((virtualMachine.getHeap()[address + offset + 1] & 0xFF) << 16) |
                ((virtualMachine.getHeap()[address + offset + 2] & 0xFF) << 8)  |
                (virtualMachine.getHeap()[address + offset + 3]  & 0xFF);
        virtualMachine.getStack().push(originalValue);
    }
}
