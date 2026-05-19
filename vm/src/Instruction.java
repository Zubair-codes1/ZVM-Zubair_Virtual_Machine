/**
 * Instruction record class
 * @param opcode opcode
 * @param operand operand
 * @param lineNumber line number
 *
 * @author Zubair Abdul Matin
 */
public record Instruction(OpCode opcode, Integer operand, Integer lineNumber) {
}
