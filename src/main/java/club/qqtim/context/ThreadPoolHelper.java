package club.qqtim.context;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolHelper {
    public static ThreadPoolExecutor threadPoolExecutor;
    private static final int WORK_QUEUE_CAPACITY = 50;
    private static final int MAX_THREAD_NUM = 16;
    private static final int INIT_THREAD_NUM = 4;
    private static final int KEEP_ALIVE_TIME_MINUTES = 60;

    static {
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(WORK_QUEUE_CAPACITY);
        threadPoolExecutor = new ThreadPoolExecutor(
                INIT_THREAD_NUM, MAX_THREAD_NUM, KEEP_ALIVE_TIME_MINUTES, TimeUnit.MINUTES, workQueue);
    }

    private ThreadPoolHelper() {
    }

}