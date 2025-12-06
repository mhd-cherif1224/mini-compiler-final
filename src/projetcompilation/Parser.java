package projetcompilation;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int i;
    private boolean erreur;

    // ========================
    // ENTRY POINT
    // ========================
    public void Z(List<Token> tokens) {
        this.tokens = tokens;
        this.i = 0;
        this.erreur = false;

        S();  // Start rule
        
        if (!erreur && check(TokenType.EOF) && i == tokens.size() - 1){

            System.out.println("chaine acceptee");
        } else {
            System.out.println("chaine NON acceptee");
        }
    }

    // ========================
    // TOKEN HELPERS
    // ========================
    

    // TOKEN HELPERS
    


    private void expect(TokenType type) {
    if (tokens.get(i).getType() == type) {
        tokens.get(i++);
    } else {
        error(type);
    }
}

    private void unexpected() {
    erreur = true;
    Token t = tokens.get(i);

    System.out.println(
        "Unexpected token: " + t.getType() +
        " at line " + t.getLine() +
        ", column " + t.getColumn()
    );
}


    private boolean check(TokenType t) {
        return tokens.get(i).getType() == t;
    }

    // ========================
    // START RULE
    // ========================
    public void S() {
        while (!erreur && !check(TokenType.EOF)) {
             Statement();
        }
        
    }

    // ========================
    // STATEMENT
    // ========================
    private void Statement() {

    if (check(TokenType.EOF)) return;
        
    if (isType( tokens.get(i).getType())) {
        Declaration();
        return;
    }

    if (check(TokenType.WHILE)) {
        While();
        return;
    }

    if (check(TokenType.BREAK)) {
        expect(TokenType.BREAK);
        expect(TokenType.SEMICOLON);
        return;
    }

    if (check(TokenType.CONTINUE)) {
        expect(TokenType.CONTINUE);
        expect(TokenType.SEMICOLON);
        return;
    }

    // x++;
    if (check(TokenType.IDENT) && tokens.get(i + 1).getType() == TokenType.INC) {
        Increment();
        return;
    }

    // x--;
    if (check(TokenType.IDENT) && tokens.get(i + 1).getType() == TokenType.DEC) {
        Decrement();
        return;
    }

    // x = ...
    if (check(TokenType.IDENT)) {
        Assignment();
        return;
    }


    if (check(TokenType.LBRACE)) {
        Block();
        return;
    }

    // NEW: skip return exactly like unknown
    if (check(TokenType.RETURN)) {
        skipUnknown();
        return;
    }

    // anything else
    skipUnknown();
}





    // ========================
    // TYPE CHECKER
    // ========================
    private boolean isType(TokenType t) {
        return t == TokenType.INT ||
               t == TokenType.FLOAT ||
               t == TokenType.CHAR;
    }

    // ========================
    // DECLARATION
    // ========================
    private void Declaration() {
    tokens.get(i++); // consume INT/FLOAT/CHAR

    if (!check(TokenType.IDENT)) {
        unexpected();
        return;
    }
    expect(TokenType.IDENT);

    if (check(TokenType.ASSIGN)) {
        expect(TokenType.ASSIGN);
        Expression();
    }

    while (check(TokenType.COMMA)) {
        expect(TokenType.COMMA);

        if (!check(TokenType.IDENT)) {
            unexpected();
            return;
        }

        expect(TokenType.IDENT);

        if (check(TokenType.ASSIGN)) {
            expect(TokenType.ASSIGN);
            Expression();
        }
    }

    expect(TokenType.SEMICOLON);
}

    private void Increment() {
    expect(TokenType.IDENT);
    expect(TokenType.INC);
    expect(TokenType.SEMICOLON);
}

    private void Decrement() {
        expect(TokenType.IDENT);
        expect(TokenType.DEC);
        expect(TokenType.SEMICOLON);
    }


    // ========================
    // ASSIGNMENT
    // ========================
    private void Assignment() {
    if (!check(TokenType.IDENT)) {
        unexpected();
        return;
    }
    expect(TokenType.IDENT);

    if (!check(TokenType.ASSIGN)) {
        unexpected();
        return;
    }
    expect(TokenType.ASSIGN);

    Expression();

    expect(TokenType.SEMICOLON);
}


    // ========================
    // WHILE LOOP
    // ========================
    private void  While() {
        expect(TokenType.WHILE);
        expect(TokenType.LPAREN);
         Expression();
        expect(TokenType.RPAREN);
         Statement(); // body
    }

    // ========================
    // BLOCK
    // ========================
    private void  Block() {
        expect(TokenType.LBRACE);

        while (!check(TokenType.RBRACE) && !check(TokenType.EOF) && !erreur) {
             Statement();
        }

        expect(TokenType.RBRACE);
    }

    // ========================
    // EXPRESSION
    // <expr> -> <term> ( ( + | - ) <term> )*
    // with optional comparison: <expr> comp <expr>
    // ========================
    private void Expression() {
    SimpleExpr();

    if (isComparison(tokens.get(i).getType())) {
        tokens.get(i++);
        SimpleExpr();
    }

    if (check(TokenType.SEMICOLON) ||
        check(TokenType.RPAREN) ||
        check(TokenType.COMMA) ||
        check(TokenType.RBRACE) ||
        check(TokenType.EOF)) {
        return; // normal exit
    }

    // If we reach here → illegal expression token
    // (example: x + / 5)
    if (!erreur)
        unexpected();
}


    private boolean isComparison(TokenType t) {
        return t == TokenType.LT || t == TokenType.GT ||
               t == TokenType.LE || t == TokenType.GE ||
               t == TokenType.EQ || t == TokenType.NEQ;
    }

    private void  SimpleExpr() {
         Term();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            tokens.get(i++);
             Term();
        }
    }

    private void  Term() {
         Factor();
        while (check(TokenType.STAR) || check(TokenType.SLASH)) {
            tokens.get(i++);
             Factor();
        }
    }

    private void Factor() {
    if (check(TokenType.NUMBER)) {
        tokens.get(i++);
        return;
    }
    if (check(TokenType.IDENT)) {
        tokens.get(i++);
        return;
    }
    if (check(TokenType.LPAREN)) {
        expect(TokenType.LPAREN);
        Expression();
        expect(TokenType.RPAREN);
        return;
    }

    // Nothing matched → unexpected()
    unexpected();
}


    // ========================
    // SKIP UNKNOWN INSTRUCTIONS
    // LIKE: if, for, switch, return...
    // ========================
    
    private boolean isKnownStatementStart(TokenType t) {
    return t == TokenType.INT ||
           t == TokenType.FLOAT ||
           t == TokenType.CHAR ||
           t == TokenType.WHILE ||
           t == TokenType.BREAK ||
           t == TokenType.CONTINUE ||
           t == TokenType.IDENT ||
           t == TokenType.LBRACE;
}

    
    
    private void skipUnknown() {

    tokens.get(i); // skip keyword, like IF/FOR/RETURN
    reportIrrelevant(tokens.get(i));       // print message
    i++;  
    
    

     

    // Special case: FOR EACH
    if (check(TokenType.IDENT)) {
        tokens.get(i++); // skip 'each' or other ident
    }

    if (check(TokenType.LPAREN))
        skipParentheses();

    if (check(TokenType.LBRACE))
        skipBraces();

    if (check(TokenType.SEMICOLON))
        tokens.get(i++);
}






    private void skipParentheses() {
        int depth = 0;
        expect(TokenType.LPAREN);
        depth++;

        while (depth > 0 && !check(TokenType.EOF)) {
            if (check(TokenType.LPAREN)) depth++;
            if (check(TokenType.RPAREN)) depth--;
            tokens.get(i++);
        }
    }

    private void skipBraces() {
        int depth = 0;
        expect(TokenType.LBRACE);
        depth++;

        while (depth > 0 && !check(TokenType.EOF)) {
            if (check(TokenType.LBRACE)) depth++;
            if (check(TokenType.RBRACE)) depth--;
            tokens.get(i++);
        }
    }
    
    private void error(TokenType expected) {
    erreur = true;

    Token t = tokens.get(i);
    System.out.println(
        "Syntax Error: expected " + expected +
        " but found " + t.getType() +
        " at line " + t.getLine() +
        ", column " + t.getColumn()
    );
    
    }
    private void reportIrrelevant(Token t) {
    System.out.println("Skipping irrelevant token: " +
        t.getType() +
        " at line " + t.getLine() +
        ", column " + t.getColumn());


}

    
    public static void main(String[] args) {

    // CHANGE THIS to your real path:
    String filePath = "C:\\Users\\USER\\Documents\\NetBeansProjects\\projetCompilation\\src\\projetcompilation\\input.txt";

    String code = "";
    
        
    
    try {
        code = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
        code = code + '#';
    } catch (Exception e) {
        System.out.println("Erreur de lecture du fichier : " + e.getMessage());
        return;
    }

    System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
    System.out.println("              MINI COMPILATEUR");
    System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
    System.out.println("\nCode lu depuis : " + filePath + "\n");

    // Now run lexer
    Lexer lex = new Lexer(code);
    List<Token> tokens = new ArrayList<>();

    Token t;
    do {
        t = lex.nextToken();
        tokens.add(t);
    } while (t.getType() != TokenType.EOF);

    // Print tokens
    for (Token tok : tokens) {
        System.out.println(tok);
    }

    // Parse
    Parser p = new Parser();
    p.Z(tokens);
}

}


