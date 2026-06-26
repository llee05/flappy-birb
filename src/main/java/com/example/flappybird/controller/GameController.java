package com.example.flappybird.controller;

import com.example.flappybird.model.Birb;
import com.example.flappybird.model.ChessClock;
import com.example.flappybird.view.BirbView;
import com.example.flappybird.view.ChessBoardView;
import com.example.flappybird.view.GameView;
import com.example.flappybird.view.HudView;
import com.example.flappybird.view.MoveHistoryView;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.time.Duration;

public class GameController {
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 800;
    private static final int HISTORY_WIDTH = 200;
    private static final int HUD_WIDTH = 200;
    private static final double BIRD_SIZE = 50;
    private static final Duration STARTING_CLOCK_TIME = Duration.ofMinutes(5).plusSeconds(0);

    private final PlayerBird whiteBird;
    private final PlayerBird blackBird;
    private final GameView gameView;
    private final ChessBoardView chessView;
    private final MoveHistoryView moveHistoryView;
    private final HudView hudView;

    private final ChessController chessController;
    private final ChessClock chessClock;
    private PlayerBird carrier;

    public GameController() {
        whiteBird = new PlayerBird(
                Side.WHITE,
                new Birb(425, 625, BIRD_SIZE, BIRD_SIZE),
                new BirbView(),
                KeyCode.A,
                KeyCode.D,
                KeyCode.SPACE,
                KeyCode.E
        );
        blackBird = new PlayerBird(
                Side.BLACK,
                new Birb(425, 125, BIRD_SIZE, BIRD_SIZE, -1),
                new BirbView(true),
                KeyCode.LEFT,
                KeyCode.RIGHT,
                KeyCode.UP,
                KeyCode.ENTER
        );
        chessView = new ChessBoardView();
        moveHistoryView = new MoveHistoryView(HISTORY_WIDTH, BOARD_HEIGHT);
        hudView = new HudView(HUD_WIDTH, BOARD_HEIGHT);
        chessController = new ChessController(chessView);
        chessClock = new ChessClock(STARTING_CLOCK_TIME);
        gameView = new GameView(chessView, moveHistoryView, hudView, HISTORY_WIDTH, BOARD_WIDTH, whiteBird.view, blackBird.view);

        whiteBird.render();
        blackBird.render();
        updateHud();
    }

    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            whiteBird.handleKeyPressed(event.getCode());
            blackBird.handleKeyPressed(event.getCode());
            handleCarryKeyPressed(whiteBird, event.getCode());
            handleCarryKeyPressed(blackBird, event.getCode());
        });

        scene.setOnKeyReleased(event -> {
            whiteBird.handleKeyReleased(event.getCode());
            blackBird.handleKeyReleased(event.getCode());
        });
    }

    public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                chessClock.tick(now, chessController.getSideToMove());
                whiteBird.update();
                blackBird.update();
                updateHud();
            }
        };

        timer.start();
    }

    public GameView getGameView() {
        return gameView;
    }

    public int getSceneWidth() {
        return HISTORY_WIDTH + BOARD_WIDTH + HUD_WIDTH;
    }

    public int getSceneHeight() {
        return BOARD_HEIGHT;
    }

    private void handleCarryKeyPressed(PlayerBird playerBird, KeyCode keyCode) {
        if (keyCode != playerBird.carryKey) {
            return;
        }

        if (playerBird.carryPressed) {
            return;
        }

        playerBird.carryPressed = true;
        toggleCarry(playerBird);
    }

    private void toggleCarry(PlayerBird playerBird) {
        if (carrier != null && carrier != playerBird) {
            return;
        }

        if (carrier == null && chessController.getSideToMove() != playerBird.side) {
            return;
        }

        String carriedPieceSymbol = chessController.toggleCarryAt(getBirdSquare(playerBird.bird));
        playerBird.view.setCarriedPieceSymbol(carriedPieceSymbol);
        carrier = carriedPieceSymbol == null ? null : playerBird;
        ChessController.CompletedMove completedMove = chessController.consumeCompletedMove();
        if (completedMove != null) {
            moveHistoryView.addMove(completedMove.side(), completedMove.notation());
        }
        updateHud();
    }

    private Square getBirdSquare(Birb bird) {
        double centerX = bird.getX() + bird.getWidth() / 2;
        double centerY = bird.getY() + bird.getHeight() / 2;
        return ChessBoardView.squareForPoint(centerX, centerY);
    }

    private void updateHud() {
        hudView.updateSideToMove(chessController.getSideToMove());
        hudView.updateTimers(
                chessClock.getRemainingSeconds(Side.WHITE),
                chessClock.getRemainingSeconds(Side.BLACK)
        );
    }

    private class PlayerBird {
        private final Side side;
        private final Birb bird;
        private final BirbView view;
        private final BirbController controller;
        private final KeyCode carryKey;
        private boolean carryPressed;

        private PlayerBird(
                Side side,
                Birb bird,
                BirbView view,
                KeyCode leftKey,
                KeyCode rightKey,
                KeyCode jumpKey,
                KeyCode carryKey
        ) {
            this.side = side;
            this.bird = bird;
            this.view = view;
            this.controller = new BirbController(bird, BOARD_WIDTH, BOARD_HEIGHT, leftKey, rightKey, jumpKey);
            this.carryKey = carryKey;
        }

        private void handleKeyPressed(KeyCode keyCode) {
            controller.handleKeyPressed(keyCode);
        }

        private void handleKeyReleased(KeyCode keyCode) {
            controller.handleKeyReleased(keyCode);
            if (keyCode == carryKey) {
                carryPressed = false;
            }
        }

        private void update() {
            controller.update();
            render();
        }

        private void render() {
            view.render(bird);
        }
    }
}
