package zplusplus.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import zplusplus.ast.*;
import zplusplus.lexer.*;

/**
 * Class for parsing input into an abstract syntax tree
 * that is stored as a list of statements so that the interpreter
 * can execute it.
 *
 * @author Zubair Abdul Matin
 */
public class Parser {

    private int parserCounter;
    private List<Token> tokens;

    public Parser() {
        parserCounter = 0;
    }

    public List<Statement> parse(String input) {
        List<Statement> statements = new ArrayList<>();

        Lexer lexer = Lexer.getInstance();
        List<Token> tokens  = lexer.tokenize(input);

        if (tokens.isEmpty()) {
            return null;
        }

        while (!isAtEnd()) {
            if (isDeclaration()) {
                if (tokens.get(parserCounter).type() == TokenType.DEF) {
                    handleFuncDeclaration();
                } else {
                    handleVarDeclaration();
                }
            }else if (isStatement()) {

                switch (tokens.get(parserCounter).type()) {
                    case IF -> handleIfStatement();
                    case WHILE -> handleWhileStatement();
                    case FOR -> handleForStatement();
                    case RETURN -> handleReturnStatement();
                    case BREAK -> handleBreakStatement();
                    case PRINT -> handlePrintStatement();
                    case LEFT_BRACE -> handleLeftBraceStatement();
                    default -> handleOtherStatements();
                }

            }
        }

        return statements;
    }

    private boolean isAtEnd() {
        if (parserCounter >= tokens.size()) { return true; }
        return tokens.get(parserCounter).type() == TokenType.EOF;
    }

    private boolean match(List<TokenType> tokenTypes) {
        for (TokenType tokenType : tokenTypes) {
            if (tokens.get(parserCounter).type() == tokenType) { return true; }
        }
        return false;
    }

    private boolean isDeclaration() {
        List<TokenType> tokenTypes = List.of(TokenType.STRING_TYPE, TokenType.INT_TYPE, TokenType.BOOLEAN_TYPE, TokenType.DEF);
        return match(tokenTypes);
    }

    private boolean isStatement() {
        List<TokenType> tokenTypes = List.of(
                TokenType.IF, TokenType.WHILE, TokenType.FOR, TokenType.RETURN,
                TokenType.BREAK, TokenType.PRINT, TokenType.LEFT_BRACE
        );
        return match(tokenTypes);
    }

    /* Expression engine */

    // addition and subtraction
    private Expression term() {
        Expression expression = factor();

        while (tokens.get(parserCounter).type() == TokenType.PLUS || tokens.get(parserCounter).type() == TokenType.MINUS) {
            Token termOperator = tokens.get(parserCounter++);
            Expression rightExpression = factor();

            expression = new BinaryExpression(expression, termOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // other math operators
    private Expression factor() {
        Expression expression;
        expression = primary(tokens.get(parserCounter++));

        while (tokens.get(parserCounter).type() == TokenType.MULTIPLY ||
            tokens.get(parserCounter).type() == TokenType.DIVIDE ||
            tokens.get(parserCounter).type() == TokenType.MODULO
        ) {
            Token factorOperator = tokens.get(parserCounter++);
            Expression rightExpression = primary(tokens.get(parserCounter++));

            expression = new BinaryExpression(expression, factorOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // number values
    private Expression primary(Token token) {
        return new LiteralExpression(token.tokenValue(), token.lineNumber());
    }

    private void handleFuncDeclaration() {}

    private void handleVarDeclaration() {}

    private void handleIfStatement() {}

    private void handleWhileStatement() {}

    private void handleForStatement() {}

    private void handleReturnStatement() {}

    private void handleBreakStatement() {}

    private void handlePrintStatement() {}

    private void handleLeftBraceStatement() {}

    private void handleOtherStatements() {}
}
