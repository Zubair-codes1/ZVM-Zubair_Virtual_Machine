package zplusplus.sem_analysis;

/**
 * Class to hold symbols (variables/functions and their types)
 * @param name name of variable/function
 * @param type type of variable/function
 * @param isFunc boolean value to check if it is a function
 *
 * @author Zubair Abdul Matin
 */
public record Symbol(String name, String type, boolean isFunc, int mem_offset) {
}
