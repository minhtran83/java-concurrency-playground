package com.practice.concurrency.era1_classic.ping_pong_game;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PingPongGameReentrantLock {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition conditionTurn = lock.newCondition();
    private Turn currentTurn = Turn.PING;

    public PingPongGameReentrantLock(int count) {
        Thread pingThread = new Thread(() -> turn(count, Turn.PING, () -> System.out.println("ping")));
        Thread pongThread = new Thread(() -> turn(count, Turn.PONG, () -> System.out.println("pong")));
        pingThread.start();
        pongThread.start();

        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Gameover");
    }

    static void main() {
        PingPongGameReentrantLock game = new PingPongGameReentrantLock(10);
    }

    private void turn(int count, Turn turn, Runnable action) {
        for (int i = 0; i < count; i++) {
            lock.lock();
            try {
                while (currentTurn != turn) {
                    conditionTurn.await();
                }
                action.run();
                currentTurn = currentTurn == Turn.PING ? Turn.PONG : Turn.PING;
                conditionTurn.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    private enum Turn {PING, PONG}
}
