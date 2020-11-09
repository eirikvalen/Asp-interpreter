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

                double doubleValue = 0.0;

                if (v instanceof RuntimeStringValue){
                    try{
                        doubleValue = Double.parseDouble(v.getStringValue("float", where));
                    }catch (Exception e){
                        runtimeError("String " + v.showInfo() + " is not a legal float", where);
                    }


                } else if (v instanceof RuntimeIntValue){
                    doubleValue = (double) v.getIntValue("float", where);
                }else if (v instanceof RuntimeFloatValue){
                    doubleValue = v.getFloatValue("float", where);
                }else{
                    runtimeError("Type error: parameter to float is neither number nor text string", where);
                }
                return new RuntimeFloatValue(doubleValue);
            }
        });

        //int
        assign("int", new RuntimeFuncValue("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);

                RuntimeValue v = actualParams.get(0);

                long intValue = 0;

                if (v instanceof RuntimeStringValue){
                    try{
                        intValue = Long.parseLong(v.getStringValue("int", where));
                    }catch (Exception e){
                        runtimeError("String " + v.showInfo() + " is not a legal int", where);
                    }


                } else if (v instanceof RuntimeIntValue){
                    intValue = v.getIntValue("int",where);

                }else if (v instanceof RuntimeFloatValue){
                    intValue = (long) v.getFloatValue("int", where);

                }else{
                    runtimeError("Type error: parameter to int is neither number nor text string", where);
                }
                return new RuntimeIntValue(intValue);
            }
        });


        //str
        assign("str", new RuntimeFuncValue("str"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);

                return new RuntimeStringValue(actualParams.get(0).toString());
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
                System.out.print(actualParams.get(0));
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
