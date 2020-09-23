package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt {

    ArrayList<AspExpr> expressions = new ArrayList<>();
    ArrayList<AspSuite> suites = new ArrayList<>();

    //TODO sjekke om vi trenger to lister til hver suite?

    AspIfStmt(int n) {
        super(n);
    }

    public static AspIfStmt parse(Scanner s) {
        enterParser("if");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());
        skip(s, TokenKind.ifToken);

        while (true) {
            ais.expressions.add(AspExpr.parse(s));
            skip(s, TokenKind.colonToken);
            ais.suites.add(AspSuite.parse(s));

            if (s.curToken().kind != TokenKind.elifToken) {
                break;
            }
            skip(s, TokenKind.elifToken);
        }

        if (s.curToken().kind == TokenKind.elseToken) {
            skip(s, TokenKind.elseToken);
            skip(s, TokenKind.colonToken);
            ais.suites.add(AspSuite.parse(s));
        }

        leaveParser("if");
        return ais;
    }
}
