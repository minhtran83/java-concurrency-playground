package com.lesson.concurrency.era1_classic.monitor;

import com.example.concurrency.util.ThreadUtils;

/**
 * Exercise: The Strict Turn-Taker (Ping-Pong)
 *
 * Problem:
 * Create two threads, "Ping" and "Pong", that must print their names alternately
 * using wait() and notify(). The output should be: Ping, Pong, Ping, Pong, ...
 *
 * Key Concepts:
 * - Monitor locks (synchronized keyword)
 * - wait()/notify() for inter-thread signaling
 * - Spurious wakeup handling (use while, not if)
 * - Strict alternation using boolean flags
 *
 * Reference: java_concurrency_exercises.md (Section 1, Monitor Pattern)
 *
 * Difficulty: ⭐ (Beginner)
 * Estimated Time: 1-2 hours
 *
 * APIs: synchronized, wait(), notify(), notifyAll()
 */
public class PingPongGame {

    private final Object lock = new Object();
    private boolean pingTurn = true;
    private volatile boolean running = true;

    /**
     * Ping thread - prints "Ping" when it's its turn.
     * Must wait if it's not ping's turn.
     */
    public void ping(int count) {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {
                // IMPORTANT: Use while, not if, to handle spurious wakeups
                while (!pingTurn && running) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (!running) break;

                System.out.print("Ping");
                if (i < count - 1) System.out.print(", ");

                // Switch turn to Pong
                pingTurn = false;
                lock.notifyAll();
            }
        }
    }

    /**
     * Pong thread - prints "Pong" when it's its turn.
     * Must wait if it's not pong's turn.
     */
    public void pong(int count) {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {
                // IMPORTANT: Use while, not if, to handle spurious wakeups
                while (pingTurn && running) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                if (!running) break;

                System.out.print("Pong");
                if (i < count - 1) System.out.print(", ");

                // Switch turn to Ping
                pingTurn = true;
                lock.notifyAll();
            }
        }
    }

    /**
     * Stop the game and wake up any waiting threads.
     */
    public void stop() {
        synchronized (lock) {
            running = false;
            lock.notifyAll();
        }
    }

    /**
     * Main method to demonstrate the exercise.
     *
     */
    static void main() {
        PingPongGame game = new PingPongGame();
        int iterations = 10;

        // Create and start Ping thread
        Thread pingThread = ThreadUtils.startThread("Ping-Thread", () -> {
            game.ping(iterations);
        });

        // Create and start Pong thread
        Thread pongThread = ThreadUtils.startThread("Pong-Thread", () -> {
            game.pong(iterations);
        });

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
     * Get the current turn status (for testing purposes).
     *
     * @return true if it's Ping's turn, false if it's Pong's turn
     */
    public boolean isPingTurn() {
        synchronized (lock) {
            return pingTurn;
        }
    }

    /**
     * Check if the game is still running.
     *
     * @return true if the game is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
}
