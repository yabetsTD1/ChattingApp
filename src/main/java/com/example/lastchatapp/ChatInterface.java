package com.example.lastchatapp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatInterface extends Remote {
    void sendMessage(String username, String message) throws RemoteException;
    List<String> getMessages() throws RemoteException;
}
