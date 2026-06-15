package com.zaklad_fryzjerski.model;

import java.util.Random;

public class ClientGenerator extends Thread {
    private final BarberShop shop;
    private final int numberOfServiceTypes;
    private boolean running = true;
    private final Random random = new Random();
    private int nextClientId = 1;
    private volatile int generationDelay = 1000;

    public ClientGenerator(BarberShop shop, int numberOfServiceTypes) {
        this.shop = shop;
        this.numberOfServiceTypes = numberOfServiceTypes;
        this.setName("ClientGenerator");
    }

    public void setGenerationDelay(int ms) {
        this.generationDelay = ms;
    }

    @Override
    public void run() {
        try {
            while (running) {
                // ±20% losowości
                int currentDelay = (int) (generationDelay * (0.8 + random.nextDouble() * 0.4));
                Thread.sleep(Math.max(100, currentDelay));
                
                int serviceId = 1 + random.nextInt(numberOfServiceTypes);
                Client client = new Client(nextClientId++, serviceId, shop);
                client.start();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stopGenerator() {
        this.running = false;
        this.interrupt();
    }
}
