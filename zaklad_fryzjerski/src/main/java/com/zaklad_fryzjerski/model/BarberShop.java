package com.zaklad_fryzjerski.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BarberShop {
    private final int capacity;
    private final int totalChairs;
    private final List<Client> waitingRoom = new LinkedList<>();
    private final boolean[] chairOccupied;
    private final Set<Client> servedClients = new HashSet<>();
    private final Set<Barber> availableBarbers = new HashSet<>();
    
    private SimulationObserver observer;

    public record ClientAndChair(Client client, int chairIndex) {}

    public BarberShop(int capacity, int totalChairs) {
        this.capacity = capacity;
        this.totalChairs = totalChairs;
        this.chairOccupied = new boolean[totalChairs];
    }

    public void setObserver(SimulationObserver observer) {
        this.observer = observer;
    }

    public synchronized boolean enterWaitingRoom(Client client) {
        if (waitingRoom.size() < capacity) {
            if (observer != null) observer.onClientArrived(client);
            waitingRoom.add(client);
            int pos = waitingRoom.size() - 1;
            if (observer != null) {
                observer.onClientEnteredWaitingRoom(client, pos);
                observer.onWaitingRoomSizeChanged(waitingRoom.size());
            }
            notifyAll();
            return true;
        }
        else {
            if (observer != null){
                observer.onClientLeft(client);
            }
        }
        return false;
    }

    public synchronized void waitForService(Client client) throws InterruptedException {
        while (!servedClients.contains(client)) {
            wait();
        }
        servedClients.remove(client);
    }

    private boolean anyFreeBarberCanServe(Client client) {
        for (Barber b : availableBarbers) {
            if (b.getSpecializations().contains(client.getRequiredServiceId())) {
                return true;
            }
        }
        return false;
    }

    public synchronized ClientAndChair getNextClientAndChairForBarber(Barber barber) throws InterruptedException {
        availableBarbers.add(barber);
        try {
            while (true) {
                int freeChairIndex = -1;
                for (int i = 0; i < totalChairs; i++) {
                    if (!chairOccupied[i]) {
                        freeChairIndex = i;
                        break;
                    }
                }
                if (freeChairIndex != -1 && !waitingRoom.isEmpty()) {
                    for (int i = 0; i < waitingRoom.size(); i++) {
                        Client client = waitingRoom.get(i);
                        if (barber.getSpecializations().contains(client.getRequiredServiceId())) {
                            boolean skipBecauseOfPriority = false;
                            for (int j = 0; j < i; j++) {
                                if (anyFreeBarberCanServe(waitingRoom.get(j))) {
                                    skipBecauseOfPriority = true;
                                    break;
                                }
                            }
                            if (!skipBecauseOfPriority) {
                                waitingRoom.remove(i);
                                chairOccupied[freeChairIndex] = true;
                                if (observer != null) {
                                    observer.onWaitingRoomSizeChanged(waitingRoom.size());
                                    for (int k = 0; k < waitingRoom.size(); k++) {
                                        observer.onClientEnteredWaitingRoom(waitingRoom.get(k), k);
                                    }
                                    observer.onBarberStartedService(barber, client, freeChairIndex);
                                }
                                return new ClientAndChair(client, freeChairIndex);
                            }
                        }
                    }
                }
                wait();
            }
        } finally {
            availableBarbers.remove(barber);
        }
    }

    public synchronized void finishService(Barber barber, Client client, int chairIndex) {
        chairOccupied[chairIndex] = false;
        servedClients.add(client);
        if (observer != null) observer.onBarberFinishedService(barber, client, chairIndex);
        notifyAll();
    }
}
