package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspComparison extends AspSyntax {
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {
        enterParser("comparison");

        AspComparison ac = new AspComparison(s.curLineNum());
        while (true) {
            ac.terms.add(AspTerm.parse(s));
            if (!s.isCompOpr()) break;
            ac.compOprs.add(AspCompOpr.parse(s));
        }

        leaveParser("comparison");
        return ac;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < terms.size(); i++) {
            terms.get(i).prettyPrint();
            if (i != compOprs.size()) {
                compOprs.get(i).prettyPrint();
            }
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = terms.get(0).eval(curScope);
        for (int i = 1; i < terms.size(); ++i){
            TokenKind k = compOprs.get(i-1).compOpr;
            v = terms.get(i-1).eval(curScope);
            switch (k) {
                case lessToken:
                    v = v.evalLess(terms.get(i).eval(curScope), this);
                    break;
                case greaterToken:
                    v = v.evalGreater(terms.get(i).eval(curScope), this);
                    break;
                case doubleEqualToken:
                    v = v.evalEqual(terms.get(i).eval(curScope), this);
                    break;
                case greaterEqualToken:
                    v = v.evalGreaterEqual(terms.get(i).eval(curScope), this);
                    break;
                case lessEqualToken:
                    v = v.evalLessEqual(terms.get(i).eval(curScope), this);
                    break;
                case notEqualToken:
                    v = v.evalNotEqual(terms.get(i).eval(curScope), this);
                    break;
                default:
                    Main.panic("Illegal comparison operator: " + k + "!");
            }
             if (!v.getBoolValue("comparison operator", this)){
                 return v;
             }

        }
        return v;
    }
}
