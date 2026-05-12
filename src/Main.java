import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        VirtualMachine virtualMachine = new VirtualMachine();
        virtualMachine.loadFromFile("src/test.txt");
        virtualMachine.executeProgram();
        virtualMachine.returnStackData();
    }

}
