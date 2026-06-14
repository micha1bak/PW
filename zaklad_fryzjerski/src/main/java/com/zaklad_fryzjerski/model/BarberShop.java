package com.zaklad_fryzjerski.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShop {
    private final int capacity;
    private final int totalChairs;
    private final List<Client> waitingRoom = new LinkedList<>();
    private final boolean[] chairOccupied;
    
    private final Lock lock = new ReentrantLock();
    private final Condition stateChanged = lock.newCondition();
    
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

    public boolean enterWaitingRoom(Client client) {
        lock.lock();
        try {
            if (waitingRoom.size() < capacity) {
                if (observer != null) observer.onClientArrived(client);
                
                waitingRoom.add(client);
                int pos = waitingRoom.size() - 1;
                
                if (observer != null) {
                    observer.onClientEnteredWaitingRoom(client, pos);
                    observer.onWaitingRoomSizeChanged(waitingRoom.size());
                }
                
                stateChanged.signalAll();
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public ClientAndChair getNextClientAndChairForBarber(Barber barber) throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                // 1. Szukaj wolnego fotela
                int freeChairIndex = -1;
                for (int i = 0; i < totalChairs; i++) {
                    if (!chairOccupied[i]) {
                        freeChairIndex = i;
                        break;
                    }
                }

                // 2. Jeśli jest fotel, szukaj odpowiedniego klienta
                if (freeChairIndex != -1) {
                    for (int i = 0; i < waitingRoom.size(); i++) {
                        Client c = waitingRoom.get(i);
                        if (barber.getSpecializations().contains(c.getRequiredServiceId())) {
                            // Znaleziono komplet!
                            waitingRoom.remove(i);
                            chairOccupied[freeChairIndex] = true;

                            if (observer != null) {
                                observer.onWaitingRoomSizeChanged(waitingRoom.size());
                                for (int j = 0; j < waitingRoom.size(); j++) {
                                    observer.onClientEnteredWaitingRoom(waitingRoom.get(j), j);
                                }
                            }
                            return new ClientAndChair(c, freeChairIndex);
                        }
                    }
                }
                
                // Czekaj na zmianę stanu (nowy klient lub zwolniony fotel)
                stateChanged.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void releaseChair(int chairIndex) {
        lock.lock();
        try {
            chairOccupied[chairIndex] = false;
            stateChanged.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
