package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeFuncValue extends RuntimeValue {
    @Override
    protected String typeName() {
        return "None";
    }


    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return "None";
    }


    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return false;
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        return new RuntimeBoolValue(v instanceof RuntimeFuncValue);
    }


    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(true);
    }


    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        return new RuntimeBoolValue(!(v instanceof RuntimeFuncValue));
    }
}
