package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        //-- Must be changed in part 4:

        //str
        assign("str", new RuntimeFuncValue("str"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                return new RuntimeStringValue(actualParams.get(0).getStringValue("str", where));
            }
        });

        //range
        assign("range", new RuntimeFuncValue("range"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                long start = actualParams.get(0).getIntValue("range", where);
                long end = actualParams.get(1).getIntValue("range", where);

                ArrayList<RuntimeValue> range = new ArrayList<>();
                for (long i = start; i<end; i++){
                    range.add(new RuntimeIntValue(i));
                }
                return new RuntimeListValue(range);
            }
        });

        //input
        assign("input", new RuntimeFuncValue("input"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.println(actualParams.get(0));
                return new RuntimeStringValue(keyboard.nextLine());
            }
        });
    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
                                int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}
