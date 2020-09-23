package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

public class AspExprStmt extends AspSmallStmt {
    AspExpr ae;

    AspExprStmt(int n) { super(n); }

    static AspExprStmt parse(Scanner s){
        enterParser("expr stmt");
        AspExprStmt ars = new AspExprStmt(s.curLineNum());

        ars.ae = AspExpr.parse(s);

        leaveParser("expr stmt");
        return ars;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
