package zplusplus.ast;

/**
 * Holds the type and identifier of a function paramter
 * @param type data type
 * @param name paramter name
 *
 * @author Zubair Abdul Matin
 */
public record Parameter(String type, String name) {

    @Override
    public String toString() {
        return type + " " + name;
    }
}
