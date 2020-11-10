package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspSmallStmt {

    AspName name;
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expr;

    AspAssignment(int n) {
        super(n);
    }

    static AspAssignment parse(Scanner s) {
        enterParser("assignment");

        AspAssignment aa = new AspAssignment(s.curLineNum());
        aa.name = AspName.parse(s);
        while (s.curToken().kind != equalToken) {
            aa.subscriptions.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        aa.expr = AspExpr.parse(s);

        leaveParser("assignment");
        return aa;
    }

    @Override
    void prettyPrint() {
        name.prettyPrint();
        for (AspSubscription sub : subscriptions) {
            sub.prettyPrint();
        }
        prettyWrite(" = ");
        expr.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        if(subscriptions.isEmpty()){
            curScope.assign(name.name, v);
            trace(name.name + " = " + v.showInfo());
        } else{
            StringBuilder trace = new StringBuilder();
            RuntimeValue a = name.eval(curScope);
            trace.append(name.name);
            RuntimeValue index = subscriptions.get(0).eval(curScope);
            for(int i = 0; i< subscriptions.size()-1; i++){
                trace.append("[").append(index.showInfo()).append("]");
                a = a.evalSubscription(index, this);
                index = subscriptions.get(i).eval(curScope);
            }
            trace.append("[").append(index.showInfo()).append("]");
            a.evalAssignElem(index, v, this);
            trace.append(" = ").append(v.showInfo());
            trace(trace.toString());
        }
        return null;
    }
}
