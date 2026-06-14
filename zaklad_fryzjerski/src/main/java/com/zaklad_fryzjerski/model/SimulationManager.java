package com.zaklad_fryzjerski.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimulationManager {
    private final BarberShop shop;
    private final List<Barber> barbers = new ArrayList<>();
    private final ClientGenerator generator;

    private SimulationObserver observer;

    public SimulationManager(int n, int pTotal, int l, int capacity, List<Set<Integer>> specializations) {
        this.shop = new BarberShop(capacity, l);

        for (int i = 0; i < pTotal; i++) {
            Set<Integer> specs = (i < specializations.size()) ? specializations.get(i) : Set.of(1);
            Barber barber = new Barber(i + 1, specs, shop);
            barbers.add(barber);
        }
        
        this.generator = new ClientGenerator(shop, n);
    }

    public void setObserver(SimulationObserver observer) {
        this.observer = observer;
        this.shop.setObserver(observer);
        for (Barber b : barbers) b.setObserver(observer);
    }

    public void start() {
        for (Barber b : barbers) b.start();
        generator.start();
    }

    public void stop() {
        generator.stopGenerator();
        for (Barber b : barbers) b.stopBarber();
    }

    public BarberShop getShop() { return shop; }
    public List<Barber> getBarbers() { return barbers; }
}
