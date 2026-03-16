package com.lesson.concurrency.util;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for common thread operations and helpers.
 * 
 * Provides convenience methods for thread management, sleeping, and synchronization
 * patterns that are commonly used across exercises.
 */
public class ThreadUtils {

    private ThreadUtils() {
        // Utility class - not instantiable
    }

    /**
     * Sleep for a specified duration in milliseconds.
     * 
     * Wraps Thread.sleep() and converts InterruptedException to RuntimeException
     * for cleaner exception handling in exercises.
     *
     * @param millis Duration to sleep in milliseconds
     * @throws RuntimeException if the thread is interrupted during sleep
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted during sleep", e);
        }
    }

    /**
     * Sleep for a specified duration in a given TimeUnit.
     *
     * @param duration The duration value
     * @param unit The TimeUnit for the duration
     * @throws RuntimeException if the thread is interrupted during sleep
     */
    public static void sleep(long duration, TimeUnit unit) {
        sleep(unit.toMillis(duration));
    }

    /**
     * Get the name of the current thread for logging purposes.
     *
     * @return Thread name in format "ThreadName (ID: threadId)"
     */
    public static String getThreadName() {
        Thread current = Thread.currentThread();
        return String.format("%s (ID: %d)", current.getName(), current.threadId());
    }

    /**
     * Print a log message with thread context.
     *
     * @param message The message to log
     */
    public static void log(String message) {
        System.out.printf("[%s] %s%n", getThreadName(), message);
    }

    /**
     * Print a formatted log message with thread context.
     *
     * @param format The format string
     * @param args Format arguments
     */
    public static void logf(String format, Object... args) {
        System.out.printf("[%s] %s%n", getThreadName(), String.format(format, args));
    }

    /**
     * Wait for a specific number of threads to reach a given count.
     * Useful for synchronization in tests.
     *
     * @param currentCount Current thread count
     * @param expectedCount Expected thread count
     * @param timeout Maximum time to wait in milliseconds
     * @return true if the count reached the expected value, false if timeout occurred
     */
    public static boolean waitForThreadCount(long timeout) {
        long startTime = System.currentTimeMillis();
        int threadCount = Thread.activeCount();
        
        while (System.currentTimeMillis() - startTime < timeout) {
            int newCount = Thread.activeCount();
            if (newCount != threadCount) {
                return true;
            }
            sleep(10);
        }
        return false;
    }

    /**
     * Create a new thread with a descriptive name.
     *
     * @param name Thread name
     * @param runnable The task to run
     * @return A new Thread instance
     */
    public static Thread createThread(String name, Runnable runnable) {
        Thread thread = new Thread(runnable, name);
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.err.printf("Uncaught exception in thread %s: %s%n", t.getName(), e.getMessage());
            e.printStackTrace();
        });
        return thread;
    }

    /**
     * Create and start a new thread with a descriptive name.
     *
     * @param name Thread name
     * @param runnable The task to run
     * @return A started Thread instance
     */
    public static Thread startThread(String name, Runnable runnable) {
        Thread thread = createThread(name, runnable);
        thread.start();
        return thread;
    }
}
