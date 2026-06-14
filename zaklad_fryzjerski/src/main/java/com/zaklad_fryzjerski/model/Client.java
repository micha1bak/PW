package com.zaklad_fryzjerski.model;

public class Client extends Thread {
    private final int clientId;
    private final int requiredServiceId;
    private final BarberShop shop;
    private boolean served = false;

    public Client(int id, int serviceId, BarberShop shop) {
        this.clientId = id;
        this.requiredServiceId = serviceId;
        this.shop = shop;
        this.setName("Client-" + id);
    }

    @Override
    public void run() {
        if (shop.enterWaitingRoom(this)) {
            synchronized (this) {
                while (!served) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            System.out.println("Klient " + clientId + " został obsłużony i wychodzi.");
        }
    }

    public synchronized void setServed() {
        this.served = true;
        this.notifyAll();
    }

    public int getClientId() { return clientId; }
    public int getRequiredServiceId() { return requiredServiceId; }
}
