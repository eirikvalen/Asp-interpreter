package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

public abstract class AspCompoundStmt extends AspStmt {

    AspCompoundStmt(int n) {
        super(n);
    }

    public static AspCompoundStmt parse(Scanner s) {
        enterParser("compound stmt");

        AspCompoundStmt acs = null;
        switch (s.curToken().kind) {
            case forToken:
                acs = AspForStmt.parse(s);
                break;
            case defToken:
                acs = AspFuncDef.parse(s);
                break;
            case ifToken:
                acs = AspIfStmt.parse(s);
                break;
            case whileToken:
                acs = AspWhileStmt.parse(s);
                break;
            default:
                parserError("Expected a compound statement but found a " +
                        s.curToken().kind + "!", s.curLineNum());
        }

        leaveParser("compound stmt");
        return acs;
    }

}
