package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AspDictDisplay extends AspAtom {

    ArrayList<AspStringLiteral> stringLits = new ArrayList<>();
    ArrayList<AspExpr> expressions = new ArrayList<>();

    AspDictDisplay(int n) {
        super(n);
    }

    public static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay adict = new AspDictDisplay(s.curLineNum());
        skip(s, TokenKind.leftBraceToken);
        while (true) {
            adict.stringLits.add(AspStringLiteral.parse(s));
            skip(s, TokenKind.colonToken);
            adict.expressions.add(AspExpr.parse(s));
            if (s.curToken().kind != TokenKind.commaToken) {
                break;
            }
            skip(s, TokenKind.commaToken);
        }
        skip(s, TokenKind.rightBraceToken);

        leaveParser("dict display");
        return adict;
    }


    @Override
    public void prettyPrint() {
        prettyWrite("{");
        for(int i = 0; i<stringLits.size(); i++){
            if(i != 0){
                prettyWrite(", ");
            }
            stringLits.get(i).prettyPrint();
            prettyWrite(": ");
            expressions.get(i).prettyPrint();
        }
        prettyWrite("}");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        LinkedHashMap<String, RuntimeValue> values = new LinkedHashMap<>();

        for(int i = 0; i < stringLits.size(); i++){
            values.put(stringLits.get(i).stringLit, expressions.get(i).eval(curScope));
        }
        return new RuntimeDictValue(values);
    }
}
