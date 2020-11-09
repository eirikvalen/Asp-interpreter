package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt {

    ArrayList<AspExpr> expressions = new ArrayList<>();
    ArrayList<AspSuite> suites = new ArrayList<>();

    AspIfStmt(int n) {
        super(n);
    }

    public static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());
        skip(s, TokenKind.ifToken);

        while (true) {
            ais.expressions.add(AspExpr.parse(s));
            skip(s, TokenKind.colonToken);
            ais.suites.add(AspSuite.parse(s));

            if (s.curToken().kind != TokenKind.elifToken) {
                break;
            }
            skip(s, TokenKind.elifToken);
        }

        if (s.curToken().kind == TokenKind.elseToken) {
            skip(s, TokenKind.elseToken);
            skip(s, TokenKind.colonToken);
            ais.suites.add(AspSuite.parse(s));
        }

        leaveParser("if stmt");
        return ais;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("if ");
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                prettyWrite("elif ");
            }
            expressions.get(i).prettyPrint();
            prettyWrite(": ");
            suites.get(i).prettyPrint();
        }
        if (suites.size() > expressions.size()) {
            prettyWrite("else: ");
            suites.get(suites.size() - 1).prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for(int i=0; i < expressions.size(); i++){
            RuntimeValue t = expressions.get(i).eval(curScope);
            if (t.getBoolValue("if test", this)){
                 trace("if True alt #" + (i+1));
                suites.get(i).eval(curScope);
                return null;
            }
        }
        if(suites.size() > expressions.size()) {
            trace("else:");
            suites.get(suites.size()-1).eval(curScope);
        }
        return null;
    }
}

