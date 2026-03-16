package com.lesson.concurrency.era1_classic.ping_pong_game;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Exercise: Ping-Pong using CyclicBarrier
 *
 * Challenge: CyclicBarrier is designed for "All threads arrive at point X",
 * not "Thread A then Thread B".
 *
 * Solution: Use TWO barriers to simulate a relay race.
 * 1. Barrier 1: "Ping is done"
 * 2. Barrier 2: "Pong is done"
 */
public class PingPongGameCyclicBarrier {

    // Barrier 1: Wait for Ping to print
    private final CyclicBarrier afterPing = new CyclicBarrier(2);

    // Barrier 2: Wait for Pong to print
    private final CyclicBarrier afterPong = new CyclicBarrier(2);

    public void play(int count) {
        Thread pingThread = new Thread(() -> {
            try {
                for (int i = 0; i < count; i++) {
                    System.out.println("Ping");
                    afterPing.await(); // Signal Ping done
                    afterPong.await(); // Wait for Pong done
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread pongThread = new Thread(() -> {
            try {
                for (int i = 0; i < count; i++) {
                    afterPing.await(); // Wait for Ping done
                    System.out.println("Pong");
                    afterPong.await(); // Signal Pong done
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        });

        pingThread.start();
        pongThread.start();

        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Game over");
    }

    public static void main(String[] args) {
        new PingPongGameCyclicBarrier().play(10);
    }
}
