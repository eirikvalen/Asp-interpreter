package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;
import java.util.HashMap;

public class RuntimeDictValue extends RuntimeValue {
    HashMap<String, RuntimeValue> dictValue;

    public RuntimeDictValue(HashMap<String, RuntimeValue> v) {
        dictValue = v;
    }


    @Override
    protected String typeName() {
        return "dictionary";
    }


    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return dictValue.toString();
    }


    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !dictValue.isEmpty();
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null;  // Required by the compiler
    }


    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(! this.getBoolValue("not operand", where));
    }


    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            return dictValue.get(v.getStringValue("subscription",where));
        }
        runtimeError("Type error for subscription.", where);
        return null;  // Required by the compiler
    }
}
