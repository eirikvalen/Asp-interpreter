package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspName extends AspAtom {

    String name;

    AspName(int n) {
        super(n);
    }

    public static AspName parse(Scanner s) {
        enterParser("name");

        AspName an = new AspName(s.curLineNum());
        an.name = s.curToken().name;

        skip(s, TokenKind.nameToken);

        leaveParser("name");
        return an;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(name);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
