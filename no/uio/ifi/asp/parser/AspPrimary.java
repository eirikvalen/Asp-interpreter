package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
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

        while(s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken){
            ap.primarySuffixes.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");

        return ap;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
