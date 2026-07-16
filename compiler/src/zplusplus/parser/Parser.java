package zplusplus.parser;

import java.util.ArrayList;
import java.util.List;
import zplusplus.ast.*;
import zplusplus.lexer.*;
import zplusplus.exceptions.*;

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
        tokens = new ArrayList<>();
    }

    public List<Statement> parse(String input) {
        List<Statement> statements = new ArrayList<>();

        Lexer lexer = Lexer.getInstance();
        tokens  = lexer.tokenize(input);

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

            } else {
                handleOtherStatements();
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

    /*
        ------------------------
        Expression engine
        -----------------------
    */

    // kicks off the expression engine
    private Expression expression() {
        return logicalOr();
    }

    // handles logical Or
    private Expression logicalOr() {
        Expression expression = logicalAnd();

        while (tokens.get(parserCounter).type() == TokenType.LOGICAL_OR) {
            Token termOperator = tokens.get(parserCounter++);
            Expression rightExpression = logicalAnd();

            expression = new BinaryExpression(expression, termOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // handles logical and
    private Expression logicalAnd() {
        Expression expression = equality();

        while (tokens.get(parserCounter).type() == TokenType.LOGICAL_AND) {
            Token termOperator = tokens.get(parserCounter++);
            Expression rightExpression = equality();

            expression = new BinaryExpression(expression, termOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // handles equality (==)
    private Expression equality() {
        Expression expression = comparison();

        while (tokens.get(parserCounter).type() == TokenType.EQUAL_EQUAL || tokens.get(parserCounter).type() == TokenType.NOT_EQUAL) {
            Token termOperator = tokens.get(parserCounter++);
            Expression rightExpression = comparison();

            expression = new BinaryExpression(expression, termOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // handles comparison operators (>, >=, <, <=)
    private Expression comparison() {
        Expression expression = termOperate();

        while (
                tokens.get(parserCounter).type() == TokenType.GREATER_THAN ||
                tokens.get(parserCounter).type() == TokenType.GREATER_OR_EQUAL ||
                tokens.get(parserCounter).type() == TokenType.LESS_THAN ||
                tokens.get(parserCounter).type() == TokenType.LESS_OR_EQUAL
        ) {
            Token termOperator = tokens.get(parserCounter++);
            Expression rightExpression = termOperate();

            expression = new BinaryExpression(expression, termOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // addition and subtraction
    private Expression termOperate() {
        Expression expression = factorOperate();

        while (tokens.get(parserCounter).type() == TokenType.PLUS || tokens.get(parserCounter).type() == TokenType.MINUS) {
            Token termOperator = tokens.get(parserCounter++);
            Expression rightExpression = factorOperate();

            expression = new BinaryExpression(expression, termOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // other math operators
    private Expression factorOperate() {
        Expression expression;
        expression = unary();

        while (tokens.get(parserCounter).type() == TokenType.MULTIPLY ||
            tokens.get(parserCounter).type() == TokenType.DIVIDE ||
            tokens.get(parserCounter).type() == TokenType.MODULO
        ) {
            Token factorOperator = tokens.get(parserCounter++);
            Expression rightExpression = unary();

            expression = new BinaryExpression(expression, factorOperator, rightExpression, expression.getLineNumber());
        }

        return expression;
    }

    // handles unary operators (higher precedence than binary operators)
    private Expression unary() {
        if (tokens.get(parserCounter).type() == TokenType.LOGICAL_NOT || tokens.get(parserCounter).type() == TokenType.MINUS) {
            Token operator = tokens.get(parserCounter++);
            Expression right = unary();
            return new UnaryExpression(operator, right, operator.lineNumber());
        }

        return primary();
    }

    // handles literals, identifier and groups surrounded by parentheses
    private Expression primary() {
        Token token = tokens.get(parserCounter);

        // handle int and string literals
        if (token.type() == TokenType.INT || token.type() == TokenType.STRING) {
            parserCounter++;
            return new LiteralExpression(token.tokenValue(), token.lineNumber());
        }

        // handle booleans
        if (token.type() == TokenType.TRUE ||  token.type() == TokenType.FALSE) {
            parserCounter++;
            boolean value = token.type() == TokenType.TRUE;
            return new LiteralExpression(value, token.lineNumber());
        }

        // handle variables
        if (token.type() == TokenType.IDENTIFIER) {
            parserCounter++;

            return new LiteralExpression(token.tokenValue(), token.lineNumber());
        }

        // grouping logic
        if (token.type() == TokenType.LEFT_PAREN) {
            parserCounter++;

            // loop back and repeat the process for inner expression
            Expression expression = expression();

            if (tokens.get(parserCounter).type() == TokenType.RIGHT_PAREN) {
                parserCounter++;
                return expression; // inner expression
            } else {
                throw new CompilerException("Syntax error: Missing \")\" at the end of expression.");
            }

        }

        throw new CompilerException("Syntax error: Unexpected token '" + token.tokenValue() + "' at line " +  token.lineNumber());
    }

    /*
        ------------------------
        Statement handlers
        -----------------------
    */

    /**
     * Checks for the current token to see if it is within range,
     * if not then returns EOF token
     * @return token or EOF
     */
    private Token peekToken() {
        if (parserCounter < tokens.size()) {
            return tokens.get(parserCounter);
        }else {
            return tokens.getLast();
        }
    }

    /**
     * Peeks at the next token to see if it within range
     * @return token or EOF
     */
    private Token peekNextToken() {
        if (parserCounter + 1 < tokens.size()) {
            return tokens.get(parserCounter + 1);
        }else {
            return tokens.getLast();
        }
    }

    /**
     * Consumes the current token and advances pointer (helper method)
     * @return consumed token
     */
    private Token advance() {
        if (parserCounter < tokens.size()) {
            parserCounter++;
        }
        // Return the token we just stepped over
        return tokens.get(parserCounter - 1);
    }

    /**
     * Checks the token type, if incorrect then throws a compiler error.
     * @param type type that is checked
     * @param errorMessage error message if type is incorrect
     * @return token or error message
     */
    private Token consume(TokenType type, String errorMessage) {
        if (peekToken().type() == type) {
            return advance(); // Safe, valid, and moves us forward!
        }
        throw new CompilerException(errorMessage);
    }

    private void handleFuncDeclaration() {}

    private Statement handleVarDeclaration() {
        // type token
        Token typeToken = advance();
        String typeName = typeToken.tokenValue();

        // identifier and error message handling
        Token nameToken = consume(TokenType.IDENTIFIER, "Syntax Error: Expected variable name after type.");
        String varName = nameToken.tokenValue();

        // Check for optional initialisation
        Expression initialiser = null;
        if (peekToken().type() == TokenType.ASSIGNMENT) {
            advance(); // Consume the '='
            initialiser = expression(); // Parse whatever expression is on the right-hand side
        }

        // semi colon
        consume(TokenType.SEMICOLON, "Syntax Error: Expected ';' at end of variable declaration.");

        // node
        return new VariableDeclarationStatement(typeName, varName, initialiser, typeToken.lineNumber());
    }

    private void handleIfStatement() {}

    private void handleWhileStatement() {}

    private void handleForStatement() {}

    private void handleReturnStatement() {}

    private void handleBreakStatement() {}

    private void handlePrintStatement() {}

    private void handleLeftBraceStatement() {}

    private Statement handleOtherStatements() {
        Token currentToken = tokens.get(parserCounter);

        // 1. Check if we are looking at: IDENTIFIER '='
        if (currentToken.type() == TokenType.IDENTIFIER &&
                parserCounter + 1 < tokens.size() &&
                tokens.get(parserCounter + 1).type() == TokenType.ASSIGNMENT) {

            // 2. Grab the name of the variable
            String varName = currentToken.tokenValue();

            // 3. Skip past the IDENTIFIER and the '='
            parserCounter += 2;

            // 4. Call the top of your expression engine to get the value
            Expression valueExpression = termOperate(); // Points directly to your math logic

            // 5. Expect and consume the trailing semicolon ';'
            if (tokens.get(parserCounter).type() == TokenType.SEMICOLON) {
                parserCounter++;

                // 6. Build your Assignment Statement and add it to your program list
                return new AssignmentStatement(varName, valueExpression, currentToken.lineNumber());
            } else {
                throw new RuntimeException("Syntax Error: Expected ';' after assignment on line " + currentToken.lineNumber());
            }
        }

        // FALLBACK: If it wasn't an assignment, then a standalone expression statement
        Expression expr = termOperate();
        if (tokens.get(parserCounter).type() == TokenType.SEMICOLON) {
            parserCounter++;
            return new ExpressionStatement(expr, currentToken.lineNumber());
        } else {
            throw new RuntimeException("Syntax Error: Expected ';' after expression on line " + currentToken.lineNumber());
        }
    }
}
