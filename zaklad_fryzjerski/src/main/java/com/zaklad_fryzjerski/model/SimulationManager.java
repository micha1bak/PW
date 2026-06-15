package com.zaklad_fryzjerski.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimulationManager {
    private final BarberShop shop;
    private final List<Barber> barbers = new ArrayList<>();
    private final ClientGenerator generator;

    public SimulationManager(int n, int pTotal, int l, int capacity, List<Set<Integer>> specializations) {
        this.shop = new BarberShop(capacity, l);
        initializeBarbers(pTotal, specializations);
        this.generator = new ClientGenerator(shop, n);
    }

    private void initializeBarbers(int pTotal, List<Set<Integer>> specializations) {
        for (int i = 0; i < pTotal; i++) {
            Set<Integer> specs = (i < specializations.size()) ? specializations.get(i) : Set.of(1);
            Barber barber = new Barber(i + 1, specs, shop);
            barbers.add(barber);
        }
    }

    public void setObserver(SimulationObserver observer) {
        this.shop.setObserver(observer);
    }

    public void setGenerationDelay(int ms) {
        generator.setGenerationDelay(ms);
    }

    public void setServiceDuration(int ms) {
        for (Barber b : barbers) {
            b.setServiceDuration(ms);
        }
    }

    public void start() {
        for (Barber b : barbers) b.start();
        generator.start();
    }

    public void stop() {
        generator.stopGenerator();
        for (Barber b : barbers) b.stopBarber();
    }
}
