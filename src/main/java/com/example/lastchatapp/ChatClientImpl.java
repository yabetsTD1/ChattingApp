package com.example.lastchatapp;
import java.rmi.Naming;

public class ChatClientImpl {
    private ChatInterface server;

    public ChatClientImpl() {
        try {
            server = (ChatInterface) Naming.lookup("rmi://localhost/ChatService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String username, String message) {
        try {
            server.sendMessage(username, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMessages() {
        try {
            for (String msg : server.getMessages()) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
