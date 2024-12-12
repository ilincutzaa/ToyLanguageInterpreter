package interpreter.toylanguageinterpreter.Model.Value;

import interpreter.toylanguageinterpreter.Model.Type.StringType;
import interpreter.toylanguageinterpreter.Model.Type.Type;

public class StringValue implements Value {
    private String val;
    public StringValue(String v) {
        val = v;
    }
    public String getValue() {
        return val;
    }
    public void setValue(String v) {
        val = v;
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public Value deepCopy() {
        return new StringValue(val);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringValue && val.equals(((StringValue) obj).val);
    }
}
