package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspStmt extends AspSyntax {

    AspStmt(int n) {
        super(n);
    }

    public static AspStmt parse(Scanner s) {
        enterParser("stmt");

        AspStmt as;
        if (s.curToken().kind == forToken || s.curToken().kind == defToken || s.curToken().kind == ifToken || s.curToken().kind == whileToken) {
            as = AspCompoundStmt.parse(s);
        } else {
            as = AspSmallStmtList.parse(s);
        }

        leaveParser("stmt");
        return as;
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
