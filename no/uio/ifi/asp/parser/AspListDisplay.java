package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspListDisplay extends AspAtom {

    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }

    public static AspListDisplay parse(Scanner s) {
        enterParser("list display");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());
        skip(s, TokenKind.leftBracketToken);
        if (s.curToken().kind != TokenKind.rightBracketToken) {
            while (true) {
                ald.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind != TokenKind.commaToken) {
                    break;
                }
                skip(s, TokenKind.commaToken);
            }
        }
        skip(s, TokenKind.rightBracketToken);

        leaveParser("list display");
        return ald;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("[");
        for (AspExpr expr : exprs) {
            if (nPrinted > 0) {
                prettyWrite(", ");
            }
            expr.prettyPrint();
            nPrinted++;
        }
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> values = new ArrayList<>();
        for(AspExpr expr : exprs){
            values.add(expr.eval(curScope));
        }

        return new RuntimeListValue(values);
    }
}
