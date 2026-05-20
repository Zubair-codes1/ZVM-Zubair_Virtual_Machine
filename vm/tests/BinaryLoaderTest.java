import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class BinaryLoaderTest {

    @Test
    void testLoadsFactorialCorrectly() {
        BinaryLoader loader = new BinaryLoader();
        LoadedProgram program = loader.loadProgram("../output/factorial.bin");
        assertFalse(program.instructions().isEmpty());
    }

    @Test
    void testConstantPoolLoadsCorrectly() {
        BinaryLoader loader = new BinaryLoader();
        LoadedProgram program = loader.loadProgram("../output/factorial.bin");
        assertNotNull(program.constantPool());
    }

    @Test
    void testNonExistentFileThrows() {
        BinaryLoader loader = new BinaryLoader();
        assertThrows(VirtualMachineException.class, () -> {
            loader.loadProgram("../output/nonexistent.bin");
        });
    }

    @Test
    void testInvalidMagicNumberThrows() throws IOException {
        File fakeFile = new File("../output/fake.bin");
        try{
            try (DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream(fakeFile))) {
                dos.writeShort(0x1234); // wrong magic number
                dos.writeByte(1);
                dos.writeInt(0);
                dos.writeInt(0);
                dos.writeInt(0);
            }

            BinaryLoader loader = new BinaryLoader();
            assertThrows(VirtualMachineException.class, () -> {
                loader.loadProgram("../output/fake.bin");
            });
        }finally {
            fakeFile.delete();
        }
    }

    @Test
    void testFibonacciLoadsCorrectly() {
        BinaryLoader loader = new BinaryLoader();
        LoadedProgram program = loader.loadProgram("../output/fibonacci.bin");
        assertFalse(program.instructions().isEmpty());
    }

    @Test
    void testHelloWorldLoadsCorrectly() {
        BinaryLoader loader = new BinaryLoader();
        LoadedProgram program = loader.loadProgram("../output/hello_world.bin");
        assertFalse(program.instructions().isEmpty());
    }
}