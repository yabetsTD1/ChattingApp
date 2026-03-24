package com.example.lastchatapp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.rmi.Naming;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ChatGUI extends Application {
    private ChatInterface server;
    private String username;
    private VBox chatBox;
    private TextField messageField;
    private Set<String> displayedMessages = new HashSet<>(); // Prevent duplicate messages

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Login");

        Label serverIpLabel = new Label("Server IP:");
        TextField serverIpField = new TextField("localhost");

        Label usernameLabel = new Label("Enter Username:");
        TextField usernameField = new TextField();

        Button connectButton = new Button("Connect");

        VBox loginLayout = new VBox(10, serverIpLabel, serverIpField, usernameLabel, usernameField, connectButton);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));

        Scene loginScene = new Scene(loginLayout, 300, 200);
        primaryStage.setScene(loginScene);
        primaryStage.show();

        connectButton.setOnAction(event -> {
            String serverIP = serverIpField.getText().trim();
            username = usernameField.getText().trim();
            if (!username.isEmpty() && !serverIP.isEmpty()) {
                connectToServer(serverIP);
                showChatWindow(primaryStage);
            } else {
                showAlert("Error", "Username and Server IP are required!");
            }
        });
    }

    private void connectToServer(String serverIP) {
        try {
            server = (ChatInterface) Naming.lookup("rmi://" + serverIP + "/ChatService");
        } catch (Exception e) {
            showAlert("Connection Error", "Could not connect to server.");
            System.exit(1);
        }
    }

    private void showChatWindow(Stage primaryStage) {
        primaryStage.setTitle("Chat - " + username);

        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #f4f4f4;");

        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        messageField = new TextField();
        messageField.setPromptText("Type a message...");
        messageField.setPrefHeight(40);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, messageField, sendButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(scrollPane, inputBox);
        root.setStyle("-fx-background-color: white;");
        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();

        startMessageListener();
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                server.sendMessage(username, message);
                messageField.clear();
            } catch (Exception e) {
                showAlert("Error", "Failed to send message.");
            }
        }
    }

    private void startMessageListener() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateChat());
            }
        }, 0, 2000);
    }

    private void updateChat() {
        try {
            List<String> messages = server.getMessages();
            for (String msg : messages) {
                if (!displayedMessages.contains(msg)) { // Prevent duplicate messages
                    displayedMessages.add(msg);
                    addMessageToChat(msg);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Could not fetch messages.");
        }
    }

    private void addMessageToChat(String message) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(10));
        messageLabel.setMaxWidth(250);

        HBox messageBox = new HBox(messageLabel);
        messageBox.setPadding(new Insets(5));

        if (message.startsWith(username + ":")) {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageLabel.setStyle("-fx-background-color: #FF0000; -fx-border-radius: 10;");
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageLabel.setStyle("-fx-background-color: #000000; -fx-border-radius: 10;");
        }

        chatBox.getChildren().add(messageBox);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
