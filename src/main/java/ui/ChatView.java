package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ChatView {
    private final BorderPane root = new BorderPane();
    private final TextArea messageArea = new TextArea();
    private final TextField inputField = new TextField();
    private final Button sendButton = new Button("Envoyer");
    private final ListView<String> userList = new ListView<>();

    public ChatView() {
        messageArea.setEditable(false);
        messageArea.setWrapText(true);

        HBox bottomBar = new HBox(10, inputField, sendButton);
        bottomBar.setPadding(new Insets(10));
        HBox.setHgrow(inputField, Priority.ALWAYS); // Le champ texte prend toute la place possible

        VBox rightPanel = new VBox(5, new Label("Utilisateurs:"), userList);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setPrefWidth(150);

        root.setCenter(messageArea);
        root.setBottom(bottomBar);
        root.setRight(rightPanel);
    }

    public BorderPane getView() { return root; }
    public TextArea getMessageArea() { return messageArea; }
    public TextField getInputField() { return inputField; }
    public Button getSendButton() { return sendButton; }
    public ListView<String> getUserList() { return userList; }
}