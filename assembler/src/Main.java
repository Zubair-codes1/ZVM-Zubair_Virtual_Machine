/**
 * Main class for assembler.
 * Calls all the other parts of the assembler.
 * Takes in an input file and produces an output file
 */
public class Main {
    /**
     * main function that handles assembler execution
     * @param args input file and output file
     */
    public static void main(String[] args) {
        if (
                args.length != 3 || !args[1].equals("-o") || !args[0].endsWith(".asm") || !args[2].endsWith(".bin")
        ) {
            System.err.println("Usage: java Main <input-file> -o <output-file>");
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = args[2];

        try {
            Assembler assembler = new Assembler(inputFilePath, outputFilePath);
            assembler.assemble();
        } catch (AssemblerException e) {
            System.err.println("[ASSEMBLER ERROR]: " + e.getMessage());
        }
    }
}
