import java.util.*;

/**
 * Symbol table class (pass 1) of the assembler
 *
 * @author Zubair Abdul Matin
 */
public class SymbolTable {
    // stores labels and their instruction index
    private Map<String, Integer> labelMap;
    // stores all the constants
    private List<String> constantPool;

    /**
     * Constructor to initialise the symbol table
     */
    public SymbolTable() {
        this.labelMap = new HashMap<>();
        this.constantPool = new ArrayList<>();
    }

    /**
     * builds the symbol table by populting the constant pool
     * and the label map
     * @param parsedLines the parsed lines
     */
    public void build(List<ParsedLine> parsedLines) {
        int counter = 0;
        for (ParsedLine parsedLine : parsedLines) {
            if (parsedLine.isLabelDefinition()) {
                labelMap.put(parsedLine.labelName().substring(1), counter);
            }else {
                if (parsedLine.operandType() != null && parsedLine.operandType().equals(TokenType.IDENTIFIER)) {
                    if (!constantPool.contains(parsedLine.operandValue())) {
                        constantPool.add(parsedLine.operandValue());
                    }
                }
                else if (parsedLine.operandType() != null && parsedLine.operandType().equals(TokenType.STRING)) {
                    if (!constantPool.contains(parsedLine.operandValue())) {
                        constantPool.add(parsedLine.operandValue());
                    }
                }
                counter++;
            }
        }
    }

    /**
     * Getting the index of the label if it exists
     * @param labelName name of the label
     * @return an integer for the index
     */
    public Integer resolveLabel(String labelName) {
        String cleanName = labelName.startsWith(":") ? labelName.substring(1) : labelName;
        if (labelMap.containsKey(cleanName)) {
            return labelMap.get(cleanName);
        }

        throw new AssemblerException("Label " + labelName + " not found");
    }

    /**
     * Get index of constant pool
     * @param constantName the name of the constant
     * @return the index as an integer
     */
    public Integer resolveConstantPool(String constantName) {
        if (constantPool.contains(constantName)) {
            return constantPool.indexOf(constantName);
        }

        throw new AssemblerException("Constant pool " + constantName + " not found");
    }

    /**
     * Returns the constant pool
     * @return constant pool
     */
    public List<String> getConstantPool() {
        return constantPool;
    }
}
