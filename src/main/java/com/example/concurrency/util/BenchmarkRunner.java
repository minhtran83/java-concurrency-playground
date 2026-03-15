package com.example.concurrency.util;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for benchmarking and performance measurement.
 * 
 * Provides simple timing utilities for performance analysis without requiring JMH setup.
 * For comprehensive benchmarking, use JMH in the benchmarks module.
 */
public class BenchmarkRunner {

    private BenchmarkRunner() {
        // Utility class - not instantiable
    }

    /**
     * Measure the execution time of a single operation.
     *
     * @param operation The operation to time
     * @return Execution time in nanoseconds
     */
    public static long time(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    /**
     * Measure the average execution time over multiple iterations.
     *
     * @param iterations Number of iterations to run
     * @param operation The operation to time
     * @return Average execution time in nanoseconds
     */
    public static long averageTime(int iterations, Runnable operation) {
        long totalTime = 0;
        for (int i = 0; i < iterations; i++) {
            totalTime += time(operation);
        }
        return totalTime / iterations;
    }

    /**
     * Measure execution time and convert to a specific TimeUnit.
     *
     * @param operation The operation to time
     * @param unit The TimeUnit to convert to
     * @return Execution time in the specified unit
     */
    public static long time(Runnable operation, TimeUnit unit) {
        long nanos = time(operation);
        return unit.convert(nanos, TimeUnit.NANOSECONDS);
    }

    /**
     * Benchmark a concurrent operation with multiple threads.
     *
     * @param numThreads Number of concurrent threads
     * @param operationPerThread Operation each thread executes
     * @return Execution time in nanoseconds
     */
    public static long concurrentTime(int numThreads, Runnable operationPerThread) {
        Thread[] threads = new Thread[numThreads];
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < numThreads; i++) {
            threads[i] = ThreadUtils.createThread("Worker-" + i, operationPerThread);
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for thread completion", e);
            }
        }
        
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    /**
     * Print benchmark results in a readable format.
     *
     * @param operationName Name of the operation
     * @param timeNanos Time in nanoseconds
     */
    public static void printResult(String operationName, long timeNanos) {
        System.out.printf("%-40s: %12d ns (%8.3f ms)%n", 
            operationName, timeNanos, timeNanos / 1_000_000.0);
    }

    /**
     * Compare performance of two operations.
     *
     * @param name1 Name of first operation
     * @param operation1 First operation
     * @param name2 Name of second operation
     * @param operation2 Second operation
     * @param iterations Number of iterations for each
     */
    public static void compare(String name1, Runnable operation1, 
                               String name2, Runnable operation2, 
                               int iterations) {
        long time1 = averageTime(iterations, operation1);
        long time2 = averageTime(iterations, operation2);
        
        System.out.println("\n=== Performance Comparison ===");
        printResult(name1, time1);
        printResult(name2, time2);
        
        double ratio = (double) time1 / time2;
        String faster = ratio > 1 ? name2 : name1;
        System.out.printf("\n%s is %.2fx faster%n", faster, Math.max(ratio, 1/ratio));
    }

    /**
     * Warm up the JVM before benchmarking.
     * 
     * Executes the operation multiple times to allow JIT compilation.
     *
     * @param operation The operation to warm up
     * @param iterations Number of warmup iterations
     */
    public static void warmup(Runnable operation, int iterations) {
        for (int i = 0; i < iterations; i++) {
            operation.run();
        }
    }
}
