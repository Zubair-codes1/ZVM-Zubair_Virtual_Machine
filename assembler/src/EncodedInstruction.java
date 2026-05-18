/**
 * Fully encoded instructions as integers only.
 * This will be used to generate the .bin files
 * @param opcode the opcode integer value
 * @param operand the operand integer value
 */
public record EncodedInstruction(int opcode, int operand, int lineNumber) {
}
