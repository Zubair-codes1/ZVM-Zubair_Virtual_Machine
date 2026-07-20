package zplusplus.sem_analysis;

import java.util.List;

/**
 * Environment/Scope node class to hold information for each scope level
 * Holds its own symbols and the environment of its parent scope
 *
 * @author Zubair Abdul Matin
 */
public class Environment {
    private Environment parentEnvironment;
    private List<Symbol> localSymbols;
}
