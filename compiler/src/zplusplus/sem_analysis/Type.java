package zplusplus.sem_analysis;

/**
 * Enum to hold the different types of variables/functions that
 * might be encountered during analysis.
 *
 * @author Zubair Abdul Matin
 */
public enum Type {
    INT,
    STRING,
    BOOLEAN,
    VOID,       // used for semantic purposes (no return value)
    ERROR
}
