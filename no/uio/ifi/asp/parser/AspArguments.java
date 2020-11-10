package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AspArguments extends AspPrimarySuffix {

    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s) {
        enterParser("arguments");

        AspArguments aa = new AspArguments(s.curLineNum());
        skip(s, leftParToken);
        if (s.curToken().kind != rightParToken) {
            while (true) {
                aa.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) break;
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);

        leaveParser("arguments");
        return aa;
    }

    @Override
    void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("(");
        for (AspExpr expr : exprs) {
            if (nPrinted != 0) {
                prettyWrite(", ");
            }
            expr.prettyPrint();
            nPrinted++;
        }
        prettyWrite(")");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> actualParams = new ArrayList<>();

        for(AspExpr expr : exprs){
            actualParams.add(expr.eval(curScope));
        }

        RuntimeListValue v = new RuntimeListValue(actualParams);
        return v;
    }
}
