/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetcompilation;

/**
 *
 * @author BAKI
 */
public class Lexeme {
     private StringBuilder buffer;
    private int line;
    private int column;

    public Lexeme(int line, int column) {
        this.buffer = new StringBuilder();
        this.line = line;
        this.column = column;
    }
    
    
    

    // Add one character to the lexeme
    public void addChar(char c) {
        buffer.append(c);
    }

    // Return the assembled lexeme string
    public String getText() {
        return buffer.toString();
    }

    // The starting line of the lexeme
    public int getLine() {
        return line;
    }
//
    // The starting column of the lexeme
    public int getColumn() {
        return column;
    }
//
    // Reset the lexeme (if reused)
    public void reset(int newLine, int newColumn) {
        buffer.setLength(0);
        this.line = newLine;
        this.column = newColumn;
    }
//
    @Override
    public String toString() {
        return getText();
    }
    

}
