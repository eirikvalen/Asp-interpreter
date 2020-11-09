package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeFloatValue extends RuntimeValue {
    double floatValue;

    public RuntimeFloatValue(double v) {
        floatValue = v;
    }


    @Override
    protected String typeName() {
        return "float";
    }


    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return Double.toString(floatValue);
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (floatValue != 0.0);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return Double.toString(floatValue);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(floatValue);
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue + v.getIntValue("+ operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue + v.getFloatValue("+ operand", where));
        }
        runtimeError("Type error for +.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue - v.getIntValue("- operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue - v.getFloatValue("- operand", where));
        }
        runtimeError("Type error for -.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue * v.getIntValue("* operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue * v.getFloatValue("* operand", where));
        }
        runtimeError("Type error for *.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue / v.getIntValue("/ operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue / v.getFloatValue("/ operand", where));
        }
        runtimeError("Type error for /.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(Math.floor(floatValue / v.getIntValue(("// operand"), where)));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue(("// operand"), where)));
        }
        runtimeError("Type error for //.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            double vIntValue = v.getIntValue("% operand", where);
            return new RuntimeFloatValue(floatValue - vIntValue*Math.floor(floatValue /vIntValue));
        } else if (v instanceof RuntimeFloatValue){
            double vFloatValue = v.getFloatValue("% operand", where);
            return new RuntimeFloatValue(floatValue - vFloatValue*Math.floor(floatValue /vFloatValue));
        }
        runtimeError("Type error for %.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue < v.getIntValue("< operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue < v.getFloatValue("< operand", where));
        }
        runtimeError("Type error for <.", where);
        return null; // Required by the compiler
    }

    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue > v.getIntValue("> operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue > v.getFloatValue("> operand", where));
        }
        runtimeError("Type error for >.", where);
        return null; // Required by the compiler
    }

    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue <= v.getIntValue("<= operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue <= v.getFloatValue("<= operand", where));
        }
        runtimeError("Type error for <=.", where);
        return null; // Required by the compiler
    }

    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue >= v.getIntValue(">= operand", where));
        } else if (v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue >= v.getFloatValue(">= operand", where));
        }
        runtimeError("Type error for >=.", where);
        return null; // Required by the compiler
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue == v.getIntValue("== operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue == v.getFloatValue("== operand", where));
        }
        runtimeError("Type error for ==.", where);
        return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeBoolValue(floatValue != v.getIntValue("!= operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue != v.getFloatValue("!= operand", where));
        }
        runtimeError("Type error for !=.", where);
        return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!this.getBoolValue("not operand", where));
    }
}
