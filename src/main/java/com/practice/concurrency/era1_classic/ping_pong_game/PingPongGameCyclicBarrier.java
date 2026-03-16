package com.practice.concurrency.era1_classic.ping_pong_game;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class PingPongGameCyclicBarrier {
    CyclicBarrier pingCyclicBarrier = new CyclicBarrier(2);
    CyclicBarrier pongCyclicBarrier = new CyclicBarrier(2);

    public PingPongGameCyclicBarrier(int count) {
        Thread pingThread = new Thread(() -> {
            for (int i = 0; i < count; i++) {
                try {
                    System.out.println("Ping");
                    pingCyclicBarrier.await();
                    pongCyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        Thread pongThread = new Thread(() -> {
            for (int i = 0; i < count; i++) {
                try {
                    pingCyclicBarrier.await();
                    System.out.println("Pong");
                    pongCyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
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
    }

    static void main() {
        new PingPongGameCyclicBarrier(10);
    }
}
