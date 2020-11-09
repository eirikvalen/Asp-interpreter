package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        //-- Must be changed in part 4:
        //len
        assign("len", new RuntimeFuncValue("len") {
            @Override
            public RuntimeValue evalFuncCall(
                    ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });

        //print
        assign("print", new RuntimeFuncValue("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                for (int i = 0; i < actualParams.size(); ++i) {
                    if (i > 0) System.out.print(" ");
                    System.out.println(actualParams.get(i).toString());
                }
                //System.out.println();
                return new RuntimeNoneValue();
            }
        });

        //exit
        assign("exit", new RuntimeFuncValue("exit") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "exit", where);
                System.exit((int) actualParams.get(0).getIntValue("exit", where));
                return new RuntimeNoneValue();
            }
        });

        //float
        assign("float", new RuntimeFuncValue("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);

                RuntimeValue v = actualParams.get(0);
                if (v instanceof RuntimeStringValue){
                    double doubleValue = v.getFloatValue("float", where);
                    return new RuntimeFloatValue(doubleValue);

                }else if (v instanceof RuntimeIntValue){
                    return new RuntimeFloatValue(v.getFloatValue("float", where));

                }


                return new RuntimeNoneValue();
            }
        });

    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
                                int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}
