package com.practice.concurrency.era1_classic.monitor;

public class PingPongGameExtract {
    private final Object lock = new Object();
    private final int count;
    private Turn turn = Turn.PING;

    PingPongGameExtract(int count) {
        this.count = count;
        Thread pingThread = new Thread(() -> turn(Turn.PING, () -> {
            try { Thread.sleep(1); } catch (InterruptedException e) {}  // Slow
            System.out.println("ping");
        }));
        Thread pongThread = new Thread(() -> turn(Turn.PONG, () -> {
            try { Thread.sleep(1); } catch (InterruptedException e) {}  // Slow
            System.out.println("pong");
        }));

        pingThread.start();
        pongThread.start();

        try {
            pingThread.join(5000);
            pongThread.join(5000);
            if (pingThread.isAlive() || pongThread.isAlive()) {
                System.out.println("DEADLOCK DETECTED!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Game over");
    }

    static void main() {
        new PingPongGameExtract(125);
    }

    private void switchTurn() {
        turn = turn == Turn.PING ? Turn.PONG : Turn.PING;
    }

    public void turn(Turn currentTurn, Runnable action) {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {
                while (turn != currentTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                switchTurn();
                action.run();
                lock.notifyAll();
            }
        }
    }

    private enum Turn {PING, PONG}
}
