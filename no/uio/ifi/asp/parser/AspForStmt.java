package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspForStmt extends AspCompoundStmt {

    AspName name;
    AspExpr expression;
    AspSuite suite;

    AspForStmt(int n) {
        super(n);
    }

    public static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());
        skip(s, TokenKind.forToken);
        afs.name = AspName.parse(s);
        skip(s, TokenKind.inToken);
        afs.expression = AspExpr.parse(s);
        skip(s, TokenKind.colonToken);
        afs.suite = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        name.prettyPrint();
        prettyWrite(" in ");
        expression.prettyPrint();
        prettyWrite(":");
        suite.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expression.eval(curScope);
        if(v instanceof RuntimeListValue){
            ArrayList<RuntimeValue> list = ((RuntimeListValue) v).getListValue();
            for (int i = 0; i<list.size(); i++) {
                trace("for #" + (i+1) + ": " + name.name + " = " + list.get(i));
                curScope.assign(name.name, list.get(i));
                suite.eval(curScope);
            }
        }
        return null;
    }
}
