package interpreter.toylanguageinterpreter.Model.Value;

import interpreter.toylanguageinterpreter.Model.Type.BoolType;
import interpreter.toylanguageinterpreter.Model.Type.Type;

public class BoolValue implements Value{
    private final boolean val;
    
    public BoolValue(boolean v) {
        val = v;
    }
    public boolean getValue() {
        return val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(val);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoolValue && val == ((BoolValue)obj).val;
    }
}




