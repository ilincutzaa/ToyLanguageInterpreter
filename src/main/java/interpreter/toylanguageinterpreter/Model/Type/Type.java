package interpreter.toylanguageinterpreter.Model.Type;

import interpreter.toylanguageinterpreter.Model.Value.Value;

public interface Type {
    Type deepCopy();
    Value defaultValue();
}

