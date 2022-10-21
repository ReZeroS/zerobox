package club.qqtim.atomic;

import sun.misc.Unsafe;

public class AtomicInt {


    private static final Unsafe unsafe = Unsafe.getUnsafe();

    private volatile int value;

    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(
                    AtomicInt.class.getDeclaredField("value"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public AtomicInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int incrementAndGet(){
        int val;
        do {
            // important： now val is in stack and won't change
            val = unsafe.getIntVolatile(this, valueOffset);
            // val + 1 is in stack local variable, seems like an atomic operation () -> val + 1
        } while (unsafe.compareAndSwapInt(this, valueOffset, val, val + 1));
        return val + 1;
    }




}
