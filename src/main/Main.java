package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static final int ORIGINAL_TITLE_SIZE = 32;
    private static final int SCALE = 2;
    public static final int TITLE_SIZE = ORIGINAL_TITLE_SIZE * SCALE;

    public static final int MAX_SCREEN_COL = 16;
    public static final int MAX_SCREEN_ROW = 10;

    public static final int SCREEN_WIDTH = TITLE_SIZE * MAX_SCREEN_COL; // 1024
    public static final int SCREEN_HEIGHT = TITLE_SIZE * MAX_SCREEN_ROW; // 640

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../res/FXML/home.fxml")));
        primaryStage.setTitle("JavaFX với FXML");
        primaryStage.setScene(new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT));
        root.requestFocus();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}