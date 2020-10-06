package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspSmallStmt extends AspSyntax {

    AspSmallStmt(int n) { super(n); }

    static AspSmallStmt parse(Scanner s) {
        enterParser("small stmt");

        AspSmallStmt ass;
        switch (s.curToken().kind) {
            case returnToken:
                ass = AspReturnStmt.parse(s);
                break;
            case passToken:
                ass = AspPassStmt.parse(s);
                break;
            default:
                if (s.anyEqualToken()) {
                    ass = AspAssignment.parse(s);
                } else {
                    ass = AspExprStmt.parse(s);
                }
        }

        leaveParser("small stmt");
        return ass;
    }

}
