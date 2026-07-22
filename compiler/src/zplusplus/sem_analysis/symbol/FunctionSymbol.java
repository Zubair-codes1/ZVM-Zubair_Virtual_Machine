package zplusplus.sem_analysis.symbol;

import zplusplus.sem_analysis.Type;

import java.util.List;

/**
 * Function symbol extends generic symbol class
 *
 * @author Zubair Abdul Matin
 */
public class FunctionSymbol extends Symbol {
    private List<VariableSymbol> parameters;
    private int stackOffset;

    /**
     * Constructor for function symbol class
     * @param name name
     * @param type type
     * @param parameters list of parameters
     * @param stackOffset stack offset (memory stack)
     */
    public FunctionSymbol(String name, Type type, List<VariableSymbol> parameters, int stackOffset) {
        super(name, type);

        this.parameters = parameters;
        this.stackOffset = stackOffset;
    }

    /**
     * Gets parameters
     * @return list of parameters
     */
    public List<VariableSymbol> getParameters() {
        return parameters;
    }

    /**
     * Gets Stack offset
     * @return parameters
     */
    public int getStackOffset() {
        return stackOffset;
    }

    /**
     * Returns function layout as a string
     * @return function as a string
     */
    @Override
    public String toString() {
        return "def " + getType().toString().toLowerCase() + " " + getName() + "(" + getParameters().toString() + ")";
    }
}
