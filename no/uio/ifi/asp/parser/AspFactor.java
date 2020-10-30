package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

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
        RuntimeValue v = primarys.get(0).eval(curScope);
        if(factorPrefixes.get(0) != null){
            TokenKind k = factorPrefixes.get(0).factorPrefix;
            switch (k) {
                case plusToken:
                    v = v.evalPositive(this);
                    break;
                case minusToken:
                    v = v.evalNegate(this);
                    break;
            }
        }
        for (int i = 1; i < primarys.size(); ++i){
            RuntimeValue primary = primarys.get(i).eval(curScope);
            if(factorPrefixes.get(i) != null) {
                TokenKind pre = factorPrefixes.get(i).factorPrefix;
                switch (pre) {
                    case plusToken:
                        primary = primary.evalPositive(this);
                        break;
                    case minusToken:
                        primary = primary.evalNegate(this);
                        break;
                }
            }
            TokenKind opr = factorOprs.get(i-1).factorOpr;
            switch (opr) {
                case astToken:
                    v = v.evalMultiply(primary, this);
                    break;
                case slashToken:
                    v = v.evalDivide(primary, this);
                    break;
                case percentToken:
                    v = v.evalModulo(primary, this);
                    break;
                case doubleSlashToken:
                    v = v.evalIntDivide(primary, this);
                    break;
                default:
                    Main.panic("Illegal term operator: " + opr + "!");
            }
        }

        return v;
    }
}
