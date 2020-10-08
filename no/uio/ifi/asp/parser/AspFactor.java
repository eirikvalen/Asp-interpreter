package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

public class AspFactor extends AspSyntax {

    ArrayList<AspFactorPrefix> factorPrefixes = new ArrayList<>();
    ArrayList<AspPrimary> primarys = new ArrayList<>();
    ArrayList<AspFactorOpr> factorOprs = new ArrayList<>();

    AspFactor(int n) {
        super(n);
    }

    static AspFactor parse(Scanner s) {
        enterParser("factor");

        AspFactor af = new AspFactor(s.curLineNum());
        while (true) {
            if (s.isFactorPrefix()) af.factorPrefixes.add(AspFactorPrefix.parse(s));
            else af.factorPrefixes.add(null);
            af.primarys.add(AspPrimary.parse(s));
            if (!s.isFactorOpr()) break;
            af.factorOprs.add(AspFactorOpr.parse(s));
        }

        leaveParser("factor");
        return af;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < primarys.size(); i++) {
            AspFactorPrefix factorPrefix = factorPrefixes.get(i);
            if (factorPrefix != null) {
                factorPrefix.prettyPrint();
            }
            primarys.get(i).prettyPrint();
            if (i != factorOprs.size()) {
                factorOprs.get(i).prettyPrint();
            }
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
