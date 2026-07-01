package com.game.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池 九 零一起 玩www.901 75.com
 * @Auther: gouzhongliang
 * @Date: 2021/12/23 14:20
 */
public class ThreadPoolManager {

    private ThreadPoolManager(){}

    private ScheduledThreadPoolExecutor executorService;

    private BlockingQueue<Runnable> queue;

    private static ThreadPoolManager manager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return manager;
    }

    public void init(int corePoolSize) {
        executorService = new ScheduledThreadPoolExecutor(corePoolSize);
        queue = executorService.getQueue();
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }
}
