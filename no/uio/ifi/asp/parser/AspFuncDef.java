package no.uio.ifi.asp.parser;

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
}
