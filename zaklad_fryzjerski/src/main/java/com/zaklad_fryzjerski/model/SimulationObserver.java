package com.zaklad_fryzjerski.model;

public interface SimulationObserver {
    void onClientArrived(Client client);
    void onClientLeft(Client client);
    void onBarberStartedService(Barber barber, Client client, int chairIndex);
    void onBarberFinishedService(Barber barber, Client client, int chairIndex);
    void onClientEnteredWaitingRoom(Client client, int position);
    void onWaitingRoomSizeChanged(int currentSize);
}
