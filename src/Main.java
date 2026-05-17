public class Main {
    public static void main(String[] args) {
        VirtualMachine virtualMachine = new VirtualMachine();
        virtualMachine.loadFromFile("src/test.txt");
        virtualMachine.executeProgram();
        virtualMachine.returnStackData();
        virtualMachine.printGlobalVariables();
    }
}
