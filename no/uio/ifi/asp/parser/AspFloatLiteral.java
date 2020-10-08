package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFloatLiteral extends AspAtom {

    Double floatLiteral;

    AspFloatLiteral(int n) {
        super(n);
    }

    public static AspFloatLiteral parse(Scanner s) {
        enterParser("float literal");

        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());
        afl.floatLiteral = s.curToken().floatLit;

        skip(s, TokenKind.floatToken);

        leaveParser("float literal");
        return afl;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(Double.toString(floatLiteral));
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
