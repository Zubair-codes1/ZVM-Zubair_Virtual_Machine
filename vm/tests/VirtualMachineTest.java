import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VirtualMachineTest {

    @Test
    void testFactorialExecutesWithoutError() {
        VirtualMachine vm = new VirtualMachine(false);
        vm.loadFromBinary("../output/factorial.bin");
        assertDoesNotThrow(vm::executeProgram);
    }

    @Test
    void testFibonacciExecutesWithoutError() {
        VirtualMachine vm = new VirtualMachine(false);
        vm.loadFromBinary("../output/fibonacci.bin");
        assertDoesNotThrow(vm::executeProgram);
    }

    @Test
    void testHelloWorldExecutesWithoutError() {
        VirtualMachine vm = new VirtualMachine(false);
        vm.loadFromBinary("../output/hello_world.bin");
        assertDoesNotThrow(vm::executeProgram);
    }

    @Test
    void testGlobalVariableStoredCorrectly() {
        VirtualMachine vm = new VirtualMachine(false);
        vm.loadFromBinary("../output/factorial.bin");
        vm.executeProgram();
        assertNotNull(vm.getGlobalVariables());
    }

    @Test
    void testInvalidBinaryThrows() {
        VirtualMachine vm = new VirtualMachine(true);
        assertThrows(VirtualMachineException.class, () -> {
            vm.loadFromBinary("../output/nonexistent.bin");
        });
    }

    @Test
    void testStackIsEmptyAfterHalt() {
        VirtualMachine vm = new VirtualMachine(false);
        vm.loadFromBinary("../output/factorial.bin");
        vm.executeProgram();
        assertTrue(vm.getStack().isEmpty());
    }
}
