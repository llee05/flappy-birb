package com.example.flappybird;

import com.example.flappybird.controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlappyApp extends Application {
    @Override
    public void start(Stage stage) {
        GameController controller = new GameController();

        Scene scene = new Scene(controller.getGameView().getRoot(), 800, 800);

        controller.setupInput(scene);
        controller.startGameLoop();

        stage.setTitle("Flappy Chess");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}