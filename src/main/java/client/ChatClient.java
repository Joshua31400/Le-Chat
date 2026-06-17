package client;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;
import protocol.*;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String pseudo;

    public void connect(String host, int port, String pseudo, Consumer<String> onMessageReceived) throws IOException {
        this.pseudo = pseudo;
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // On se présente au serveur
        Message connectMsg = new Message(MessageType.CONNECT, pseudo, "", System.currentTimeMillis());
        sendMessage(MessageSerializer.serialize(connectMsg));

        // On lance l'écoute des messages entrants dans un thread séparé
        new Thread(new MessageReceiver(in, onMessageReceived)).start();
    }

    public void sendMessage(String rawMessage) {
        if (out != null) {
            out.println(rawMessage);
        }
    }

    public void sendText(String content) {
        Message textMsg = new Message(MessageType.TEXT, pseudo, content, System.currentTimeMillis());
        sendMessage(MessageSerializer.serialize(textMsg));
    }

    public void disconnect() {
        if (socket != null && !socket.isClosed()) {
            Message disconnectMsg = new Message(MessageType.DISCONNECT, pseudo, "", System.currentTimeMillis());
            sendMessage(MessageSerializer.serialize(disconnectMsg));
            try { socket.close(); } catch (IOException e) { /* Ignoré */ }
        }
    }
}