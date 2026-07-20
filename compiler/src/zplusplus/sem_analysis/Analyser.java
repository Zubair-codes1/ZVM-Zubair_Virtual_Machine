package zplusplus.sem_analysis;

import zplusplus.ast.Statement;
import java.util.List;

public class Analyser {
    private List<Statement> statements;

    public Analyser(List<Statement> statements) {
        this.statements = statements;
    }

    public void analyse() {
        for (Statement statement : statements) {

        }
    }
}
