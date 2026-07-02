package zplusplus.ast;

public class WhileStatement extends Statement {

    public final Expression condition;
    // likely a block statement
    public final Statement body;

    public WhileStatement(Expression condition, Statement body, int lineNumber) {
        super(lineNumber);

        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public String toString() {

        return "while (" + condition.toString() + ") {\n" + body.toString() + "}\n";
    }
}
