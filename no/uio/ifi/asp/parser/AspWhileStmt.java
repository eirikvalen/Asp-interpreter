package no.uio.ifi.asp.parser;


import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspWhileStmt extends AspCompoundStmt{
    AspExpr test;
    AspSuite body;



    AspWhileStmt(int n) {
        super(n);
    }


    public static AspWhileStmt parse(Scanner s){
        enterParser("while");

        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());

        skip(s, TokenKind.whileToken);
        aws.test = AspExpr.parse(s);
        skip(s, TokenKind.colonToken);
        aws.body = AspSuite.parse(s);

        leaveParser("while");

        return aws;

    }
}
