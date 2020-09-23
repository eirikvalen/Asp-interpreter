package no.uio.ifi.asp.parser;

import com.sun.org.apache.bcel.internal.classfile.ConstantLong;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt {

    ArrayList<AspName> names;
    AspSuite body;

    AspFuncDef(int n) {
        super(n);
    }


    public static AspFuncDef parse(Scanner s){

        enterParser("function def");

        AspFuncDef afd = new AspFuncDef(s.curLineNum());

        afd.names.add(AspName.parse(s));
        skip(s, TokenKind.leftParToken);

        while(true){
            afd.names.add(AspName.parse(s));
            if (s.curToken().kind != TokenKind.commaToken){
                break;
            }
            skip(s, TokenKind.commaToken);
        }

        skip(s, TokenKind.rightParToken);
        skip(s, TokenKind.colonToken);

        afd.body = AspSuite.parse(s);


        leaveParser("function def");

        return afd;

    }


}
