package protocol;

public class Message {
    public MessageType type;
    public String pseudo;
    public String content;
    public long timestamp;

    public Message(MessageType type, String pseudo, String content, long timestamp) {
        this.type = type;
        this.pseudo = pseudo;
        this.content = content;
        this.timestamp = timestamp;
    }
}