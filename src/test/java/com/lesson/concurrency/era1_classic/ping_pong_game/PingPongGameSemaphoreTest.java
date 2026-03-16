package com.lesson.concurrency.era1_classic.monitor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * Test suite for PingPongGameSemaphore.
 *
 * Tests verify:
 * - Correct alternation between Ping and Pong
 * - Proper synchronization without race conditions
 * - Thread termination and cleanup
 * - Edge cases (single iteration, many iterations)
 * - No deadlocks under stress (125+ iterations)
 */
class PingPongGameSemaphoreTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBasicAlternation() {
        PingPongGameSemaphore game = new PingPongGameSemaphore();
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

            // Verify alternation: should be Ping, Pong, Ping, Pong, Ping, Pong
            int pingCount = output.split("Ping", -1).length - 1;
            int pongCount = output.split("Pong", -1).length - 1;

            assertThat(pingCount).isEqualTo(iterations);
            assertThat(pongCount).isEqualTo(iterations);

            // Verify order: Ping should appear first
            assertThat(output.indexOf("Ping")).isLessThan(output.indexOf("Pong"));

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testSingleIteration() {
        PingPongGameSemaphore game = new PingPongGameSemaphore();

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
            // Verify order: Ping must come before Pong
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
        PingPongGameSemaphore game = new PingPongGameSemaphore();
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
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testStressTest_125Iterations() {
        // Stress test to ensure no race conditions or deadlocks
        // (This would deadlock with improper wait/notify implementation)
        PingPongGameSemaphore game = new PingPongGameSemaphore();
        int iterations = 125;

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

            // Count occurrences
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
    void testMultipleIterations() {
        PingPongGameSemaphore game = new PingPongGameSemaphore();
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
    void testSemaphoreState() {
        PingPongGameSemaphore game = new PingPongGameSemaphore();

        // Initially, ping should have 1 permit (available)
        assertThat(game.getPingTurn().availablePermits()).isEqualTo(1);

        // Initially, pong should have 0 permits (must wait)
        assertThat(game.getPongTurn().availablePermits()).isEqualTo(0);
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testThreadInterruptionHandling() {
        PingPongGameSemaphore game = new PingPongGameSemaphore();

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

        // Should complete without hanging
        assertThatCode(() -> pingThread.join(1000)).doesNotThrowAnyException();
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testCorrectSequence() {
        PingPongGameSemaphore game = new PingPongGameSemaphore();
        int iterations = 5;

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

            // Verify the exact sequence for 5 iterations
            // Should be: PingPongPingPongPingPongPingPongPingPong
            String expected = "PingPongPingPongPingPongPingPongPingPong";
            assertThat(output).isEqualTo(expected);

        } finally {
            System.setOut(originalOut);
        }
    }
}
