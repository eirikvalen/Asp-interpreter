package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeFuncValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt {

    ArrayList<AspName> names = new ArrayList<>();
    AspSuite suite;

    AspFuncDef(int n) {
        super(n);
    }

    public static AspFuncDef parse(Scanner s) {
        enterParser("func def");

        AspFuncDef afd = new AspFuncDef(s.curLineNum());

        skip(s, TokenKind.defToken);
        afd.names.add(AspName.parse(s));
        skip(s, TokenKind.leftParToken);

        while (s.curToken().kind != TokenKind.rightParToken) {
            afd.names.add(AspName.parse(s));
            if (s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
            }
        }

        skip(s, TokenKind.rightParToken);
        skip(s, TokenKind.colonToken);

        afd.suite = AspSuite.parse(s);

        leaveParser("func def");

        return afd;
    }

    public void evalSuite(RuntimeScope scope) throws RuntimeReturnValue {
        suite.eval(scope);
    }

    public ArrayList<AspName> getFormalParameters() {
        ArrayList<AspName> formalParams = new ArrayList<>(names);
        formalParams.remove(0);
        return formalParams;
    }



    @Override
    public void prettyPrint() {
        prettyWrite("def ");
        names.get(0).prettyPrint();
        prettyWrite("(");

        for (int i = 1; i < names.size(); i++) {
            if (i != 1) {
                prettyWrite(", ");
            }
            names.get(i).prettyPrint();
        }
        prettyWrite("):");
        suite.prettyPrint();
        prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeFuncValue v = new RuntimeFuncValue(this, curScope);
        curScope.assign(names.get(0).name, v);

        return v;

    }
}
