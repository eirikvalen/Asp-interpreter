package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;
import java.util.ArrayList;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> sstmts = new ArrayList<>();

    AspSmallStmtList(int n) { super(n); }

    public static AspSmallStmtList parse(Scanner s) {
        enterParser("small statement list");

        AspSmallStmtList assm = new AspSmallStmtList(s.curLineNum());

        while(true){
            assm.sstmts.add(AspSmallStmt.parse(s));
            //TODO Funker dette sånn? skal man skippe når man sjekker kind?
            if(s.curToken().kind == semicolonToken){
                skip(s, semicolonToken);
            }
            if(s.curToken().kind == newLineToken){
                skip(s, newLineToken);
                break;
            }
        }

        leaveParser("small statement list");
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
