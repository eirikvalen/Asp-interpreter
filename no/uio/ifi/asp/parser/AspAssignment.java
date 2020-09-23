package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspSmallStmt{
    AspName an;
    Arraylist<AspSubscription> as = new ArrayList<>();
    AspExpr ae;

    AspAssignment(int n) { super(n); }

    static AspAssignment parse(Scanner s){
        enterParser("assignment");
        AspAssignment aa = new AspAssignment(s.curLineNum());

        aa.an = AspName.parse(s);

        while(s.curToken().kind != equalToken){
            aa.as.add(AspSubscription.parse(s));
        }

        skip(s, equalToken);

        aa.ae = AspExpr.parse(s);

        leaveParser("assignment");
        return aa;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
