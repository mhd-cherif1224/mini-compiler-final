/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetcompilation;
// TestLexer.java

public class TestLexer {
    public static void main(String[] args) {
        String program = ""
            + "int i = 0;\n"
            + "while (i < 10 && i != 5) {\n"
            + "    i = i + 1;\n"
            + "    // commentaire\n"
            + "    if (i % 2 == 0) i++;\n"
            + "}\n";

        Lexer lexer = new Lexer(program);
        Token t;
        do {
            t = lexer.nextToken();
            System.out.println(t);
        } while (t.getType() != TokenType.EOF);
    }
}
