package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeStringValue extends RuntimeValue {
    String strValue;

    public RuntimeStringValue(String v) {
        strValue = v;
    }


    @Override
    protected String typeName() {
        return "string";
    }


    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        if (strValue.contains("\"")) return "'" + strValue + "'";
        else return '"' + strValue + '"';
    }

    @Override
    public String toString() {
        return strValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (!strValue.isEmpty());
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return strValue;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeStringValue){
            return new RuntimeStringValue(strValue + v.getStringValue("+ operand", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            String multiplied = new String(new char[(int) v.getIntValue("* operand", where)])
                    .replace("\0", strValue);
            return new RuntimeStringValue(multiplied);
        }
        runtimeError("Type error for *.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(strValue.compareTo(v.getStringValue("< operand", where)) < 0);
        }
        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(strValue.compareTo(v.getStringValue("> operand", where)) > 0);
        }
        runtimeError("Type error for >.", where);
        return null; // Required by the compiler
    }

    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(strValue.compareTo(v.getStringValue("<= operand", where)) <= 0);
        }
        runtimeError("Type error for <=.", where);
        return null; // Required by the compiler
    }

    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(strValue.compareTo(v.getStringValue(">= operand", where)) >= 0);
        }
        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(strValue.equals(v.getStringValue("== operand", where)));
        } else if (v instanceof RuntimeNoneValue){
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(!strValue.equals(v.getStringValue("!= operand", where)));
        } else if (v instanceof RuntimeNoneValue){
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue){
            int index = (int) v.getIntValue("subscription", where);
            if (index >= strValue.length() || index < 0){
                runtimeError("String index " + index + " is out of range!", where);
            }
            return new RuntimeStringValue(Character.toString(strValue.charAt((int) v.getIntValue("subscription", where))));
        }
        runtimeError("Type error for subscription", where);
        return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!this.getBoolValue("not operand", where));
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(strValue.length());
    }
}
