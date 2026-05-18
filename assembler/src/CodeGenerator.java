import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    private static final CodeGenerator INSTANCE = new CodeGenerator();
    private List<EncodedInstruction> instructions = new ArrayList<>();

    private CodeGenerator() {}
    public static CodeGenerator getInstance() {
        return INSTANCE;
    }


}
