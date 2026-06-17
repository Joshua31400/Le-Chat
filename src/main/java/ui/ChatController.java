package ui;

import client.ChatClient;
import javafx.application.Platform;
import protocol.*;

import java.io.IOException;

public class ChatController {
    private final ChatView view;
    private final ChatClient client;

    public ChatController(ChatView view) {
        this.view = view;
        this.client = new ChatClient();

        initEventHandlers();
        connectToServer();
    }

    private void initEventHandlers() {
        view.getSendButton().setOnAction(e -> handleSend());
        view.getInputField().setOnAction(e -> handleSend()); // Valide avec la touche Entrée
    }

    private void connectToServer() {
        // Pseudo généré aléatoirement pour gagner du temps au lancement
        String pseudo = "User" + (int)(Math.random() * 1000);
        try {
            client.connect("127.0.0.1", 5000, pseudo, this::handleIncomingMessage);
            Platform.runLater(() -> view.getMessageArea().appendText("Connecté en tant que " + pseudo + "\n"));
        } catch (IOException e) {
            view.getMessageArea().appendText("Erreur : le serveur n'est pas lancé.\n");
        }
    }

    private void handleSend() {
        String text = view.getInputField().getText();
        if (!text.trim().isEmpty()) {
            client.sendText(text);
            view.getInputField().clear();
        }
    }

    private void handleIncomingMessage(String rawMessage) {
        Platform.runLater(() -> {
            Message msg = MessageSerializer.deserialize(rawMessage);
            if (msg != null) {
                if (msg.type == MessageType.TEXT) {
                    view.getMessageArea().appendText(msg.pseudo + " : " + msg.content + "\n");
                } else if (msg.type == MessageType.SERVER_INFO) {
                    view.getMessageArea().appendText("[INFO] " + msg.content + "\n");
                } else if (msg.type == MessageType.USER_LIST) {

                    view.getUserList().getItems().clear();
                    if (msg.content != null && !msg.content.isEmpty()) {
                        view.getUserList().getItems().addAll(msg.content.split(","));
                    }
                }
            }
        });
    }

    public void disconnect() {
        client.disconnect();
    }
}