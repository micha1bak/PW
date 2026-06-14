package com.zaklad_fryzjerski.model;

import java.util.Random;
import java.util.Set;

public class Barber extends Thread {
    private final int barberId;
    private final Set<Integer> specializations;
    private final BarberShop shop;
    private boolean running = true;
    private final Random random = new Random();
    private SimulationObserver observer;

    public Barber(int id, Set<Integer> specializations, BarberShop shop) {
        this.barberId = id;
        this.specializations = specializations;
        this.shop = shop;
    }

    public void setObserver(SimulationObserver observer) {
        this.observer = observer;
    }

    @Override
    public void run() {
        try {
            while (running) {
                // 1. Czekaj na parę: odpowiedni klient i wolny fotel (atomowo)
                BarberShop.ClientAndChair pair = shop.getNextClientAndChairForBarber(this);
                Client client = pair.client();
                int chairIndex = pair.chairIndex();
                
                if (observer != null) observer.onBarberStartedService(this, client, chairIndex);
                
                // 3. Obsługa
                int serviceTime = 3000 + random.nextInt(4000);
                Thread.sleep(serviceTime);
                
                // 4. Koniec
                client.setServed();
                shop.releaseChair(chairIndex);
                if (observer != null) observer.onBarberFinishedService(this, client, chairIndex);
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
