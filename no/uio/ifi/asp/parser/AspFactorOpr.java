package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorOpr extends AspSyntax {

    AspFactorOpr(int n) { super(n); }

    static AspFactorOpr parse(Scanner s){
        enterParser("factor opr");
        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());

        switch(s.curToken().kind){
            case astToken:
                skip(s,astToken);
            case slashToken:
                skip(s,slashToken);
            case percentToken:
                skip(s,percentToken);
            case doubleSlashToken:
                skip(s,doubleSlashToken);
        }

        leaveParser("factor opr");
        return afo;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
