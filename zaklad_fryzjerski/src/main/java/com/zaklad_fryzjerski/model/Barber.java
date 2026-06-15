package com.zaklad_fryzjerski.model;

import java.util.Random;
import java.util.Set;

public class Barber extends Thread {
    private final int barberId;
    private final Set<Integer> specializations;
    private final BarberShop shop;
    private boolean running = true;
    private final Random random = new Random();
    private volatile int serviceDuration = 4000;

    public Barber(int id, Set<Integer> specializations, BarberShop shop) {
        this.barberId = id;
        this.specializations = specializations;
        this.shop = shop;
    }

    public void setServiceDuration(int ms) {
        this.serviceDuration = ms;
    }

    @Override
    public void run() {
        try {
            while (running) {
                // 1. Czekaj na parę: odpowiedni klient i wolny fotel
                BarberShop.ClientAndChair pair = shop.getNextClientAndChairForBarber(this);
                Client client = pair.client();
                int chairIndex = pair.chairIndex();
                
                // 2. Obsługa (używamy dynamicznego czasu ±20%)
                int currentDuration = (int) (serviceDuration * (0.8 + random.nextDouble() * 0.4));
                Thread.sleep(Math.max(500, currentDuration));
                
                // 3. Koniec obsługi w salonie (powiadomienie klienta i zwolnienie fotela)
                shop.finishService(this, client, chairIndex);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stopBarber() {
        this.running = false;
        this.interrupt();
    }

    public int getBarberId() { return barberId; }
    public Set<Integer> getSpecializations() { return specializations; }
}
