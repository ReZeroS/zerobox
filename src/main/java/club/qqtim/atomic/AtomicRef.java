package club.qqtim.atomic;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AtomicRef <V> {

    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (AtomicReference.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private volatile V value;


    public AtomicRef(V value) {
        this.value = value;
    }


    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }



    public final boolean compareAndUpdate(V expect, V update) {
        return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
    }


    public final V getAndUpdate(Function<V, V> updateFunction) {
        V prev, next;
        do {
            prev = getValue();
            next = updateFunction.apply(prev);
        } while (!compareAndUpdate(prev, next));
        return prev;
    }













}

