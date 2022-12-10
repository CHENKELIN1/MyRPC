package com.ckl.rpc.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池工厂
 */
@Slf4j
public class ThreadPoolFactory {
    //    核心线程数
    private static final int CORE_POOL_SIZE = 10;
    //    最大线程数
    private static final int MAXIMUM_POOL_SIZE_SIZE = 100;
    //    存活时间
    private static final int KEEP_ALIVE_TIME = 1;
    //    阻塞队列容量
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    //    线程池map
    private static Map<String, ExecutorService> threadPollsMap = new ConcurrentHashMap<>();

    private ThreadPoolFactory() {
    }

    /**
     * 创建默认线程池
     *
     * @param threadNamePrefix 线程名前缀
     * @return ExecutorService 线程线程池
     */
    public static ExecutorService createDefaultThreadPool(String threadNamePrefix) {
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    /**
     * 创建默认线程池
     *
     * @param threadNamePrefix 线程名前缀
     * @param daemon           是否是守护线程
     * @return ExecutorService 线程池
     */
    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean daemon) {
//        若有则返回线程池，若没有，则创建一个线程池
        ExecutorService pool = threadPollsMap.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix, daemon));
//        线程池终止
        if (pool.isShutdown() || pool.isTerminated()) {
//            重置线程池
            threadPollsMap.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix, daemon);
            threadPollsMap.put(threadNamePrefix, pool);
        }
        return pool;

    }

    /**
     * 关闭所有线程池
     */
    public static void shutDownAll() {
        log.info("关闭所有线程池...");
//        遍历map
        threadPollsMap.entrySet().parallelStream().forEach(entry -> {
//            获取线程池
            ExecutorService executorService = entry.getValue();
//            关闭线程池，不再接收新任务，但会执行完当前任务
            executorService.shutdown();
            log.info("关闭线程池 [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try {
//                等待线程池内任务执行完
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                log.error("关闭线程池失败！");
//                停止所有任务
                executorService.shutdownNow();
            }
        });
    }

    /**
     * 创建线程池
     *
     * @param threadNamePrefix 线程池名前缀
     * @param daemon           是否是守护线程
     * @return 线程池
     */
    private static ExecutorService createThreadPool(String threadNamePrefix, Boolean daemon) {
//        阻塞队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
//        线程池工厂
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }


    /**
     * 创建 ThreadFactory 。如果threadNamePrefix不为空则使用自建ThreadFactory，否则使用defaultThreadFactory
     *
     * @param threadNamePrefix 作为创建的线程名字的前缀
     * @param daemon           指定是否为 Daemon Thread(守护线程)
     * @return ThreadFactory
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }

        return Executors.defaultThreadFactory();
    }
}
