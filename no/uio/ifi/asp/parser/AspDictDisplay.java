package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspDictDisplay extends AspAtom {

    ArrayList<AspStringLiteral> stringLits = new ArrayList<>();
    ArrayList<AspExpr> expressions = new ArrayList<>();

    AspDictDisplay(int n) {
        super(n);
    }

    public static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay adict = new AspDictDisplay(s.curLineNum());
        skip(s, TokenKind.leftBraceToken);
        while (true) {
            adict.stringLits.add(AspStringLiteral.parse(s));
            skip(s, TokenKind.colonToken);
            adict.expressions.add(AspExpr.parse(s));
            if (s.curToken().kind != TokenKind.commaToken) {
                break;
            }
            skip(s, TokenKind.commaToken);
        }
        skip(s, TokenKind.rightBraceToken);

        leaveParser("dict display");
        return adict;
    }


    @Override
    public void prettyPrint() {

    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
