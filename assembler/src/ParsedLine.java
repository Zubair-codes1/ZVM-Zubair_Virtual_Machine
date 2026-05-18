/**
 * Parses a line into a record containing the key information for that line
 * @param opcode the opcode
 * @param operandType the type of operand
 * @param operandValue the value of the operand
 * @param isLabelDefinition checks if the operand is a label
 * @param labelName label name if it is a label
 * @param lineNumber line number
 * @author Zubair Abdul Matin
 */
public record ParsedLine(
        String opcode, TokenType operandType,
        String operandValue, boolean isLabelDefinition,
        String labelName, int lineNumber
) {}