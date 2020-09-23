package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import static no.uio.ifi.asp.scanner.TokenKind.minusToken;
import static no.uio.ifi.asp.scanner.TokenKind.plusToken;

public class AspFactorPrefix extends AspSyntax{
    AspFactorPrefix(int n) { super(n); }

    static AspFactorPrefix parse(Scanner s){
        enterParser("pass stmt");
        AspFactorPrefix aps = new AspFactorPrefix(s.curLineNum());

        switch(s.curToken().kind){
            case plusToken:
                skip(s,plusToken);
            case minusToken:
                skip(s,minusToken);
        }

        leaveParser("pass stmt");
        return aps;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
