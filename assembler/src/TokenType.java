/**
 * Enum TokenType holds the different types of tokens that can appear in my assembly language
 *
 * @author Zubair Abdul Matin
 */
public enum TokenType {
    OPCODE,             // An Opcode.
    INTEGER,            // An integer - in operands
    LABEL_DEFINITION,   // Defining a label
    LABEL_REFERENCE,    // label reference
    IDENTIFIER,         // identifier - such as variable names
    COMMENT,            // Comments
    UNKNOWN             // Unknown token type
}
