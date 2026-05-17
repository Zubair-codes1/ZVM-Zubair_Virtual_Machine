public class Main {
    public static void main(String[] args) {
        VirtualMachine virtualMachine = new VirtualMachine();
        virtualMachine.loadFromFile("vm/src/test.txt");
        virtualMachine.executeProgram();
        virtualMachine.returnStackData();
        virtualMachine.printGlobalVariables();
    }
}
