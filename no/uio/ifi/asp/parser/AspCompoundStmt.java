package no.uio.ifi.asp
        .parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

import static no.uio.ifi.asp.scanner.TokenKind.*;
import static no.uio.ifi.asp.scanner.TokenKind.whileToken;

public abstract class AspCompoundStmt extends AspStmt{

    AspCompoundStmt(int n) {
        super(n);
    }


    public static AspCompoundStmt parse(Scanner s) {
        enterParser("program");

        AspCompoundStmt acs = null;

        switch (s.curToken().kind){
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
                //TODO Endre message
                parserError("compund statment error", s.curLineNum());


        }



        leaveParser("program");
        return acs;
    }


    @Override
    public void prettyPrint() {

    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        return null;
    }



}