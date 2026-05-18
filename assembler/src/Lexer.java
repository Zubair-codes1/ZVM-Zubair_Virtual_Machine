public class Lexer {

    private static final Lexer INSTANCE = new Lexer();

    private Lexer() {}

    public static Lexer getInstance() {
        return INSTANCE;
    }

    public void tokenize(String input, int lineNumber) {

    }

}
