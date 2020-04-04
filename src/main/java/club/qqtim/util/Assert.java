package club.qqtim.util;

/**
 * @Author: ReZero
 * @Date: 3/17/19 8:05 PM
 * @Version 1.0
 */
public abstract class Assert {
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
