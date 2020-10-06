package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTermOpr extends AspSyntax {

    TokenKind termOpr;

    AspTermOpr(int n) { super(n); }

    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");

        AspTermOpr aps = new AspTermOpr(s.curLineNum());
        aps.termOpr = s.curToken().kind;

        switch (s.curToken().kind) {
            case plusToken:
                skip(s, plusToken);
                break;
            case minusToken:
                skip(s, minusToken);
                break;
        }

        leaveParser("term opr");
        return aps;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + termOpr.toString() + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
