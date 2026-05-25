package com.example.flappybird.controller;

import com.example.flappybird.model.Birb;
import com.example.flappybird.view.BirbView;
import com.example.flappybird.view.ChessBoardView;
import com.example.flappybird.view.GameView;
import com.github.bhlangonijr.chesslib.Square;
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
    private boolean carryPressed;
    private boolean jumpPressed;
    private boolean leftPressed;
    private boolean rightPressed;

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
                if (!jumpPressed) {
                    bird.jump();
                }
                jumpPressed = true;
            }

            if (event.getCode() == KeyCode.A) {
                leftPressed = true;
            }

            if (event.getCode() == KeyCode.D) {
                rightPressed = true;
            }

            if (event.getCode() == KeyCode.E) {
                if (!carryPressed) {
                    birdView.setCarriedPieceSymbol(chessController.toggleCarryAt(getBirdSquare()));
                }
                carryPressed = true;
            }

        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                jumpPressed = false;
            }

            if (event.getCode() == KeyCode.A) {
                leftPressed = false;
            }

            if (event.getCode() == KeyCode.D) {
                rightPressed = false;
            }

            if (event.getCode() == KeyCode.E) {
                carryPressed = false;
            }
        });
    }

    public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                bird.setHorizontalInput(getHorizontalInput());
                bird.update();
                bird.constrainToBounds(WINDOW_WIDTH, WINDOW_HEIGHT);
                birdView.render(bird);
            }
        };

        timer.start();
    }

    public GameView getGameView() {
        return gameView;
    }

    private int getHorizontalInput() {
        if (leftPressed == rightPressed) {
            return 0;
        }

        return leftPressed ? -1 : 1;
    }

    private Square getBirdSquare() {
        double centerX = bird.getX() + bird.getWidth() / 2;
        double centerY = bird.getY() + bird.getHeight() / 2;
        return ChessBoardView.squareForPoint(centerX, centerY);
    }
}
