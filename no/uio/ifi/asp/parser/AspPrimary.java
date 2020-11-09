package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {
    AspAtom atom;
    ArrayList<AspPrimarySuffix> primarySuffixes = new ArrayList<>();

    AspPrimary(int n) {
        super(n);
    }

    static AspPrimary parse(Scanner s) {
        enterParser("primary");

        AspPrimary ap = new AspPrimary(s.curLineNum());
        ap.atom = AspAtom.parse(s);
        while (s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken) {
            ap.primarySuffixes.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    void prettyPrint() {
        atom.prettyPrint();
        for (AspPrimarySuffix aps : primarySuffixes) {
            aps.prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = atom.eval(curScope);

        for (AspPrimarySuffix primarySuffix : primarySuffixes) {
            if (v instanceof RuntimeDictValue || v instanceof RuntimeListValue || v instanceof RuntimeStringValue) {
                v = v.evalSubscription(primarySuffix.eval(curScope), this);
            }else if (v instanceof RuntimeFuncValue && primarySuffix instanceof AspArguments){

                RuntimeListValue params = (RuntimeListValue) primarySuffix.eval(curScope);
                trace("Call function " + v.toString() + " with parameters " + params.toString());
                v = v.evalFuncCall(params.getListValue(), this);

            }
        }
        return v;

    }
}




