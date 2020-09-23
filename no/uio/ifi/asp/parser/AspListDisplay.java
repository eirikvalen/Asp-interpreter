package no.uio.ifi.asp.parser;

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

    @Override
    void prettyPrint() {

    }

    public static AspListDisplay parse(Scanner s) {
        enterParser("list display");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());

        skip(s, TokenKind.leftBraceToken);

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
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
