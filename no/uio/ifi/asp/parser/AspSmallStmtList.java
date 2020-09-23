package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;
import java.util.ArrayList;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();

    AspSmallStmtList(int n) { super(n); }

    public static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");

        AspSmallStmtList assm = new AspSmallStmtList(s.curLineNum());

        while(true){
            assm.smallStmts.add(AspSmallStmt.parse(s));
            if(s.curToken().kind == semicolonToken){
                skip(s, semicolonToken);
            }
            if(s.curToken().kind == newLineToken){
                skip(s, newLineToken);
                break;
            }
        }

        leaveParser("small stmt list");
        return assm;
    }


    @Override
    public void prettyPrint() {
        //-- Must be changed in part 2:
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        //-- Must be changed in part 3:
        return null;
    }

}
