package zplusplus.sem_analysis.symbol;

import zplusplus.sem_analysis.Type;

/**
 * Generic Symbol class to hold the name of the
 * symbol (variable/function) along with their type/return type.
 */
public abstract class Symbol {
    private String name;
    private Type type;

    /**
     * Constructor for Symbol class
     * @param name name of symbol
     * @param type type of symbol
     */
    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets type
     * @return type
     */
    public Type getType() {
        return type;
    }

    /**
     * To string method for the symbol
     * @return symbol formatted as a string
     */
    @Override
    abstract public String toString();
}
