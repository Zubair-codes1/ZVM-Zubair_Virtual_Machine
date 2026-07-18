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
            return statements;
        }

        while (!isAtEnd()) {
            Statement statement = parseStatement();
            statements.add(statement);
        }

        return statements;
    }

    private boolean isAtEnd() {
        if (parserCounter >= tokens.size()) { return true; }
        return tokens.get(parserCounter).type() == TokenType.EOF;
    }

    private boolean match(List<TokenType> tokenTypes) {
        for (TokenType tokenType : tokenTypes) {
            if (peekToken().type() == tokenType) { return true; }
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

    private Statement parseStatement() {
        Statement statement;
        if (isDeclaration()) {
            if (tokens.get(parserCounter).type() == TokenType.DEF) {
                statement = handleFuncDeclaration();
            } else {
                statement = handleVarDeclaration();
            }
        }else if (isStatement()) {

            switch (tokens.get(parserCounter).type()) {
                case IF -> statement = handleIfStatement();
                case WHILE -> statement = handleWhileStatement();
                case FOR -> statement = handleForStatement();
                case RETURN -> statement = handleReturnStatement();
                case BREAK -> statement = handleBreakStatement();
                case PRINT -> statement = handlePrintStatement();
                case LEFT_BRACE -> statement = handleLeftBraceStatement();
                default -> statement = handleOtherStatements();
            }

        } else {
            statement = handleOtherStatements();
        }

        return statement;
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
            if (peekNextToken().type() != TokenType.LEFT_PAREN) {
                parserCounter++;
                return new VariableExpression(token.tokenValue(), token.lineNumber());
            }

            return handleFuncCall();
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

    private CallingExpression handleFuncCall() {
        Token functionName = advance(); // Consumes the identifier

        consume(TokenType.LEFT_PAREN, "Syntax error: Missing '(' at the start of function call.");
        List<Expression> arguments = new ArrayList<>();

        while (peekToken().type() != TokenType.RIGHT_PAREN && !isAtEnd()) {
            Expression argument = expression();
            arguments.add(argument);

            // Handle commas between multiple arguments
            if (peekToken().type() == TokenType.COMMA) {
                advance(); // Consume the comma
                if (peekToken().type() == TokenType.RIGHT_PAREN) {
                    throw new CompilerException("Syntax Error: Trailing comma in argument list.");
                }
            } else if (peekToken().type() != TokenType.RIGHT_PAREN) {
                throw new CompilerException("Syntax Error: Expected ',' or ')' after argument.");
            }
        }

        consume(TokenType.RIGHT_PAREN, "Syntax error: Missing ')' at the end of argument list.");

        return new CallingExpression(functionName.tokenValue(), arguments, functionName.lineNumber());
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

    private Statement handleFuncDeclaration() {
        Token funcDeclaration = advance(); // Consumes 'def'
        Token returnType = advance();

        if (
                returnType.type() != TokenType.INT_TYPE &&
                        returnType.type() != TokenType.STRING_TYPE &&
                        returnType.type() != TokenType.BOOLEAN_TYPE
        ) {
            throw new CompilerException("Syntax Error: Invalid function return type.");
        }

        // Enforce that the name must be an identifier
        Token name = consume(TokenType.IDENTIFIER, "Syntax Error: Missing function name.");

        consume(TokenType.LEFT_PAREN, "Syntax Error: Missing '(' after function name.");
        List<Parameter> parameters = new ArrayList<>();

        while (peekToken().type() != TokenType.RIGHT_PAREN) {
            Token paramReturnType = null;
            if (isDeclaration() && peekToken().type() != TokenType.DEF) {
                paramReturnType = advance();
            } else {
                throw new CompilerException("Syntax Error: Missing parameter type.");
            }

            Token paramName = consume(TokenType.IDENTIFIER, "Syntax Error: Missing parameter name.");
            parameters.add(new Parameter(paramReturnType.tokenValue(), paramName.tokenValue()));

            // Handle commas between multiple parameters
            if (peekToken().type() == TokenType.COMMA) {
                advance(); // Consume the comma
                if (peekToken().type() == TokenType.RIGHT_PAREN) {
                    throw new CompilerException("Syntax Error: Trailing comma in parameter list.");
                }
            } else if (peekToken().type() != TokenType.RIGHT_PAREN) {
                throw new CompilerException("Syntax Error: Expected ',' or ')' after parameter.");
            }
        }

        consume(TokenType.RIGHT_PAREN, "Syntax Error: Missing ')' after parameter list.");

        BlockStatement functionBody;
        if (peekToken().type() == TokenType.LEFT_BRACE) {
            functionBody = (BlockStatement) parseStatement();
        } else {
            throw new CompilerException("Syntax Error: Missing function body block '{'.");
        }

        // Note: Assumes FunctionDeclarationStatement has been adjusted to accept BlockStatement
        return new FunctionDeclarationStatement(
                returnType.tokenValue(),
                name.tokenValue(),
                parameters,
                functionBody,
                funcDeclaration.lineNumber()
        );
    }

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

    private Statement handleIfStatement() {
        Token ifToken = advance();
        consume(TokenType.LEFT_PAREN, "Syntax Error: Missing '(' at start of if statement.");

        // parses the expression
        Expression condition = expression();
        consume(TokenType.RIGHT_PAREN, "Syntax Error: Expected ')' at end of if statement.");

        Statement ifStatements = parseStatement();

        Statement elseStatements = null;
        if (peekToken().type() == TokenType.ELSE) {
            Token elseToken = advance();

            elseStatements = parseStatement();
        }

        return new IfStatement(condition, ifStatements, elseStatements,  ifToken.lineNumber());
    }

    /**
     * Handler for while statements
     * @return while statement
     */
    private Statement handleWhileStatement() {
        Token whileToken = advance();
        consume(TokenType.LEFT_PAREN, "Syntax Error: Missing '(' at start of while statement condition.");

        Expression condition = expression();
        consume(TokenType.RIGHT_PAREN, "Syntax Error: Expected ')' at end of while statement condition.");

        Statement whileStatements = parseStatement();

        return new WhileStatement(condition, whileStatements, whileToken.lineNumber());
    }

    private Statement handleForStatement() {
        Token forToken = advance();
        consume(TokenType.LEFT_PAREN, "Syntax Error: Missing '(' at start of for statement condition.");

        Statement initialiser = null;
        if (peekToken().type() == TokenType.SEMICOLON) {
            advance();
        } else if (isDeclaration()) {
            initialiser = handleVarDeclaration();
        } else {
            initialiser = handleOtherStatements();
        }

        Expression condition = null;
        if (peekToken().type() != TokenType.SEMICOLON) {
            condition = expression();
        }
        consume(TokenType.SEMICOLON, "Syntax Error: Expected ';' at end of for statement condition.");

        Statement incrementStatement = null;
        if (peekToken().type() != TokenType.RIGHT_PAREN) {
            Expression increment = expression();
            incrementStatement = new ExpressionStatement(increment, increment.getLineNumber());
        }

        consume(TokenType.RIGHT_PAREN, "Syntax Error: Expected ')' at end of for increment statement.");

        Statement forBlockStatements = parseStatement();

        return new ForStatement(initialiser, condition,  incrementStatement, forBlockStatements, forToken.lineNumber());

    }

    private Statement handleReturnStatement() {
        Token returnToken = advance();

        Expression returnValue = null;
        if (peekToken().type() != TokenType.SEMICOLON) {
            returnValue = expression();
        }
        consume(TokenType.SEMICOLON, "Syntax Error: Expected ';' at end of return statement.");

        return new ReturnStatement(returnValue, returnToken.lineNumber());
    }

    private Statement handleBreakStatement() {
        Token breakToken = advance();

        consume(TokenType.SEMICOLON, "Syntax Error: Expected ';' at end of break statement.");

        return new BreakStatement(breakToken.lineNumber());
    }

    private Statement handlePrintStatement() {
        Token printToken = advance();

        consume(TokenType.LEFT_PAREN, "Syntax Error: Missing '(' at start of print statement.");
        Expression expression = expression();
        consume(TokenType.RIGHT_PAREN, "Syntax Error: Expected ')' at end of print statement.");
        consume(TokenType.SEMICOLON, "Syntax Error: Expected ';' at end of print statement.");

        return new PrintStatement(expression, printToken.lineNumber());
    }

    /**
     * Handles everything between braces pair { ... }
     * @return a block statement which contains all the statements within the block
     */
    private Statement handleLeftBraceStatement() {
        Token leftBraceToken = advance();
        List<Statement> blockStatements = new ArrayList<>();

        while (peekToken().type() != TokenType.RIGHT_BRACE && !isAtEnd()) {
            Statement statement = parseStatement();
            blockStatements.add(statement);
        }

        Token rightBraceToken = consume(TokenType.RIGHT_BRACE, "Syntax Error: Missing '}' at end of block statement.");

        return new BlockStatement(blockStatements, rightBraceToken.lineNumber());
    }

    private Statement handleOtherStatements() {
        // Check for assignment
        if (peekToken().type() == TokenType.IDENTIFIER && peekNextToken().type() == TokenType.ASSIGNMENT) {
            Token nameToken = advance(); // Consume the identifier
            String  name = nameToken.tokenValue();
            advance(); // Consume the '=' operator

            Expression value = expression(); // Parse the RHS value
            consume(TokenType.SEMICOLON, "Syntax Error: Expected ';' after assignment.");

            return new AssignmentStatement(name, value, nameToken.lineNumber());
        }

        // Otherwise, treat it as a standard expression statement
        Expression expr = expression();
        consume(TokenType.SEMICOLON, "Syntax Error: Expected ';' after expression.");

        return new ExpressionStatement(expr, expr.getLineNumber());
    }
}
