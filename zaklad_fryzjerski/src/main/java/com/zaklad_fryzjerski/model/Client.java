package com.zaklad_fryzjerski.model;
public class Client extends Thread {
    private final int clientId;
    private final int requiredServiceId;
    private final BarberShop shop;

    public Client(int id, int serviceId, BarberShop shop) {
        this.clientId = id;
        this.requiredServiceId = serviceId;
        this.shop = shop;
        this.setName("Client-" + id);
    }

    @Override
    public void run() {
        try {
            if (shop.enterWaitingRoom(this)) {
                shop.waitForService(this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getClientId() { return clientId; }
    public int getRequiredServiceId() { return requiredServiceId; }
}
