package com.sunsun.myapplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by dexoam on 6/16/16.
 */
public class ThreadPoolUtils {

    private static volatile ThreadPoolUtils mScheduledExecutor;

    private static final int CORE_POOL_SIZE = 4;

    private ScheduledExecutorService scheduledExecutorService;

    private ThreadPoolUtils() {
        scheduledExecutorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    }


    public synchronized void execute(Runnable runnable) {
        scheduledExecutorService.execute(runnable);
    }

    public synchronized static ThreadPoolUtils getInstance() {
        if (mScheduledExecutor == null) {
            mScheduledExecutor = new ThreadPoolUtils();
        }
        return mScheduledExecutor;
    }

}
