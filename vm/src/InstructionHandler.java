/**
 * Interface for handling instruction functionality
 */
public interface InstructionHandler {
    /**
     * Executes functionality for instructions
     * @param instruction instruction
     * @param opCode opcode
     * @param virtualMachine virtual machine
     * @throws VirtualMachineException exception thrown
     */
    void execute(Instruction instruction, OpCode opCode, VirtualMachine virtualMachine) throws VirtualMachineException;
}
