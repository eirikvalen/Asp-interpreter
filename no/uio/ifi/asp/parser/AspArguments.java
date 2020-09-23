package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s) {
        enterParser("arguments");
        AspArguments aa = null;

        skip(s, leftParToken);

        //TODO fix potensiell feil med komma på slutten uten expr (1,)
        if(s.curToken().kind != rightParToken){
            while(true){
                aa.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) break;
                skip(s, commaToken);
            }
        }

        skip(s, rightParToken);

        leaveParser("arguments");
        return aa;
    }

    @Override
    void prettyPrint() {

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
