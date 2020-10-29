package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeBoolValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspBooleanLiteral extends AspAtom {

    Boolean booleanLiteral;

    AspBooleanLiteral(int n) {
        super(n);
    }

    public static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");

        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
        abl.booleanLiteral = s.curToken().kind == TokenKind.trueToken;

        if (s.curToken().kind == TokenKind.trueToken) {
            skip(s, TokenKind.trueToken);
        } else {
            skip(s, TokenKind.falseToken);
        }

        leaveParser("boolean literal");
        return abl;
    }

    @Override
    void prettyPrint() {
        prettyWrite(booleanLiteral ? "True" : "False");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(booleanLiteral);
    }
}
