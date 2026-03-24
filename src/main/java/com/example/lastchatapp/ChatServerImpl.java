package com.example.lastchatapp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatServerImpl extends UnicastRemoteObject implements ChatInterface {
    private final Map<String, String> messages = new LinkedHashMap<>(); // Stores messages in order

    protected ChatServerImpl() throws RemoteException {}

    @Override
    public synchronized void sendMessage(String sender, String message) throws RemoteException {
        messages.put(sender, message);
        System.out.println(sender + ": " + message);
    }

    @Override
    public synchronized List<String> getMessages() throws RemoteException {
        List<String> chatHistory = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            chatHistory.add(entry.getKey() + ": " + entry.getValue());
        }
        return chatHistory;
    }
}
