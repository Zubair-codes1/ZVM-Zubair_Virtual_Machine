public class Main {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].endsWith(".bin")) {
            System.err.println("Usage: java Main <program.bin>");
            return;
        }

        VirtualMachine vm = new VirtualMachine();
        vm.loadFromBinary(args[0]);
        vm.executeProgram();
        vm.returnStackData();
        vm.printGlobalVariables();
    }
}
