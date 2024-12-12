package interpreter.toylanguageinterpreter.Model.Value;

import interpreter.toylanguageinterpreter.Model.Type.Type;

public interface Value {
    Type getType();
    Value deepCopy();
}
