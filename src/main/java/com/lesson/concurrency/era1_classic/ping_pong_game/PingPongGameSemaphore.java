package com.lesson.concurrency.era1_classic.monitor;

import java.util.concurrent.Semaphore;

/**
 * Exercise: The Strict Turn-Taker (Ping-Pong) using Semaphore
 *
 * Problem:
 * Create two threads, "Ping" and "Pong", that must print their names alternately
 * using Semaphore for synchronization. The output should be: Ping, Pong, Ping, Pong, ...
 *
 * Key Concepts:
 * - Semaphore as synchronization primitive
 * - Permits (initial count)
 * - acquire() blocks until permit available
 * - release() gives permit to waiting thread
 * - Cleaner than wait()/notify() for simple turn-taking
 *
 * Advantages over wait()/notify():
 * - No explicit lock needed
 * - No while loops for spurious wakeups
 * - Simpler logic for turn-based coordination
 * - Harder to misuse
 *
 * Reference: java_concurrency_exercises.md (Section 1, Monitor Pattern Alternatives)
 *
 * Difficulty: ⭐ (Beginner - simpler than wait/notify)
 * Estimated Time: 1-2 hours
 *
 * APIs: Semaphore, acquire(), release()
 */
public class PingPongGameSemaphore {

    // Semaphore for Ping thread (starts with 1 permit - Ping goes first)
    private final Semaphore pingTurn = new Semaphore(1);

    // Semaphore for Pong thread (starts with 0 permits - Pong waits initially)
    private final Semaphore pongTurn = new Semaphore(0);

    /**
     * Generic method to handle turn-based coordination using Semaphore.
     * Much simpler than wait/notify because:
     * - No explicit locking needed
     * - No condition checking with while loops
     * - Semaphore handles all the coordination
     *
     * @param myTurn The semaphore this thread acquires (waits for permit)
     * @param otherTurn The semaphore this thread releases (gives permit to other)
     * @param action The action to execute (work to do)
     * @param count How many times to repeat
     */
    private void doTurnBasedWork(Semaphore myTurn, Semaphore otherTurn,
                                  Runnable action, int count) {
        try {
            for (int i = 0; i < count; i++) {
                // ACQUIRE: Wait until my turn (blocks if no permit available)
                myTurn.acquire();

                // DO WORK: Execute the action
                action.run();

                // RELEASE: Give turn to other thread
                otherTurn.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Ping thread execution.
     * Waits for pingTurn semaphore, prints "Ping", releases pongTurn.
     *
     * @param count Number of times to print "Ping"
     */
    public void ping(int count) {
        doTurnBasedWork(pingTurn, pongTurn,
                       () -> System.out.print("Ping"),
                       count);
    }

    /**
     * Pong thread execution.
     * Waits for pongTurn semaphore, prints "Pong", releases pingTurn.
     *
     * @param count Number of times to print "Pong"
     */
    public void pong(int count) {
        doTurnBasedWork(pongTurn, pingTurn,
                       () -> System.out.print("Pong"),
                       count);
    }

    /**
     * Main method to demonstrate the Semaphore-based Ping-Pong game.
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        PingPongGameSemaphore game = new PingPongGameSemaphore();
        int iterations = 10;

        // Create and start Ping thread
        Thread pingThread = new Thread(() -> {
            game.ping(iterations);
        }, "PingThread");

        // Create and start Pong thread
        Thread pongThread = new Thread(() -> {
            game.pong(iterations);
        }, "PongThread");

        pingThread.start();
        pongThread.start();

        // Wait for both threads to complete
        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread was interrupted");
        }

        System.out.println("\n\nGame finished!");
    }

    /**
     * Get ping semaphore (for testing).
     *
     * @return The ping turn semaphore
     */
    public Semaphore getPingTurn() {
        return pingTurn;
    }

    /**
     * Get pong semaphore (for testing).
     *
     * @return The pong turn semaphore
     */
    public Semaphore getPongTurn() {
        return pongTurn;
    }
}
