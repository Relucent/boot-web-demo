package yyl.demo.component;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import yyl.demo.properties.ExecutorProperties;

/**
 * 统一线程池管理器
 */
@Slf4j
@Component
public class BizExecutorManager {

    private final ExecutorProperties properties;
    private final ConcurrentHashMap<String, ThreadPoolExecutor> executors = new ConcurrentHashMap<>();

    public BizExecutorManager(ExecutorProperties properties) {
        this.properties = properties;
    }

    public ExecutorService getExecutor(String name) {
        return executors.computeIfAbsent(name, n -> createExecutor(n));
    }

    private ThreadPoolExecutor createExecutor(String name) {

        ExecutorProperties.ExecutorItem cfg = properties.getExecutors().get(name);

        if (cfg == null) {
            throw new IllegalArgumentException("No executor config found for name: " + name);
        }
        int core = cfg.getOrDefaultCore();
        int max = cfg.getOrDefaultMax();
        int queue = cfg.getOrDefaultQueue();

        log.info("[Executor] Creating pool: {}, core={}, max={}, queue={}", name, core, max, queue);

        return new ThreadPoolExecutor(
                // // 核心线程池大小，表示线程池常驻线程数量
                core,
                // 最大线程数，表示线程池最多创建的线程数量
                max,
                // 保活时间，表示一个非核心线程多久没有使用，会被回收
                60, TimeUnit.SECONDS,
                // 阻塞队列，表示队列最多缓存多少任务，如果队列满了，将触发 RejectedExecutionHandler
                new ArrayBlockingQueue<>(queue),
                // 线程创建方法
                createNamedThreadFactory(name),
                // 拒绝策略，当阻塞队列满了之后，会触发这里的handler
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @PreDestroy
    public void shutdown() {
        log.info("[Executor] Start shutting down thread pools...");
        List<CompletableFuture<Void>> futures = executors.entrySet()//
                .stream()//
                .filter(e -> !e.getValue().isShutdown())//
                .map(e -> CompletableFuture.runAsync(() -> {
                    String name = e.getKey();
                    ExecutorService executor = e.getValue();
                    log.info("[Executor] Shutting down pool: {}", name);
                    executor.shutdown();
                    try {
                        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                            log.warn("[Executor] Timeout waiting for {}. Forcing shutdown...", name);
                            executor.shutdownNow();

                            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                                log.error("[Executor] Pool {} did not terminate!", name);
                            }
                        }
                    } catch (InterruptedException ex) {
                        log.error("[Executor] Shutdown interrupted for {}", name);
                        executor.shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                    log.info("[Executor] Shutdown complete: {}", name);
                }))//
                .collect(Collectors.toList());//
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("[Executor] All executors shut down.");
    }

    static ThreadFactory createNamedThreadFactory(final String prefix) {
        final AtomicLong count = new AtomicLong(1);
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, prefix + "-thread-" + count.getAndIncrement());
            }
        };
    }

}
