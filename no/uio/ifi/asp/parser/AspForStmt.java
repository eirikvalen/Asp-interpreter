package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspForStmt extends AspCompoundStmt {

    AspName name;
    AspExpr expression;
    AspSuite suite;

    AspForStmt(int n) {
        super(n);
    }

    public static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());
        skip(s, TokenKind.forToken);
        afs.name = AspName.parse(s);
        skip(s, TokenKind.inToken);
        afs.expression = AspExpr.parse(s);
        skip(s, TokenKind.colonToken);
        afs.suite = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }
}
