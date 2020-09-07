package no.uio.ifi.asp.scanner;

import java.io.*;
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
                sourceFile.close();
                sourceFile = null;
                curLineTokens.add(new Token(eofToken, curLineNum()));
                return;
            } else {
                Main.log.noteSourceLine(curLineNum(), line);
            }
        } catch (IOException e) {
            sourceFile = null;
            scannerError("Unspecified I/O error!");
        }
        System.out.println(line);

        //Algorithm for making indent tokens
        int n = findIndent(expandLeadingTabs(line));

        if(line.trim().isEmpty() || line.startsWith("#")){
            return;
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

        int pos = n;

        //Dividing line into tokens
        while(pos < line.length()) {
            char c = line.charAt(pos);
            if(Character.isWhitespace(c)) {
            } else if(c == '#'){
                break;
            }
            else if(c == '"'){
                String temp = Character.toString(c);
                while ( c != '"'){
                    c = line.charAt(pos++);
                    temp += c;
                }
                Token t = new Token(stringToken, curLineNum());
                t.stringLit = temp;
                curLineTokens.add(t);
            }
            else if (isDigit(c)){
                Token t = null;
                String temp = "";
                if(c == '0'){
                    char nextChar = line.charAt(pos+1);
                    if(nextChar != '.' && !Character.isWhitespace(nextChar)){
                        scannerError("Cannot read " + curFileName + "!");
                    }
                }
                while (!Character.isWhitespace(c)){
                    temp += c;
                    if(c == '.'){
                        t = new Token(floatToken, curLineNum());
                    }
                    c = line.charAt(pos++);
                }
                if(t == null){
                    t = new Token(integerToken, curLineNum());
                    t.integerLit = Integer.parseInt(temp);
                }
                else{
                    t.floatLit = Float.parseFloat(temp);
                }
                curLineTokens.add(t);
            } else if (isLetterAZ(c) || c == '_'){
                String temp = "";
                while(isLetterAZ(c) || isDigit(c) || c == '_'){
                    temp += c;
                    c = line.charAt(++pos);
                }
                //TODO funker ikke
                ArrayList<TokenKind> keywords = new ArrayList<>(Arrays.asList(
                        andToken,
                        defToken,
                        elifToken,
                        elseToken,
                        falseToken,
                        forToken,
                        ifToken,
                        inToken,
                        noneToken,
                        notToken,
                        orToken,
                        passToken,
                        returnToken,
                        trueToken,
                        whileToken,
                        yieldToken));
                final String streng = temp;
                TokenKind tokenKind = keywords
                        .stream()
                        .filter(tk -> tk.image.equals(streng))
                        .findAny()
                        .orElse(nameToken);
                Token t = new Token(tokenKind, curLineNum());
                if(tokenKind == nameToken){
                    t.name = streng;
                }
                /*
                Token t = null;
                if(temp == "and"){
                    t = new Token(andToken, curLineNum());
                } else if(temp == "def"){
                    t = new Token(defToken, curLineNum());
                } else if(temp == "elif"){
                    t = new Token(elifToken, curLineNum());
                } else if(temp == "else"){
                    t = new Token(elseToken, curLineNum());
                } else if(temp == "False"){
                    t = new Token(falseToken, curLineNum());
                } else if(temp == "for"){
                    t = new Token(forToken, curLineNum());
                } else if(temp == "if"){
                    t = new Token(ifToken, curLineNum());
                } else if(temp == "in"){
                    t = new Token(inToken, curLineNum());
                } else if(temp == "None"){
                    t = new Token(noneToken, curLineNum());
                } else if(temp == "not"){
                    t = new Token(notToken, curLineNum());
                } else if(temp == "or"){
                    t = new Token(orToken, curLineNum());
                } else if(temp == "pass"){
                    t = new Token(passToken, curLineNum());
                } else if(temp == "return"){
                    t = new Token(returnToken, curLineNum());
                } else if(temp == "True"){
                    t = new Token(trueToken, curLineNum());
                } else if(temp == "while"){
                    t = new Token(whileToken, curLineNum());
                } else{
                    t = new Token(nameToken, curLineNum());
                    t.name = temp;
                }*/
                curLineTokens.add(t);
            } else if (c == '*'){
                Token t = new Token(astToken, curLineNum());
                curLineTokens.add(t);
            } else if (c == '='){
                pos = extendedToken(line, pos, '=', doubleEqualToken, equalToken);
            } else if (c == '/'){
                pos = extendedToken(line, pos, '/', doubleSlashToken, slashToken);
            } else if (c == '>'){
                pos = extendedToken(line, pos, '=', greaterEqualToken, greaterToken);
            } else if (c == '<'){
                pos = extendedToken(line, pos, '=', lessEqualToken, lessToken);
            } else if (c == '-'){
                Token t = new Token(minusToken, curLineNum());
                curLineTokens.add(t);
            } else if (c == '!'){
                Token t = null;
                if(line.charAt(pos+1) == '='){
                    t = new Token(notEqualToken, curLineNum());
                    pos++;
                }
                //TODO is this a scanner error?
                else{
                    scannerError("Cannot read " + curFileName + "!");
                }
                curLineTokens.add(t);
            } else if (c=='%'){
                Token t = new Token(percentToken, curLineNum());
                curLineTokens.add(t);
            } else if (c=='+'){
                Token t = new Token(plusToken, curLineNum());
                curLineTokens.add(t);
            } else if (c==':'){
                Token t = new Token(colonToken, curLineNum());
                curLineTokens.add(t);
            } else if (c==','){
                Token t = new Token(commaToken, curLineNum());
                curLineTokens.add(t);
            } else if (c=='{'){
                Token t = new Token(leftBraceToken, curLineNum());
                curLineTokens.add(t);
            } else if (c=='['){
                Token t = new Token(leftBracketToken, curLineNum());
                curLineTokens.add(t);
            } else if (c=='('){
                Token t = new Token(leftParToken, curLineNum());
                curLineTokens.add(t);
            } else if (c=='}'){
                Token t = new Token(rightBraceToken, curLineNum());
                curLineTokens.add(t);
            } else if (c==']'){
                Token t = new Token(rightBracketToken, curLineNum());
                curLineTokens.add(t);
            } else if (c==')'){
                Token t = new Token(rightParToken, curLineNum());
                curLineTokens.add(t);
            }
            pos++;
        }

        // Terminate line:
        curLineTokens.add(new Token(newLineToken, curLineNum()));

        for (Token t : curLineTokens) {
            Main.log.noteToken(t);
            System.out.println(t.showInfo());
        }
    }

    private int extendedToken(String line, int pos, char c, TokenKind extended, TokenKind single){
        Token t;
        if(line.charAt(pos+1) == c){
            t = new Token(extended, curLineNum());
            pos++;
        } else {
            t = new Token(single, curLineNum());
        }
        curLineTokens.add(t);
        return pos;
    }

    public int curLineNum() {
        return sourceFile != null ? sourceFile.getLineNumber() : 0;
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
        //-- Must be changed in part 2:
        return false;
    }


    public boolean isFactorPrefix() {
        TokenKind k = curToken().kind;
        //-- Must be changed in part 2:
        return false;
    }


    public boolean isFactorOpr() {
        TokenKind k = curToken().kind;
        //-- Must be changed in part 2:
        return false;
    }


    public boolean isTermOpr() {
        TokenKind k = curToken().kind;
        //-- Must be changed in part 2:
        return false;
    }


    public boolean anyEqualToken() {
        for (Token t : curLineTokens) {
            if (t.kind == equalToken) return true;
            if (t.kind == semicolonToken) return false;
        }
        return false;
    }
}
