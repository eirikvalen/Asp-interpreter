package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

public class AspTerm extends AspSyntax{
    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> termOprs = new ArrayList<>();

    AspTerm(int n) {
        super(n);
    }
    static AspTerm parse(Scanner s) {
        enterParser("comparison test");
        AspTerm at = new AspTerm(s.curLineNum());

        while (true){
            at.factors.add(AspFactor.parse(s));
            if(!s.isTermOpr()) break;
            at.termOprs.add(AspTermOpr.parse(s));
        }

        leaveParser("comparison test");
        return at;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
