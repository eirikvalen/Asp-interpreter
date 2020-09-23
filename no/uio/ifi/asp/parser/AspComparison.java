package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.notToken;

public class AspComparison extends AspSyntax {
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }
    static AspComparison parse(Scanner s) {
        enterParser("comparison test");
        AspComparison ant = new AspComparison(s.curLineNum());

        while (true){
            ant.terms.add(AspTerm.parse(s));
            if(!s.isCompOpr()) break;
            ant.compOprs.add(AspCompOpr.parse(s));
        }

        leaveParser("comparison test");
        return ant;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
