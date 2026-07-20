package zplusplus.sem_analysis.symbol;

/**
 * Variable symbol class
 *
 * @author Zubair Abdul Matin
 */
public class VariableSymbol extends Symbol {

    /**
     * Variable Symbol constructor
     * @param name variable name
     * @param type variable type
     */
    public VariableSymbol(String name, String type) {
        super(name, type);
    }

    /**
     * Displays variable as a string
     * @return variable as a string
     */
    @Override
    public String toString() {
        return getType() + " " + getName();
    }
}
