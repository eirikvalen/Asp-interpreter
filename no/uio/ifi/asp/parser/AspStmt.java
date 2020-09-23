package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public abstract class AspStmt extends AspSyntax{
    ArrayList<AspStmt> stmts = new ArrayList<>();

    AspStmt(int n) {
        super(n);
    }


    public static AspStmt parse(Scanner s) {
        enterParser("program");

        AspStmt as = null;
        if (s.curToken().kind == forToken || s.curToken().kind == defToken || s.curToken().kind == ifToken || s.curToken().kind == whileToken){
            as = AspCompoundStmt.parse(s);
        }else{
            as = AspSmallStmtList.parse(s);
        }


        leaveParser("program");
        return as;
    }


    @Override
    public void prettyPrint() {

    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        return null;
    }
    
}
