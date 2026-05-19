import java.util.ArrayList;

/**
 * Frame class
 *
 * @author Zubair Abdul Matin
 */
public class Frame {

    // local variables
    private ArrayList<Integer> localVariables;
    // address from where the function was called
    private int returnAddress;

    /**
     * Constructor for frame
     * @param returnAddress return address
     */
    public Frame(int returnAddress) {
        this.localVariables = new ArrayList<>();
        this.returnAddress = returnAddress;
    }

    /**
     * Local variables getter method
     * @return local variables
     */
    public ArrayList<Integer> getLocalVariables() {
        return localVariables;
    }

    /**
     * Set return address
     * @param address return address
     */
    public void setReturnAddress(int address) {
        this.returnAddress = address;
    }

    /**
     * return address getter
     * @return return address
     */
    public int getReturnAddress() {
        return returnAddress;
    }
}
