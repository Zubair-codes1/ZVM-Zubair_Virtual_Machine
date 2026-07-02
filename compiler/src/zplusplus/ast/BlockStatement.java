package zplusplus.ast;

import java.util.List;

/**
 * Handles blocks of statements (e.g. a function block)
 *
 * @author Zubair Abdul Matin
 */
public class BlockStatement extends Statement {

    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements, int lineNumber) {
        super(lineNumber);

        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Statement statement : statements) {
            sb.append(statement.toString());
            sb.append("\n");
        }
        sb.append("}");

        return sb.toString();
    }
}
