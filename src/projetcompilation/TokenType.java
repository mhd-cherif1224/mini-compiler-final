/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetcompilation;


public enum TokenType {

    // ----- Keywords: Control Structures -----
    IF,
    ELSE,
    WHILE,
    DO,
    FOR,
    FOREACH,
    IN,            // for foreach (foreach (x IN collection))
    SWITCH,
    CASE,
    DEFAULT,
    BREAK,
    CONTINUE,
    RETURN,

    // ----- Keywords: Types -----
    INT,
    FLOAT,
    CHAR,
    BOOL,
    STRING,
    VOID,

    TRUE,
    FALSE,

    // ----- Identifiers & Literals -----
    IDENT,              // variable, function names
    NUMBER,             // integer/float literals
    CHAR_LITERAL,        // 'a'
    STRING_LITERAL,      // "hello"

    // ----- Arithmetic Operators -----
    PLUS,               // +
    MINUS,              // -
    STAR,               // *
    SLASH,              // /
    MOD,                // %

    // ----- Unary Operators -----
    NOT,                // !
    INC,                // ++
    DEC,                // --

    // ----- Assignment Operators -----
    ASSIGN,             // =
    PLUS_ASSIGN,        // +=
    MINUS_ASSIGN,       // -=
    STAR_ASSIGN,        // *=
    SLASH_ASSIGN,       // /=

    // ----- Comparison Operators -----
    EQ,                 // ==
    NEQ,                // !=
    LT,                 // <
    GT,                 // >
    LE,                 // <=
    GE,                 // >=

    // ----- Logical Operators -----
    AND,                // &&
    OR,                 // ||

    // ----- Bitwise Operators -----
    BIT_AND,            // &
    BIT_OR,             // |
    BIT_XOR,            // ^
    BIT_NOT,            // ~
    SHIFT_LEFT,         // <<
    SHIFT_RIGHT,        // >>

    // ----- Switch/Case symbols -----
    COLON,              // :
    QUESTION,           // ?

    // ----- Other Symbols -----
    LPAREN,             // (
    RPAREN,             // )
    LBRACE,             // {
    RBRACE,             // }
    LBRACKET,           // [
    RBRACKET,           // ]
    COMMA,              // ,
    SEMICOLON,          // ;
    DOT,                // .

    // ----- End & Error -----
    EOF,
    ERROR
}
