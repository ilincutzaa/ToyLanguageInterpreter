package interpreter.toylanguageinterpreter.Utils;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerKey {
    private final AtomicInteger value;

    public AtomicIntegerKey(int value) {
        this.value = new AtomicInteger(value);
    }

    public int getAndIncrement() {
        return value.getAndIncrement();
    }

    public void set(int value) {
        this.value.set(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AtomicIntegerKey) {
            return this.value.get() == ((AtomicIntegerKey) obj).value.get();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value.get());
    }


    public int get() {
        return value.get();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}