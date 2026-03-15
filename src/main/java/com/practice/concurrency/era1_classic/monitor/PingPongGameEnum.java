package com.practice.concurrency.era1_classic.monitor;

public class PingPongGameEnum {
    private final Object lock = new Object();
    private Turn turn = Turn.PING;

    public PingPongGameEnum() {
        PingThread pingThread = new PingThread(10);
        PongThread pongThread = new PongThread(10);

        pingThread.start();
        pongThread.start();

        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Game over");
    }

    static void main() {
        PingPongGameEnum game = new PingPongGameEnum();
    }

    class PingThread extends Thread {
        private final int count;

        PingThread(int count) {
            super("PingThread");
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                synchronized (lock) {
                    while (turn != Turn.PING) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    System.out.println("Ping");
                    turn = Turn.PONG;
                    lock.notifyAll();
                }
            }
        }
    }

    class PongThread extends Thread {
        private final int count;

        PongThread(int count) {
            super("PongThread");
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                synchronized (lock) {
                    while (turn != Turn.PONG) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    System.out.println("Pong");
                    turn = Turn.PING;
                    lock.notifyAll();
                }
            }
        }
    }

    private enum Turn {
        PING, PONG
    }
}
