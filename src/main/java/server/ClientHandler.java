package server;

import java.io.*;
import java.net.Socket;
import protocol.*;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ClientRegistry registry;
    private PrintWriter out;
    private BufferedReader in;
    private String pseudo;

    public ClientHandler(Socket socket, ClientRegistry registry) {
        this.socket = socket;
        this.registry = registry;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String rawMessage;
            while ((rawMessage = in.readLine()) != null) {
                Message msg = MessageSerializer.deserialize(rawMessage);
                if (msg != null) {
                    if (msg.type == MessageType.CONNECT) {
                        this.pseudo = msg.pseudo;
                        // Broadcast de connexion
                        Message info = new Message(MessageType.SERVER_INFO, "", pseudo + " a rejoint le chat", System.currentTimeMillis());
                        registry.broadcast(MessageSerializer.serialize(info));

                        Message listMsg = new Message(MessageType.USER_LIST, "Server", registry.getActiveUsers(), System.currentTimeMillis());
                        registry.broadcast(MessageSerializer.serialize(listMsg));
                    } else if (msg.type == MessageType.DISCONNECT) {
                        break;
                    } else {
                        // Broadcast du message texte
                        registry.broadcast(rawMessage);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de connexion client : " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    public void sendMessage(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    private void disconnect() {
        registry.removeClient(this);
        if (pseudo != null) {
            Message info = new Message(MessageType.SERVER_INFO, "", pseudo + " a quitté le chat", System.currentTimeMillis());
            registry.broadcast(MessageSerializer.serialize(info));

            Message listMsg = new Message(MessageType.USER_LIST, "Server", registry.getActiveUsers(), System.currentTimeMillis());
            registry.broadcast(MessageSerializer.serialize(listMsg));
        }
        try { socket.close(); } catch (IOException e) { /* Ignoré */ }
    }

    public String getPseudo() { return pseudo; }
}