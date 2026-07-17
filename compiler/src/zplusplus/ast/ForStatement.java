package zplusplus.ast;

public class ForStatement extends Statement {
    private final Statement initializer; // e.g., int i = 0;
    private final Expression condition;   // e.g., i < 10
    private final Statement increment;   // e.g., i = i + 1
    private final Statement body;        // The loop body

    public ForStatement(Statement initializer, Expression condition, Statement increment, Statement body, int lineNumber) {
        super(lineNumber);
        this.initializer = initializer;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public Statement getInitializer() {
        return initializer;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getIncrement() {
        return increment;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public String toString() {
        String initStr = (initializer != null) ? initializer.toString().trim() : "";
        String condStr = (condition != null) ? condition.toString() : "";
        String incStr  = (increment != null) ? increment.toString().trim() : "";
        String bodyStr = (body != null) ? body.toString() : "{}";

        return "for (" + initStr + " " + condStr + "; " + incStr + ") " + bodyStr;
    }

}
