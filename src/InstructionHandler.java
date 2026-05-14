public interface InstructionHandler {
    void execute(Instruction instruction, OpCode opCode, VirtualMachine virtualMachine) throws VirtualMachineException;
}
