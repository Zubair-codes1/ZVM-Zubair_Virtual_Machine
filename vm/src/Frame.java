import java.util.ArrayList;

public class Frame {

    private ArrayList<Integer> localVariables;
    private int returnAddress;

    public Frame() {
        this.localVariables = new ArrayList<>();
        this.returnAddress = -1;
    }

    public Frame(int returnAddress) {
        this.localVariables = new ArrayList<>();
        this.returnAddress = returnAddress;
    }

    public ArrayList<Integer> getLocalVariables() {
        return localVariables;
    }

    public void setReturnAddress(int address) {
        this.returnAddress = address;
    }

    public int getReturnAddress() {
        return returnAddress;
    }
}
