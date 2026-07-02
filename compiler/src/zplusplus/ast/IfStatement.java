package zplusplus.ast;

/**
 * Class for If statements (condition, if branch and else branch)
 *
 * @author Zubair Abdul Matin
 */
public class IfStatement extends Statement {

    private final Expression condition;

    // will be block statements
    private final Statement ifBranch;
    private final Statement elseBranch;

    public IfStatement(Expression condition, Statement ifBranch, Statement elseBranch, int lineNumber) {
        super(lineNumber);

        this.condition = condition;
        this.ifBranch = ifBranch;
        this.elseBranch = elseBranch;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getIfStatement() {
        return ifBranch;
    }

    public Statement getElseStatement() {
        return elseBranch;
    }

    @Override
    public String toString() {

        if (elseBranch != null) {
            return "if (" + condition.toString() + ") " + ifBranch.toString() + " else " + elseBranch.toString();
        }

        return "if (" + condition.toString() + ") " + ifBranch.toString();
    }
}
