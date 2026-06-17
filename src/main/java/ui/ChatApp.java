package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        ChatView view = new ChatView();
        ChatController controller = new ChatController(view);

        Scene scene = new Scene(view.getView(), 600, 400);
        primaryStage.setTitle("Le Chat Local");
        primaryStage.setScene(scene);

        // Coupe proprement la connexion réseau si on ferme la fenêtre
        primaryStage.setOnCloseRequest(event -> controller.disconnect());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}