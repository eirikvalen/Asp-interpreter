package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeListValue extends RuntimeValue {
    boolean boolValue;

    public RuntimeListValue(boolean v) {
        boolValue = v;
    }


    @Override
    protected String typeName() {
        return "boolean";
    }


    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return (boolValue ? "True" : "False");
    }


    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return boolValue;
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeListValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null;  // Required by the compiler
    }


    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeListValue(!boolValue);
    }


    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeListValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;  // Required by the compiler
    }
}
