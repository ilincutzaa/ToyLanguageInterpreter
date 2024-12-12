package interpreter.toylanguageinterpreter.Model.Type;

import interpreter.toylanguageinterpreter.Model.Value.BoolValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;

public class BoolType implements Type {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public Type deepCopy() {
        return new BoolType();
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }
}
