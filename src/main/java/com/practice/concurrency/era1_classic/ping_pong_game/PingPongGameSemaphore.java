package com.practice.concurrency.era1_classic.ping_pong_game;

import java.util.concurrent.Semaphore;

public class PingPongGameSemaphore {
    private final Semaphore pingTurn = new Semaphore(1);
    private final Semaphore pongTurn = new Semaphore(0);

    PingPongGameSemaphore(int count) {
        for (int i = 0; i < count; i++) {
            Thread pingThread = new Thread(() -> turn(pingTurn, pongTurn, () -> System.out.println("ping")));
            Thread pongThread = new Thread(() -> turn(pongTurn, pingTurn, () -> System.out.println("pong")));
            pingThread.start();
            pongThread.start();

            try {
                pingThread.join();
                pongThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        PingPongGameSemaphore game = new PingPongGameSemaphore(10);
    }

    private void turn(Semaphore ourTurnSemaphore, Semaphore theirTurnSemaphore, Runnable action) {
        try {
            ourTurnSemaphore.acquire();
            action.run();
            theirTurnSemaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
