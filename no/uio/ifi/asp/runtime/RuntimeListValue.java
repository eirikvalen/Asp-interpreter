package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeListValue extends RuntimeValue {
    ArrayList<RuntimeValue> listValue;

    public RuntimeListValue(ArrayList<RuntimeValue> v) {
        listValue = v;
    }

    public ArrayList<RuntimeValue> getListValue(){
        return listValue;
    }


    @Override
    protected String typeName() {
        return "list";
    }


    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
       return listValue.toString();

    }


    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return ! listValue.isEmpty();
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return listValue.toString();
    }


    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!this.getBoolValue("not operand", where));
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
       if (v instanceof RuntimeIntValue){
           int index = (int) v.getIntValue("subscription", where);
           if (index >= listValue.size() || index < 0){
               runtimeError("List index " + index + " is out of range!", where);
           }
           return listValue.get(index);
       }
        runtimeError("Type error for subscription.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue){

            ArrayList<RuntimeValue> values = new ArrayList<>();

            for(int i = 0; i < v.getIntValue("* operand", where); i++){
                values.addAll(this.listValue);
            }

            return new RuntimeListValue(values);
        }
        runtimeError("Type error for *.", where);
        return null; // Required by the compiler
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
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;  // Required by the compiler
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        listValue.set((int) inx.getIntValue("assign element", where), val);
    }
}
