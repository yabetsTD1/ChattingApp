package com.example.lastchatapp;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.net.InetAddress;

public class ChatServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Start RMI registry
            ChatInterface server = new ChatServerImpl();
            String serverIP = InetAddress.getLocalHost().getHostAddress();
            Naming.rebind("rmi://" + serverIP + "/ChatService", server);
            System.out.println("Chat Server is running on IP: " + serverIP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
