package protocol;

public class MessageSerializer {
    // Sérialise un objet Message en String CSV
    public static String serialize(Message msg) {
        return msg.type + "|" +
                (msg.pseudo != null ? msg.pseudo : "") + "|" +
                (msg.content != null ? msg.content : "") + "|" +
                msg.timestamp;
    }

    // Désérialise un String CSV en objet Message
    public static Message deserialize(String raw) {
        String[] parts = raw.split("\\|", -1);
        if (parts.length < 4) return null;
        return new Message(
                MessageType.valueOf(parts[0]),
                parts[1].isEmpty() ? null : parts[1],
                parts[2].isEmpty() ? null : parts[2],
                Long.parseLong(parts[3])
        );
    }
}