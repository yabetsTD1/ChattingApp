package com.example.lastchatapp;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ChatClient {
    private ChatInterface server;
    private String username;

    public ChatClient(String serverIP, String username) {
        this.username = username;
        try {
            server = (ChatInterface) Naming.lookup("rmi://" + serverIP + "/ChatService");
        } catch (Exception e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            System.exit(1);
        }
    }

    public void sendMessage(String message) {
        try {
            server.sendMessage(username, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMessages() {
        try {
            List<String> chatHistory = server.getMessages();
            System.out.println("\nChat History:");
            for (String msg : chatHistory) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Server IP Address: ");
        String serverIP = scanner.nextLine();

        System.out.print("Enter Your Username: ");
        String username = scanner.nextLine();

        ChatClient client = new ChatClient(serverIP, username);

        System.out.println("Connected to chat! Type your messages below:");

        while (true) {
            String message = scanner.nextLine();
            client.sendMessage(message);
            client.getMessages();
        }
    }
}
