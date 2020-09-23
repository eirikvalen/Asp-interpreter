package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import javax.swing.text.StyleContext;
import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {
    ArrayList<AspExpr> exprs = new ArrayList<>();
    AspSmallStmt sStmt;

    AspSuite(int n) {
        super(n);
    }

    public static AspSuite parse (Scanner s){
        AspSuite as = new AspSuite(s.curLineNum());
        if(s.curToken().kind == newLineToken){
            skip(s, newLineToken);
            skip(s, indentToken);
            while(s.curToken().kind != dedentToken){
                as.exprs.add(AspExpr.parse(s));
            }
            skip(s, dedentToken);
        } else{
            as.sStmt = AspSmallStmt.parse(s);
        }
        return as;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
