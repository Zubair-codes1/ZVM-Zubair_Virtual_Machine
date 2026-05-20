import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class FullAssemblerTest {

    @Test
    void testAssemblesFactorial() {
        new Assembler("../programs/factorial.asm", "../output/factorial.bin").assemble();
        File output = new File("../output/factorial.bin");
        assertTrue(output.exists());
        assertTrue(output.length() > 0);
    }

    @Test
    void testAssemblesFibonacci() {
        new Assembler("../programs/fibonacci.asm", "../output/fibonacci.bin").assemble();
        File output = new File("../output/fibonacci.bin");
        assertTrue(output.exists());
        assertTrue(output.length() > 0);
    }

    @Test
    void testAssemblesHelloWorld() {
        new Assembler("../programs/hello_world.asm", "../output/hello_world.bin").assemble();
        File output = new File("../output/hello_world.bin");
        assertTrue(output.exists());
        assertTrue(output.length() > 0);
    }

    @Test
    void testInvalidFileThrows() {
        assertThrows(AssemblerException.class, () -> {
            new Assembler("../programs/nonexistent.asm", "../output/nonexistent.bin").assemble();
        });
    }
}
