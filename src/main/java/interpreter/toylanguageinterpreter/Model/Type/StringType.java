package interpreter.toylanguageinterpreter.Model.Type;

import interpreter.toylanguageinterpreter.Model.Value.StringValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;

public class StringType implements Type {
    public boolean equals(Object obj){
        return obj instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public Type deepCopy() {
        return new StringType();
    }

    @Override
    public Value defaultValue() {
        return new StringValue("");
    }
}
