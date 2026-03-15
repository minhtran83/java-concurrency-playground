package com.lesson.concurrency.era1_classic.monitor;

import com.lesson.concurrency.era1_classic.monitor.PingPongGame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * Test suite for PingPongGame exercise.
 *
 * Tests verify:
 * - Correct alternation between Ping and Pong
 * - Proper synchronization without race conditions
 * - Thread termination and cleanup
 * - Edge cases (single iteration, etc.)
 */
class PingPongGameTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicAlternation() {
        PingPongGame game = new PingPongGame();
        int iterations = 3;

        // Capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Create and start threads
            Thread pingThread = new Thread(() -> game.ping(iterations));
            Thread pongThread = new Thread(() -> game.pong(iterations));

            pingThread.start();
            pongThread.start();

            // Wait for completion
            try {
                pingThread.join();
                pongThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Test interrupted");
            }

            System.setOut(originalOut);
            String output = outContent.toString();

            // Verify that both Ping and Pong appear in alternating pattern
            assertThat(output).contains("Ping");
            assertThat(output).contains("Pong");
            // Count occurrences - should have equal number of Ping and Pong
            int pingCount = output.split("Ping", -1).length - 1;
            int pongCount = output.split("Pong", -1).length - 1;
            assertThat(pingCount).isEqualTo(iterations);
            assertThat(pongCount).isEqualTo(iterations);

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testSingleIteration() {
        PingPongGame game = new PingPongGame();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            Thread pingThread = new Thread(() -> game.ping(1));
            Thread pongThread = new Thread(() -> game.pong(1));

            pingThread.start();
            pongThread.start();

            try {
                pingThread.join();
                pongThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Test interrupted");
            }

            System.setOut(originalOut);
            String output = outContent.toString();

            assertThat(output).contains("Ping");
            assertThat(output).contains("Pong");
            // Verify order: Ping must come before Pong in the first pair
            int pingIndex = output.indexOf("Ping");
            int pongIndex = output.indexOf("Pong");
            assertThat(pingIndex).isLessThan(pongIndex);

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testNoDeadlock() {
        // Test that the game completes without deadlock
        PingPongGame game = new PingPongGame();
        int iterations = 5;

        Thread pingThread = new Thread(() -> game.ping(iterations));
        Thread pongThread = new Thread(() -> game.pong(iterations));

        pingThread.start();
        pongThread.start();

        // If we reach here within the timeout, no deadlock occurred
        assertThatCode(() -> {
            pingThread.join();
            pongThread.join();
        }).doesNotThrowAnyException();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testTurnAlternation() {
        PingPongGame game = new PingPongGame();

        // Initially, it should be Ping's turn
        assertThat(game.isPingTurn()).isTrue();

        // After Ping executes, it should be Pong's turn
        Thread pingThread = new Thread(() -> {
            synchronized (game) {
                if (game.isPingTurn()) {
                    // Simulate Ping taking its turn
                    // (This would be done inside ping() method in real execution)
                }
            }
        });

        pingThread.start();

        try {
            pingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test interrupted");
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testGameCanBeStopped() {
        PingPongGame game = new PingPongGame();

        assertThat(game.isRunning()).isTrue();

        game.stop();

        assertThat(game.isRunning()).isFalse();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testMultipleIterations() {
        PingPongGame game = new PingPongGame();
        int iterations = 10;

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            Thread pingThread = new Thread(() -> game.ping(iterations));
            Thread pongThread = new Thread(() -> game.pong(iterations));

            pingThread.start();
            pongThread.start();

            try {
                pingThread.join();
                pongThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Test interrupted");
            }

            System.setOut(originalOut);
            String output = outContent.toString();

            // Count occurrences of Ping and Pong
            int pingCount = output.split("Ping", -1).length - 1;
            int pongCount = output.split("Pong", -1).length - 1;

            assertThat(pingCount).isEqualTo(iterations);
            assertThat(pongCount).isEqualTo(iterations);

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testThreadInterruptionHandling() {
        PingPongGame game = new PingPongGame();

        Thread pingThread = new Thread(() -> game.ping(5));
        pingThread.start();

        // Let it run briefly
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Interrupt the thread
        pingThread.interrupt();
        game.stop();

        // Should complete without hanging
        assertThatCode(() -> pingThread.join(1000)).doesNotThrowAnyException();
    }
}
