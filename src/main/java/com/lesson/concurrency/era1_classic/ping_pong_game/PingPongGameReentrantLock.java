package com.lesson.concurrency.era1_classic.ping_pong_game;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Exercise: The Strict Turn-Taker (Ping-Pong) using ReentrantLock
 *
 * Problem:
 * Create two threads, "Ping" and "Pong", that must print their names alternately
 * using ReentrantLock and Condition variables.
 *
 * Key Concepts:
 * - Explicit locking with ReentrantLock (vs synchronized)
 * - Condition variables (await/signal vs wait/notify)
 * - Try-finally block for guaranteed unlocking
 * - Explicit control over fairness
 *
 * Advantages over synchronized:
 * - More explicit API (lock.lock(), condition.await())
 * - Can have multiple Condition variables per lock (e.g. notEmpty, notFull)
 * - Supports timed waits and interruptible lock acquisition
 * - Can check if lock is held (lock.isLocked())
 *
 * Reference: java_concurrency_exercises.md (Section 1, Monitor Pattern Alternatives)
 *
 * Difficulty: ⭐⭐ (Intermediate)
 * Estimated Time: 1-2 hours
 *
 * APIs: ReentrantLock, Condition, await(), signalAll()
 */
public class PingPongGameReentrantLock {

    // Explicit lock replaces synchronized(obj)
    private final ReentrantLock lock = new ReentrantLock();

    // Condition replaces obj.wait() / obj.notify()
    private final Condition turnCondition = lock.newCondition();

    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;

    /**
     * Generic method to handle turn-based coordination using ReentrantLock.
     *
     * @param myTurn The turn this thread should execute on
     * @param nextTurn The turn to hand off to
     * @param action The action to execute (work to do)
     * @param count How many times to repeat
     */
    private void doTurnBasedWork(Turn myTurn, Turn nextTurn,
                                  Runnable action, int count) {
        try {
            for (int i = 0; i < count; i++) {
                lock.lock();  // Explicitly acquire lock
                try {
                    // Wait for my turn
                    while (turn != myTurn) {
                        turnCondition.await();  // Replaces lock.wait()
                    }

                    // Do the work
                    action.run();

                    // Pass the turn
                    turn = nextTurn;
                    turnCondition.signalAll();  // Replaces lock.notifyAll()

                } finally {
                    lock.unlock();  // CRITICAL: Always unlock in finally!
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void ping(int count) {
        doTurnBasedWork(Turn.PING, Turn.PONG,
                       () -> System.out.println("Ping"),
                       count);
    }

    public void pong(int count) {
        doTurnBasedWork(Turn.PONG, Turn.PING,
                       () -> System.out.println("Pong"),
                       count);
    }

    /**
     * Main method to demonstrate the ReentrantLock-based Ping-Pong game.
     */
    public static void main(String[] args) {
        PingPongGameReentrantLock game = new PingPongGameReentrantLock();
        int iterations = 10;

        Thread pingThread = new Thread(() -> game.ping(iterations), "PingThread");
        Thread pongThread = new Thread(() -> game.pong(iterations), "PongThread");

        pingThread.start();
        pongThread.start();

        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n\nGame finished!");
    }
}
