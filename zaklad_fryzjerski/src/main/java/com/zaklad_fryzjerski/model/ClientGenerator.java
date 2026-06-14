package com.zaklad_fryzjerski.model;

import java.util.Random;

public class ClientGenerator extends Thread {
    private final BarberShop shop;
    private final int numberOfServiceTypes;
    private boolean running = true;
    private final Random random = new Random();
    private int nextClientId = 1;

    public ClientGenerator(BarberShop shop, int numberOfServiceTypes) {
        this.shop = shop;
        this.numberOfServiceTypes = numberOfServiceTypes;
        this.setName("ClientGenerator");
    }

    @Override
    public void run() {
        try {
            while (running) {
                // Losowy czas do przybycia kolejnego klienta (np. 0.5-1.0 sekundy)
                Thread.sleep(500 + random.nextInt(1000));
                
                int serviceId = 1 + random.nextInt(numberOfServiceTypes);
                Client client = new Client(nextClientId++, serviceId, shop);
                client.start();
            }
        } catch (InterruptedException e) {
            System.out.println("Generator klientów zatrzymany.");
            Thread.currentThread().interrupt();
        }
    }

    public void stopGenerator() {
        this.running = false;
        this.interrupt();
    }
}
