package com.lesson.concurrency.era1_classic.ping_pong_game;

import java.util.concurrent.Exchanger;

/**
 * Exercise: Ping-Pong using Exchanger
 *
 * Challenge: Exchanger only synchronizes the "meet up". After the swap,
 * both threads run in parallel.
 *
 * To enforce strict "Ping THEN Pong", we need TWO exchanges per cycle:
 * 1. Ping prints -> Exchange (Wake Pong)
 * 2. Pong prints -> Exchange (Wake Ping)
 */
public class PingPongGameExchanger {

    private final Exchanger<String> exchanger = new Exchanger<>();

    public void play(int count) {
        Thread pingThread = new Thread(() -> {
            try {
                for (int i = 0; i < count; i++) {
                    System.out.print("Ping");
                    exchanger.exchange("PingDone"); // 1. Hand off to Pong
                    exchanger.exchange("Wait");     // 2. Wait for Pong to finish
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread pongThread = new Thread(() -> {
            try {
                for (int i = 0; i < count; i++) {
                    exchanger.exchange("Wait");     // 1. Wait for Ping
                    System.out.print("Pong ");
                    exchanger.exchange("PongDone"); // 2. Hand off to Ping
                }
            } catch (InterruptedException e) {
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
        System.out.println("\nGame over");
    }

    public static void main(String[] args) {
        new PingPongGameExchanger().play(10);
    }
}
