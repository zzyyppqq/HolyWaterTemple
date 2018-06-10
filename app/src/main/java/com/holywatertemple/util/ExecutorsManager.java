package com.holywatertemple.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhangyipeng on 2017/5/25.
 */

public class ExecutorsManager {

    private static ExecutorsManager executorsManager;
    private ExecutorService singleThreadExecutor;
    private ExecutorService cachedThreadPool;
    private ScheduledExecutorService singleThreadScheduledExecutor;
    private ExecutorService fixedThreadPool;


    /**
     * 定义FixedThreadPool 线程池数量为AsyncTask默认线程池数量的一半
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = (CPU_COUNT + 1) / 2;
    private static final int MAXIMUM_POOL_SIZE = (CPU_COUNT * 2 + 1) / 2;
    private static final int KEEP_ALIVE = 1;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
            return new Thread(r, "YCExecutorsManager FixedThreadPool #" + mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    private ThreadPoolOverflowHandler mDefaultThreadPoolOverflowHandler = new ThreadPoolOverflowHandler();

    private CacheThreadPoolOverflowHandler mDefaultTCachehreadPoolOverflowHandler = new CacheThreadPoolOverflowHandler();

    private static class ThreadPoolOverflowHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            ExecutorsManager.getInstance().getCachedThreadPool().execute(r);
        }
    }

    private static class CacheThreadPoolOverflowHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if(null != r)
                new Thread(r, "CacheCPRejectHandlerThread").start();
        }
    }


    public static ExecutorsManager getInstance() {
        if (executorsManager == null) {
            synchronized (ExecutorsManager.class) {
                if (executorsManager == null)
                    executorsManager = new ExecutorsManager();
            }
        }
        return executorsManager;
    }

    public ExecutorService getSingleThreadExecutor() {
        if (singleThreadExecutor == null) {
            synchronized (ExecutorsManager.class) {
                if (singleThreadExecutor == null) {
                    singleThreadExecutor = Executors.newSingleThreadExecutor();
                }
            }
        }
        return singleThreadExecutor;
    }

    public ScheduledExecutorService getSingleThreadScheduledExecutor() {
        if (singleThreadScheduledExecutor == null) {
            synchronized (ExecutorsManager.class) {
                if (singleThreadScheduledExecutor == null) {
                    singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
                }
            }
        }
        return singleThreadScheduledExecutor;
    }

    /**
     * when task is user sensitive or can't be blocked by limited number of threads, uses this executor
     * note this executor can lead to heavier memory consumption<br/>
     * <br/><b>note: this Executor must not be shutdown</b>
     * @return ExecutorService
     */
    public ExecutorService getCachedThreadPool() {
        if (cachedThreadPool == null) {
            synchronized (ExecutorsManager.class) {
                if (cachedThreadPool == null) {
                    cachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                            60L, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>(), mDefaultTCachehreadPoolOverflowHandler);
                }
            }
        }
        return cachedThreadPool;
    }

    /**
     * when task's not user sensitive and can be blocked by limited number of threads running, uses this executor
     * <br/>
     * <br/><b>note: this Executor must not be shutdown</b>
     * @return ExecutorService
     */
    public ExecutorService getFixedThreadPool() {
        if (fixedThreadPool == null) {
            synchronized (ExecutorsManager.class) {
                if (fixedThreadPool == null) {
                    fixedThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, mDefaultThreadPoolOverflowHandler);
                }
            }
        }
        return fixedThreadPool;
    }

    /**
     * 销毁所有公用线程池
     */
    public void destoryAll() {
        synchronized (ExecutorsManager.class) {
            if(null != fixedThreadPool) {
                fixedThreadPool.shutdown();
                fixedThreadPool = null;
            }
            if(null != cachedThreadPool) {
                cachedThreadPool.shutdown();
                cachedThreadPool = null;
            }
            if(null != singleThreadExecutor) {
                singleThreadExecutor.shutdown();
                singleThreadExecutor = null;
            }
            if(null != singleThreadScheduledExecutor) {
                singleThreadScheduledExecutor.shutdown();
                singleThreadScheduledExecutor = null;
            }
        }
    }
}
