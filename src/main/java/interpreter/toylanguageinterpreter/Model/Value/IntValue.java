package interpreter.toylanguageinterpreter.Model.Value;

import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.Type;

public class IntValue implements Value {
    private int val;
    public IntValue(int v) {
        val = v;
    }
    public int getValue() {
        return val;
    }
    public void setValue(int v) {
        val = v;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public Value deepCopy() {
        return new IntValue(val);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntValue && val == ((IntValue) obj).val;
    }
}


