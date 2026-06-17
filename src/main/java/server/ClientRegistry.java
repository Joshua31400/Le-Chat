package server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientRegistry {
    // Liste thread-safe pour stocker les clients connectés
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public String getActiveUsers() {
        java.util.List<String> pseudos = new java.util.ArrayList<>();
        for (ClientHandler client : clients) {
            if (client.getPseudo() != null) pseudos.add(client.getPseudo());
        }
        return String.join(",", pseudos);
    }
}