package projetcompilation;

public class Lexer {

    private final String input;
    private int i = 0;
    private int line = 1;
    private int column = 1;

    public Lexer(String input) {
        this.input = input;
    }

    // ----------------------------------------------------
    // BASIC INDEX / CHARACTER HANDLING
    // ----------------------------------------------------

   
    private char current() {
        if (i >= input.length()) return '#';
        return input.charAt(i);
    }

    private char suivant(){
        if (i + 1 >= input.length()) return '#';
        return input.charAt(i + 1);
    }

    

    private void updatePosition(char c) {
        if (c == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
    }

    

    // ----------------------------------------------------
    // SKIP WHITESPACE AND COMMENTS
    // ----------------------------------------------------

    private void skipWhitespaceAndComments() {

        while (current() != '#') {
            
            char c = input.charAt(i);

            // whitespace
            if (Character.isWhitespace(c)) {
                updatePosition(c);
                i++;
                continue;
            }

            // single-line comment //
            if (c == '/' && suivant() == '/') {
                updatePosition(c); i++;
                updatePosition('/'); i++;
                while (current() != '#' && current() != '\n') {
                    updatePosition(current());
                    i++;
                }
                continue;
            }

            // block comment /* ... */
            if (c == '/' && suivant() == '*') {
                updatePosition(c); i++;
                updatePosition('*'); i++;

                while (current() != '#') {
                    if (current() == '*' && suivant() == '/') {
                        updatePosition('*'); i++;
                        updatePosition('/'); i++;
                        break;
                    }
                    updatePosition(current());
                    i++;
                }
                continue;
            }

            break;
        }
    }

    // ----------------------------------------------------
    // TOKENIZER
    // ----------------------------------------------------

    public Token nextToken() {

        skipWhitespaceAndComments();

        int startLine = line;
        int startColumn = column;
        char c = current();

        if ( c == '#')
            return new Token(TokenType.EOF, "EOF", line, column);

        
        if (c == '#')
    return new Token(TokenType.EOF, "#", startLine, startColumn);
        // ----------------------------------------------------
        // IDENTIFIERS + KEYWORDS
        // ----------------------------------------------------
        if (Character.isLetter(c) || c == '_') {
            Lexeme lex = new Lexeme(startLine, startColumn);

            lex.addChar(c);
            updatePosition(c);
            i++;

            while (current() != '#' &&
                (Character.isLetterOrDigit(current()) || current() == '_')) {

                c = input.charAt(i);
                lex.addChar(c);
                updatePosition(c);
                i++;
            }

            String word = lex.getText();

            if (word.equals("if")) {
                return new Token(TokenType.IF, word, startLine, startColumn);
            }
            else if (word.equals("else")) {
                return new Token(TokenType.ELSE, word, startLine, startColumn);
            }
            else if (word.equals("while")) {
                return new Token(TokenType.WHILE, word, startLine, startColumn);
            }
            else if (word.equals("do")) {
                return new Token(TokenType.DO, word, startLine, startColumn);
            }
            else if (word.equals("for")) {
                return new Token(TokenType.FOR, word, startLine, startColumn);
            }
            else if (word.equals("foreach")) {
                return new Token(TokenType.FOREACH, word, startLine, startColumn);
            }
            else if (word.equals("in")) {
                return new Token(TokenType.IN, word, startLine, startColumn);
            }
            else if (word.equals("switch")) {
                return new Token(TokenType.SWITCH, word, startLine, startColumn);
            }
            else if (word.equals("case")) {
                return new Token(TokenType.CASE, word, startLine, startColumn);
            }
            else if (word.equals("default")) {
                return new Token(TokenType.DEFAULT, word, startLine, startColumn);
            }
            else if (word.equals("break")) {
                return new Token(TokenType.BREAK, word, startLine, startColumn);
            }
            else if (word.equals("continue")) {
                return new Token(TokenType.CONTINUE, word, startLine, startColumn);
            }
            else if (word.equals("int")) {
                return new Token(TokenType.INT, word, startLine, startColumn);
            }
            else if (word.equals("float")) {
                return new Token(TokenType.FLOAT, word, startLine, startColumn);
            }
            else if (word.equals("char")) {
                return new Token(TokenType.CHAR, word, startLine, startColumn);
            }
            else if (word.equals("bool")) {
                return new Token(TokenType.BOOL, word, startLine, startColumn);
            }
            else if (word.equals("return")) {
                return new Token(TokenType.RETURN, word, startLine, startColumn);
            }


            return new Token(TokenType.IDENT, word, startLine, startColumn);
        }

        // ----------------------------------------------------
        // NUMBERS
        // ----------------------------------------------------
        if (Character.isDigit(c)) {

            Lexeme lex = new Lexeme(startLine, startColumn);

            while (current() != '#' && Character.isDigit(current())) {
                input.charAt(i);
                lex.addChar(c);
                updatePosition(c);
                i++;
            }

            if (current() == '.' && Character.isDigit(suivant())) {
                char dot = current();
                lex.addChar(dot);
                updatePosition(dot);
                i++;

                while (current() != '#' && Character.isDigit(current())) {
                    input.charAt(i);
                    lex.addChar(c);
                    updatePosition(c);
                    i++;
                }
            }

            return new Token(TokenType.NUMBER, lex.getText(), startLine, startColumn);
        }

        // ----------------------------------------------------
        // STRING LITERAL
        // ----------------------------------------------------
        if (c == '"') {
            updatePosition(c);
            i++;

            Lexeme lex = new Lexeme(startLine, startColumn);

            while (current() != '#' && current() != '"') {
                char ch = input.charAt(i);
                lex.addChar(ch);
                updatePosition(ch);
                i++;
            }

            if (current() == '"') {
                updatePosition('"');
                i++;
            }

            return new Token(TokenType.STRING_LITERAL, lex.getText(), startLine, startColumn);
        }

        // ----------------------------------------------------
        // CHAR LITERAL
        // ----------------------------------------------------
        if (c == '\'') {
            updatePosition(c);
            i++;

            Lexeme lex = new Lexeme(startLine, startColumn);

            if (current() != '#' && current() != '\'') {
                char ch = input.charAt(i);
                lex.addChar(ch);
                updatePosition(ch);
                i++;
            }

            if (current() == '\'') {
                updatePosition('\'');
                i++;
            }

            return new Token(TokenType.CHAR_LITERAL, lex.getText(), startLine, startColumn);
        }

        // ----------------------------------------------------
        // TWO-CHAR OPERATORS
        // ----------------------------------------------------

        if (c == '=' && suivant() == '=') {
            updatePosition('='); i++;
            updatePosition('='); i++;
            return new Token(TokenType.EQ, "==", startLine, startColumn);
        }
        if (c == '!' && suivant() == '=') {
            updatePosition('!'); i++;
            updatePosition('='); i++;
            return new Token(TokenType.NEQ, "!=", startLine, startColumn);
        }
        if (c == '<' && suivant() == '=') {
            updatePosition('<'); i++;
            updatePosition('='); i++;
            return new Token(TokenType.LE, "<=", startLine, startColumn);
        }
        if (c == '>' && suivant() == '=') {
            updatePosition('>'); i++;
            updatePosition('='); i++;
            return new Token(TokenType.GE, ">=", startLine, startColumn);
        }
        if (c == '&' && suivant() == '&') {
            updatePosition('&'); i++;
            updatePosition('&'); i++;
            return new Token(TokenType.AND, "&&", startLine, startColumn);
        }
        if (c == '|' && suivant() == '|') {
            updatePosition('|'); i++;
            updatePosition('|'); i++;
            return new Token(TokenType.OR, "||", startLine, startColumn);
        }
        if (c == '+' && suivant() == '+') {
            updatePosition('+'); i++;
            updatePosition('+'); i++;
            return new Token(TokenType.INC, "++", startLine, startColumn);
        }
        if (c == '-' && suivant() == '-') {
            updatePosition('-'); i++;
            updatePosition('-'); i++;
            return new Token(TokenType.DEC, "--", startLine, startColumn);
        }
        if (c == '<' && suivant() == '<') {
            updatePosition('<'); i++;
            updatePosition('<'); i++;
            return new Token(TokenType.SHIFT_LEFT, "<<", startLine, startColumn);
        }
        if (c == '>' && suivant() == '>') {
            updatePosition('>'); i++;
            updatePosition('>'); i++;
            return new Token(TokenType.SHIFT_RIGHT, ">>", startLine, startColumn);
        }

        // assignment ops
        if (c == '+' && suivant() == '=') {
            updatePosition('+'); i++;
            updatePosition('='); i++;
            return new Token(TokenType.PLUS_ASSIGN, "+=", startLine, startColumn);
        }
        if (c == '-' && suivant() == '=') {
            updatePosition('-'); i++;
            updatePosition('='); i++;
            return new Token(TokenType.MINUS_ASSIGN, "-=", startLine, startColumn);
        }
        if (c == '*' && suivant() == '=') {
            updatePosition('*'); i++;
            updatePosition('='); i++;
            return new Token(TokenType.STAR_ASSIGN, "*=", startLine, startColumn);
        }
        if (c == '/' && suivant() == '=') {
            updatePosition('/'); i++;
            updatePosition('='); i++;
            return new Token(TokenType.SLASH_ASSIGN, "/=", startLine, startColumn);
        }

        // ----------------------------------------------------
        // SINGLE-CHAR TOKENS
        // ----------------------------------------------------

        // Single-character tokens (converted from switch → if/else)

        if (c == '+') {
            updatePosition(c); i++;
            return new Token(TokenType.PLUS, "+", startLine, startColumn);
        }
        else if (c == '-') {
            updatePosition(c); i++;
            return new Token(TokenType.MINUS, "-", startLine, startColumn);
        }
        else if (c == '*') {
            updatePosition(c); i++;
            return new Token(TokenType.STAR, "*", startLine, startColumn);
        }
        else if (c == '/') {
            updatePosition(c); i++;
            return new Token(TokenType.SLASH, "/", startLine, startColumn);
        }
        else if (c == '%') {
            updatePosition(c); i++;
            return new Token(TokenType.MOD, "%", startLine, startColumn);
        }
        else if (c == '=') {
            updatePosition(c); i++;
            return new Token(TokenType.ASSIGN, "=", startLine, startColumn);
        }
        else if (c == '<') {
            updatePosition(c); i++;
            return new Token(TokenType.LT, "<", startLine, startColumn);
        }
        else if (c == '>') {
            updatePosition(c); i++;
            return new Token(TokenType.GT, ">", startLine, startColumn);
        }
        else if (c == '!') {
            updatePosition(c); i++;
            return new Token(TokenType.NOT, "!", startLine, startColumn);
        }
        else if (c == '&') {
            updatePosition(c); i++;
            return new Token(TokenType.BIT_AND, "&", startLine, startColumn);
        }
        else if (c == '|') {
            updatePosition(c); i++;
            return new Token(TokenType.BIT_OR, "|", startLine, startColumn);
        }
        else if (c == '^') {
            updatePosition(c); i++;
            return new Token(TokenType.BIT_XOR, "^", startLine, startColumn);
        }
        else if (c == '~') {
            updatePosition(c); i++;
            return new Token(TokenType.BIT_NOT, "~", startLine, startColumn);
        }

        // Brackets
        else if (c == '(') {
            updatePosition(c); i++;
            return new Token(TokenType.LPAREN, "(", startLine, startColumn);
        }
        else if (c == ')') {
            updatePosition(c); i++;
            return new Token(TokenType.RPAREN, ")", startLine, startColumn);
        }
        else if (c == '{') {
            updatePosition(c); i++;
            return new Token(TokenType.LBRACE, "{", startLine, startColumn);
        }
        else if (c == '}') {
            updatePosition(c); i++;
            return new Token(TokenType.RBRACE, "}", startLine, startColumn);
        }
        else if (c == '[') {
            updatePosition(c); i++;
            return new Token(TokenType.LBRACKET, "[", startLine, startColumn);
        }
        else if (c == ']') {
            updatePosition(c); i++;
            return new Token(TokenType.RBRACKET, "]", startLine, startColumn);
        }

        // Punctuation
        else if (c == ';') {
            updatePosition(c); i++;
            return new Token(TokenType.SEMICOLON, ";", startLine, startColumn);
        }
        else if (c == ',') {
            updatePosition(c); i++;
            return new Token(TokenType.COMMA, ",", startLine, startColumn);
        }
        else if (c == '.') {
            updatePosition(c); i++;
            return new Token(TokenType.DOT, ".", startLine, startColumn);
        }
        else if (c == ':') {
            updatePosition(c); i++;
            return new Token(TokenType.COLON, ":", startLine, startColumn);
        }
        else if (c == '?') {
            updatePosition(c); i++;
            return new Token(TokenType.QUESTION, "?", startLine, startColumn);
        }

        // ----------------------------------------------------
        // ERROR TOKEN
        // ----------------------------------------------------

        char bad = c;
        updatePosition(c);
        i++;
        return new Token(TokenType.ERROR, String.valueOf(bad), startLine, startColumn);
    }
}
