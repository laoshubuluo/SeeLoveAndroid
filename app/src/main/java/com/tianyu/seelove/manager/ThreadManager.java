package com.tianyu.seelove.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
    private Executor exec = new ThreadPoolExecutor(15, 200, 10,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    private static ThreadManager manager = new ThreadManager();

    private ThreadManager() {
    }

    public static ThreadManager getInstance() {
        return manager;
    }

    public Executor getThreadPool() {
        return exec;
    }
}
