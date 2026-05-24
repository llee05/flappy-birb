package com.example.flappybird.controller;

import com.example.flappybird.model.Birb;
import com.example.flappybird.view.BirbView;
import com.example.flappybird.view.ChessBoardView;
import com.example.flappybird.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GameController {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;

    private final Birb bird;
    private final BirbView birdView;
    private final GameView gameView;
    private final ChessBoardView chessView;

    private final ChessController chessController;

    public GameController() {
        bird = new Birb(
                WINDOW_WIDTH / 2.0 - 25,
                WINDOW_HEIGHT / 2.0 - 25,
                50,
                50
        );

        birdView = new BirbView();
        chessView = new ChessBoardView();
        chessController = new ChessController(chessView);
        gameView = new GameView(chessView, birdView);

        birdView.render(bird);
    }

    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                bird.jump();
            }

            if (event.getCode() == KeyCode.A) {
                bird.dashLeft();
            }

            if (event.getCode() == KeyCode.D) {
                bird.dashRight();
            }

        });


    }

    public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bird.update();

                if (bird.getY() < 0) {
                    bird.setY(0);
                }

                birdView.render(bird);

                if (bird.getY() + bird.getHeight() >= WINDOW_HEIGHT) {
                    bird.setY(WINDOW_HEIGHT - bird.getHeight());
                }
            }
        };

        timer.start();
    }

    public GameView getGameView() {
        return gameView;
    }
}
