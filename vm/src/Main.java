/**
 * Main Class that runs everything
 *
 * @author Zubair Abdul Matin
 */
public class Main {
    /**
     * main function - ties entire VM together
     * @param args arguments passed in (output file)
     */
    public static void main(String[] args) {
        if (args.length < 1 || !args[0].endsWith(".bin")) {
            System.err.println("Usage: java Main <program.bin>");
            return;
        }

        VirtualMachine vm;
        if (args.length == 2 || args[0].equals("--debug")) {
            vm = new VirtualMachine(true);
        }else {
            vm = new VirtualMachine(false);
        }


        vm.loadFromBinary(args[0]);
        vm.executeProgram();
        vm.returnStackData();
        vm.printGlobalVariables();
    }
}
