package zplusplus.sem_analysis;

import zplusplus.ast.*;
import zplusplus.exceptions.SemanticException;
import zplusplus.sem_analysis.symbol.Symbol;

import java.util.List;

/**
 * Semantic analyser class. Checks the semantics for all statements
 * and expressions.
 *
 * @author Zubair Abdul Matin
 */
public class Analyser {
    private List<Statement> statements;
    private Environment currentEnvironment;
    private Type currentFunctionReturnType;
    private int loopDepth;

    /**
     * Constructor for analyser class,
     * initialises list of statements, current environment,
     * current function return type and the depth of the current loop
     * @param statements list of statements
     */
    public Analyser(List<Statement> statements) {
        this.statements = statements;
        this.currentEnvironment = new Environment(null);
        this.currentFunctionReturnType = null;
        this.loopDepth = 0;
    }

    /**
     * Loops through each statement and calls analyseStatement()
     */
    public void analyse() {
        for (Statement statement : statements) {
            analyseStatement(statement);
        }
    }

    /**
     * Checks type of statement and then calls the corresponding
     * handler function.
     *
     * @param statement current statement
     */
    private void analyseStatement(Statement statement) {
        switch (statement) {
            case VariableDeclarationStatement varDeclStatement -> analyseVarDecl(varDeclStatement);
            case AssignmentStatement assignmentStatement -> analyseAssign(assignmentStatement);
            case IfStatement ifStatement -> analyseIf(ifStatement);
            case WhileStatement whileStatement -> analyseWhile(whileStatement);
            case ForStatement forStatement -> analyseFor(forStatement);
            case ReturnStatement returnStatement -> analyseReturn(returnStatement);
            case BreakStatement breakStatement -> analyseBreak(breakStatement);
            case BlockStatement blockStatement -> analyseBlock(blockStatement);
            case FunctionDeclarationStatement functionDeclarationStatement ->
                    analyseFuncDecl(functionDeclarationStatement);
            case PrintStatement printStatement -> analysePrint(printStatement);
            case ExpressionStatement expressionStatement -> analyseExpression(expressionStatement.getExpression());
            default -> throw new SemanticException("Semantic Error: Not a valid statement", statement.getLineNumber());
        }
    }

    private void analyseVarDecl(VariableDeclarationStatement varDeclStatement) {
        return;
    }

    private void analyseAssign(AssignmentStatement assignmentStatement) {
        return;
    }

    private void analyseIf(IfStatement ifStatement) {
        return;
    }

    private void analyseWhile(WhileStatement whileStatement) {
        return;
    }

    private void analyseFor(ForStatement forStatement) {
        return;
    }

    private void analyseReturn(ReturnStatement returnStatement) {
        return;
    }

    private void analyseBreak(BreakStatement breakStatement) {
        return;
    }

    private void analyseBlock(BlockStatement blockStatement) {
        return;
    }

    private void analyseFuncDecl(FunctionDeclarationStatement functionDeclarationStatement) {
        return;
    }

    private void analysePrint(PrintStatement printStatement) {
        return;
    }

    private Type analyseExpression(Expression expression) {
        if (expression instanceof LiteralExpression literalExpression) {
            if (literalExpression.getValue() instanceof Integer) {
                return Type.INT;
            }else if (literalExpression.getValue() instanceof String) {
                return Type.STRING;
            }else if (literalExpression.getValue() instanceof Boolean) {
                return Type.BOOLEAN;
            }else {
                return Type.ERROR;
            }
        }else if (expression instanceof VariableExpression variableExpression) {
            return analyseVarExpression(variableExpression);
        }else if (expression instanceof BinaryExpression binaryExpression) {
            return analyseBinExpr(binaryExpression);
        }

        return Type.ERROR;
    }

    private Type analyseVarExpression(VariableExpression variableExpression) {
        Symbol symbol = currentEnvironment.getSymbol(variableExpression.getName());
        return symbol != null ? symbol.getType() : Type.ERROR;
    }

    private Type analyseBinExpr(BinaryExpression binaryExpression) {
        return null;
    }
}
