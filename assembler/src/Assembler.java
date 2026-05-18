import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Assembler {
    private String inputFilePath;
    private String outputFilePath;

    public Assembler(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public void assemble() {

        try {
            List<String> lines = Files.readAllLines(Paths.get(inputFilePath));

            List<ParsedLine> parsedLines = Parser.getInstance().parse(lines);
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.build(parsedLines);

            List<EncodedInstruction> encodedInstructions = CodeGenerator.getInstance().generateInstructions(parsedLines, symbolTable);
            BinaryWriter.getInstance().write(encodedInstructions, symbolTable.getConstantPool(), outputFilePath);

            System.out.println("Assembling complete. File produced: " + outputFilePath);

        } catch (IOException e) {
            throw new AssemblerException("Could not read file: " + inputFilePath);
        }
    }
}
