package com.practice.concurrency.era1_classic.monitor;

public class PingPongGame {
    private final Object lock = new Object();
    private boolean pingTurn = true;

    public PingPongGame() {
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
                    while (!pingTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    System.out.println("Ping");
                    pingTurn = false;
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
                    while (pingTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }

                    System.out.println("Pong");
                    pingTurn = true;
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        PingPongGame game = new PingPongGame();
    }
}
