package interpreter.toylanguageinterpreter.Model.Type;

import interpreter.toylanguageinterpreter.Model.Value.RefValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;


public class RefType implements Type {
    private Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    @Override
    public Type deepCopy() {
        return new RefType(inner.deepCopy());
    }


    @Override
    public Value defaultValue() {
        return new RefValue(0,inner);
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof RefType)
            return inner.equals(((RefType) other).getInner());
        else
            return false;
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }
}
