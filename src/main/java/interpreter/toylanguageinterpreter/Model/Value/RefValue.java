package interpreter.toylanguageinterpreter.Model.Value;

import interpreter.toylanguageinterpreter.Model.Type.RefType;
import interpreter.toylanguageinterpreter.Model.Type.Type;

public class RefValue implements Value {
    private int address;
    private Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public Value deepCopy() {
        return new RefValue(address, locationType);
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "RefValue{" + "address=" + address + ", locationType=" + locationType + '}';
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
