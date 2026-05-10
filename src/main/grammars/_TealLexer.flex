package com.galua.teal.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.galua.teal.psi.TealTypes.*;

%%

%{
  public _TealLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _TealLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

%state SHEBANG_STATE
%state BLOCK_COMMENT_STATE

EOL=\R
WHITE_SPACE=\s+

DIGIT=[0-9]
DECIMAL_INT={DIGIT}+
DECIMAL_FLOAT=({DIGIT}+\.{DIGIT}+|\.{DIGIT}+)
EXP=[eE][+-]?{DIGIT}+
NUMBER_LITERAL=({DECIMAL_FLOAT}|{DECIMAL_INT}){EXP}?

ESC=\\.
DQ_STRING=\"([^\"\\\r\n]|{ESC})*\"
SQ_STRING='([^'\\\r\n]|{ESC})*'
STRING_LITERAL=({DQ_STRING}|{SQ_STRING})

LINE_COMMENT=--[^\r\n]*
DOC_COMMENT=---[^\r\n]*

ID=[A-Za-z_][A-Za-z0-9_]*

%%
<YYINITIAL> {
  {WHITE_SPACE}           { return WHITE_SPACE; }

  "#!"                    { yybegin(SHEBANG_STATE); return SHEBANG; }
  "--[["                  { yybegin(BLOCK_COMMENT_STATE); return BLOCK_COMMENT; }
  {DOC_COMMENT}           { return DOC_COMMENT; }
  {LINE_COMMENT}          { return SHORT_COMMENT; }

  {STRING_LITERAL}        { return STRING; }
  {NUMBER_LITERAL}        { return NUMBER; }

  "and"                   { return AND; }
  "break"                 { return BREAK; }
  "do"                    { return DO; }
  "else"                  { return ELSE; }
  "elseif"                { return ELSEIF; }
  "end"                   { return END; }
  "false"                 { return FALSE; }
  "for"                   { return FOR; }
  "function"              { return FUNCTION; }
  "global"                { return GLOBAL; }
  "if"                    { return IF; }
  "in"                    { return IN; }
  "interface"             { return INTERFACE; }
  "local"                 { return LOCAL; }
  "nil"                   { return NIL; }
  "not"                   { return NOT; }
  "or"                    { return OR; }
  "repeat"                { return REPEAT; }
  "require"               { return REQUIRE; }
  "record"                { return RECORD; }
  "return"                { return RETURN; }
  "then"                  { return THEN; }
  "true"                  { return TRUE; }
  "type"                  { return TYPE; }
  "until"                 { return UNTIL; }
  "userdata"              { return USERDATA; }
  "where"                 { return WHERE; }
  "while"                 { return WHILE; }
  "enum"                  { return ENUM; }
  "as"                    { return AS; }
  "is"                    { return IS; }
  "metamethod"            { return METAMETHOD; }
  "REGION"                { return REGION; }
  "ENDREGION"             { return ENDREGION; }
  "..."                   { return ELLIPSIS; }
  ".."                    { return CONCAT; }
  "=="                    { return EQ; }
  ">="                    { return GE; }
  "<="                    { return LE; }
  "~="                    { return NE; }
  "-"                     { return MINUS; }
  "+"                     { return PLUS; }
  "*"                     { return MULT; }
  "%"                     { return MOD; }
  "/"                     { return DIV; }
  "="                     { return ASSIGN; }
  ">"                     { return GT; }
  "<"                     { return LT; }
  "("                     { return LPAREN; }
  ")"                     { return RPAREN; }
  "["                     { return LBRACK; }
  "]"                     { return RBRACK; }
  "{"                     { return LCURLY; }
  "}"                     { return RCURLY; }
  "?"                     { return QUESTION; }
  "#"                     { return GETN; }
  ","                     { return COMMA; }
  ";"                     { return SEMI; }
  ":"                     { return COLON; }
  "."                     { return DOT; }
  "^"                     { return EXP; }
  "::"                    { return DOUBLE_COLON; }
  "goto"                  { return GOTO; }
  "|"                     { return BIT_OR; }
  "&"                     { return BIT_AND; }
  "<<"                    { return BIT_LTLT; }
  ">>"                    { return BIT_RTRT; }
  "~"                     { return BIT_TILDE; }
  "//"                    { return DOUBLE_DIV; }

  {ID}                    { return ID; }

}

<SHEBANG_STATE> {
  [^\r\n]+                { yybegin(YYINITIAL); return SHEBANG_CONTENT; }
  {EOL}                   { yybegin(YYINITIAL); return WHITE_SPACE; }
}

<BLOCK_COMMENT_STATE> {
  "]]"                    { yybegin(YYINITIAL); return BLOCK_COMMENT; }
  [^]]+                   { return BLOCK_COMMENT; }
  "]"                     { return BLOCK_COMMENT; }
}

[^] { return BAD_CHARACTER; }
