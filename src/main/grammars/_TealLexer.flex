package com.galua.teal.parser;

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

EOL=\R
WHITE_SPACE=\s+

ID=[A-Za-z_][A-Za-z0-9_]*

%%
<YYINITIAL> {
  {WHITE_SPACE}           { return WHITE_SPACE; }

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
  "record"                { return RECORD; }
  "return"                { return RETURN; }
  "then"                  { return THEN; }
  "true"                  { return TRUE; }
  "type"                  { return TYPE; }
  "until"                 { return UNTIL; }
  "while"                 { return WHILE; }
  "enum"                  { return ENUM; }
  "as"                    { return AS; }
  "is"                    { return IS; }
  "REGION"                { return REGION; }
  "ENDREGION"             { return ENDREGION; }
  "#!"                    { return SHEBANG; }
  "SHEBANG_CONTENT"       { return SHEBANG_CONTENT; }
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
  "SHORT_COMMENT"         { return SHORT_COMMENT; }
  "DOC_COMMENT"           { return DOC_COMMENT; }
  "BLOCK_COMMENT"         { return BLOCK_COMMENT; }
  "NUMBER"                { return NUMBER; }
  "STRING"                { return STRING; }
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

[^] { return BAD_CHARACTER; }
