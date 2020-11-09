package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspName;
import no.uio.ifi.asp.parser.AspSuite;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeFuncValue extends RuntimeValue {
    AspFuncDef def;
    RuntimeScope defScope;
    String name;

    public RuntimeFuncValue(String def){
        this.name = def;
    }

    public RuntimeFuncValue(AspFuncDef def, RuntimeScope defScope, String name){
        this.def = def;
        this.defScope = defScope;
        this.name = name;
    }


    @Override
    protected String typeName() {
        return "Function";
    }


    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return name;
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
        ArrayList<AspName> formalParameters = def.getFormalParameters();

        if (actualParams.size() != formalParameters.size()){
            runtimeError("Wrong number of arguments: Expected " + formalParameters.size(), where);
        }
        RuntimeScope newScope = new RuntimeScope(defScope);

        for(int i = 0; i < actualParams.size(); i++){
            newScope.assign(formalParameters.get(i).getName(), actualParams.get(i));
        }

        try{
            def.evalSuite(newScope);
        } catch (RuntimeReturnValue e){
            return e.value;
        }
        return new RuntimeNoneValue();
    }
}
