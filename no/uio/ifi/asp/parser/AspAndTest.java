package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAndTest extends AspSyntax {

    ArrayList<AspNotTest> notTests = new ArrayList<>();

    AspAndTest(int n) {
        super(n);
    }

    static AspAndTest parse(Scanner s) {
        enterParser("and test");

        AspAndTest aat = new AspAndTest(s.curLineNum());
        while (true) {
            aat.notTests.add(AspNotTest.parse(s));
            if (s.curToken().kind != andToken) break;
            skip(s, andToken);
        }

        leaveParser("and test");
        return aat;
    }

    @Override
    void prettyPrint() {
        int nPrinted = 0;
        for (AspNotTest ant : notTests) {
            if (nPrinted > 0) {
                prettyWrite(" and ");
            }
            ant.prettyPrint();
            ++nPrinted;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
