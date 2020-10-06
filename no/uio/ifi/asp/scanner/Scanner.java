package no.uio.ifi.asp.scanner;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import no.uio.ifi.asp.main.*;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
        curFileName = fileName;
        indents.push(0);

        try {
            sourceFile = new LineNumberReader(
                    new InputStreamReader(
                            new FileInputStream(fileName),
                            "UTF-8"));
        } catch (IOException e) {
            scannerError("Cannot read " + fileName + "!");
        }
    }


    private void scannerError(String message) {
        String m = "Asp scanner error";
        if (curLineNum() > 0)
            m += " on line " + curLineNum();
        m += ": " + message;

        Main.error(m);
    }


    public Token curToken() {
        while (curLineTokens.isEmpty()) {
            readNextLine();
        }
        return curLineTokens.get(0);
    }


    public void readNextToken() {
        if (!curLineTokens.isEmpty())
            curLineTokens.remove(0);
    }


    private void readNextLine() {
        curLineTokens.clear();

        // Read the next line:
        String line = null;
        try {
            line = sourceFile.readLine();
            if (line == null) {
                for(int indent : indents){
                    if(indent>0){
                        Token t = new Token(dedentToken);
                        curLineTokens.add(t);
                        Main.log.noteToken(t);
                    }
                }
                Token t = new Token(eofToken);
                curLineTokens.add(t);
                Main.log.noteToken(t);
                sourceFile.close();
                sourceFile = null;
                return;
            } else {
                Main.log.noteSourceLine(curLineNum(), line);
            }
        } catch (IOException e) {
            sourceFile = null;
            scannerError("Unspecified I/O error!");
        }

        int indentCount = findIndentCount(line);

        //For blank and commented lines
        if(indentCount == -1){
            return;
        }

        makeTokens(line);

        for (Token tok : curLineTokens) {
            Main.log.noteToken(tok);
        }
    }

    private void makeTokens(String line){
        int pos = 0;
        Token t = null;
        StringBuilder sb = new StringBuilder();

        //Dividing line into tokens
        while(pos <= line.length()) {
            char c = ' ';
            if (pos != line.length()){
                c = line.charAt(pos++);
            } else{
                pos++;
            }
            if(c == '#'){
                break;
            }
            //Tokens with known length, and indicating that we have started a token with an unknown length
            else if (t == null){
                if(c == '"' || c == '\''){
                    t = new Token(stringToken, curLineNum());
                    sb.append(c);
                } else if(isDigit(c)){
                    t = new Token(integerToken, curLineNum());
                    sb.append(c);
                } else if(isLetterAZ(c) || c == '_'){
                    t = new Token(nameToken, curLineNum());
                    sb.append(c);
                } else if (c == '='){
                    pos = extendedToken(line, pos, '=', doubleEqualToken, equalToken);
                } else if (c == '/'){
                    pos = extendedToken(line, pos, '/', doubleSlashToken, slashToken);
                } else if (c == '>'){
                    pos = extendedToken(line, pos, '=', greaterEqualToken, greaterToken);
                } else if (c == '<'){
                    pos = extendedToken(line, pos, '=', lessEqualToken, lessToken);
                } else if (c == '!' && pos != line.length() && line.charAt(pos) == '='){
                    curLineTokens.add(new Token(notEqualToken, curLineNum()));
                    pos++;
                } else if (!Character.isWhitespace(c)) {
                    final String streng = Character.toString(c);
                    TokenKind tokenKind = EnumSet.range(astToken, semicolonToken)
                            .stream()
                            .filter(tk -> tk.image.equals(streng))
                            .findAny()
                            .orElse(null);
                    if (tokenKind != null){
                        curLineTokens.add(new Token(tokenKind, curLineNum()));
                    } else {
                        scannerError("Illegal symbol '" + c + "'");
                    }
                }
            }
            //Keywords, name, strings and number tokens
            else if(t != null){
                if (t.kind == stringToken){
                    if(c != sb.toString().charAt(0)){
                        sb.append(c);
                    } else{
                        addStringToken(sb, t);
                        t = null;
                    }
                }
                else if (t.kind == integerToken){
                    if (isDigit(c)){
                        sb.append(c);
                    } else if (c == '.'){
                        t.kind = floatToken;
                        sb.append(c);
                    } else{
                        addIntToken(sb, t);
                        t = null;
                        pos = endNameNumToken(pos, line.length());
                    }
                }
                else if (t.kind == floatToken){
                    if (isDigit(c)){
                        sb.append(c);
                    } else if (c == '.'){
                        scannerError("Illegal symbol '.'");
                    } else{
                        addFloatToken(sb, t);
                        t = null;
                        pos = endNameNumToken(pos, line.length());
                    }
                }
                else if (t.kind == nameToken){
                    if (isLetterAZ(c) || isDigit(c) || c == '_'){
                        sb.append(c);
                    } else {
                        addNameToken(sb, t);
                        t = null;
                        pos = endNameNumToken(pos, line.length());
                    }
                }
            }
        }

        if(t!= null && t.kind == stringToken){
            scannerError("EOL while scanning string literal");
        }

        // Terminate line:
        curLineTokens.add(new Token(newLineToken, curLineNum()));
    }

    //For checking tokens with either one or two symbols, e.g. ==
    private int extendedToken(String line, int pos, char c, TokenKind extended, TokenKind single){
        Token t;
        if(pos != line.length() && line.charAt(pos) == c){
            t = new Token(extended, curLineNum());
            pos++;
        } else {
            t = new Token(single, curLineNum());
        }
        curLineTokens.add(t);
        return pos;
    }

    private void addNameToken(StringBuilder sb, Token t){
        t.name = sb.toString();
        t.checkResWords();
        addToken(sb, t);
    }

    private void addFloatToken(StringBuilder sb, Token t){
        if(sb.toString().charAt(sb.length()-1) == '.'){
            scannerError("Illegal symbol '.'");
        }
        t.floatLit = Double.parseDouble(sb.toString());
        addToken(sb, t);
    }

    private void addIntToken(StringBuilder sb, Token t){
        if(sb.toString().charAt(0) == '0' && sb.length() > 1){
            Token zero = new Token(integerToken, curLineNum());
            zero.integerLit = 0;
            curLineTokens.add(zero);
        }
        t.integerLit = Integer.parseInt(sb.toString());
        addToken(sb, t);
    }

    private void addStringToken(StringBuilder sb, Token t){
        t.stringLit = sb.toString().substring(1);
        addToken(sb, t);
    }

    private void addToken(StringBuilder sb, Token t){
        sb.delete(0, sb.length());
        curLineTokens.add(t);
    }

    //Reducing position count to not skip characters
    private int endNameNumToken(int pos, int length){
        if (length >= pos){
            pos--;
        }
        return pos;
    }

    public int curLineNum() {
        return sourceFile != null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndentCount(String line){
        int n = findIndent(expandLeadingTabs(line));

        if(line.trim().isEmpty() || line.startsWith("#")){
            return -1;
        }

        if(n > indents.peek()){
            indents.push(n);
            curLineTokens.add(new Token(indentToken, curLineNum()));
        }
        while (n < indents.peek()) {
            indents.pop();
            curLineTokens.add(new Token(dedentToken, curLineNum()));
        }

        if(n != indents.peek()){
            scannerError("Indentation error");
        }

        return n;
    }

    private int findIndent(String s) {
        int indent = 0;

        while (indent < s.length() && s.charAt(indent) == ' ') indent++;
        return indent;
    }

    private String expandLeadingTabs(String s) {
        String newS = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\t') {
                do {
                    newS += " ";
                } while (newS.length() % TABDIST > 0);
            } else if (c == ' ') {
                newS += " ";
            } else {
                newS += s.substring(i);
                break;
            }
        }
        return newS;
    }

    private boolean isLetterAZ(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || (c == '_');
    }


    private boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }


    public boolean isCompOpr() {
        TokenKind k = curToken().kind;
        EnumSet compOpr = EnumSet.of(
                lessToken,
                greaterToken,
                doubleEqualToken,
                lessEqualToken,
                greaterEqualToken,
                notEqualToken
        );
        return compOpr.contains(k);
    }


    public boolean isFactorPrefix() {
        TokenKind k = curToken().kind;
        EnumSet factorPrefix = EnumSet.of(
                plusToken,
                minusToken
        );
        return factorPrefix.contains(k);
    }


    public boolean isFactorOpr() {
        TokenKind k = curToken().kind;
        EnumSet factorPrefix = EnumSet.of(
                astToken,
                slashToken,
                percentToken,
                doubleSlashToken
        );
        return factorPrefix.contains(k);
    }


    public boolean isTermOpr() {
        TokenKind k = curToken().kind;
        EnumSet termOpr = EnumSet.of(
                plusToken,
                minusToken
        );
        return termOpr.contains(k);
    }


    public boolean anyEqualToken() {
        for (Token t : curLineTokens) {
            if (t.kind == equalToken) return true;
            if (t.kind == semicolonToken) return false;
        }
        return false;
    }
}
