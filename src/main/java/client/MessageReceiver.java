package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

public class MessageReceiver implements Runnable {
    private final BufferedReader in;
    private final Consumer<String> onMessageReceived;

    public MessageReceiver(BufferedReader in, Consumer<String> onMessageReceived) {
        this.in = in;
        this.onMessageReceived = onMessageReceived;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                // On transmet le message à l'interface via ce callback
                onMessageReceived.accept(message);
            }
        } catch (IOException e) {
            System.out.println("Connexion au serveur perdue.");
        }
    }
}