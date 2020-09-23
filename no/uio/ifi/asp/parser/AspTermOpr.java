package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import static no.uio.ifi.asp.scanner.TokenKind.*;
import static no.uio.ifi.asp.scanner.TokenKind.notEqualToken;

public class AspTermOpr extends AspSyntax{
    AspTermOpr(int n) { super(n); }

    static AspTermOpr parse(Scanner s){
        enterParser("pass stmt");
        AspTermOpr aps = new AspTermOpr(s.curLineNum());

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
