package com.example.flappybird.controller;

import com.example.flappybird.model.Birb;
import com.example.flappybird.model.ChessClock;
import com.example.flappybird.model.ChessGameModel.GameStatus;
import com.example.flappybird.view.BirbView;
import com.example.flappybird.view.ChessBoardView;
import com.example.flappybird.view.EndScreenView;
import com.example.flappybird.view.GameView;
import com.example.flappybird.view.HudView;
import com.example.flappybird.view.MoveHistoryView;
import com.example.flappybird.view.PromotionChoiceView;
import com.github.bhlangonijr.chesslib.PieceType;
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
    private final PromotionChoiceView promotionChoiceView;
    private final EndScreenView endScreenView;

    private final ChessController chessController;
    private final ChessClock chessClock;
    private AnimationTimer gameLoop;
    private PlayerBird carrier;
    private boolean gameOver;

    public GameController(Runnable restartAction) {
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
        promotionChoiceView = new PromotionChoiceView(getSceneWidth(), getSceneHeight());
        endScreenView = new EndScreenView(getSceneWidth(), getSceneHeight(), restartAction);
        chessController = new ChessController(chessView);
        chessClock = new ChessClock(STARTING_CLOCK_TIME);
        gameView = new GameView(
                chessView,
                moveHistoryView,
                hudView,
                promotionChoiceView,
                endScreenView,
                HISTORY_WIDTH,
                BOARD_WIDTH,
                whiteBird.view,
                blackBird.view
        );

        whiteBird.render();
        blackBird.render();
        updateHud();
    }

    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (gameOver) {
                return;
            }
            if (chessController.hasPendingPromotion()) {
                handlePromotionKeyPressed(event.getCode());
                return;
            }
            whiteBird.handleKeyPressed(event.getCode());
            blackBird.handleKeyPressed(event.getCode());
            handleCarryKeyPressed(whiteBird, event.getCode());
            handleCarryKeyPressed(blackBird, event.getCode());
        });

        scene.setOnKeyReleased(event -> {
            if (gameOver) {
                return;
            }
            whiteBird.handleKeyReleased(event.getCode());
            blackBird.handleKeyReleased(event.getCode());
        });
    }

    public void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                chessClock.tick(now, chessController.getSideToMove());
                if (endGameIfClockExpired()) {
                    return;
                }
                whiteBird.update();
                blackBird.update();
                updateHud();
            }
        };

        gameLoop.start();
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
        if (gameOver) {
            return;
        }

        if (carrier != null && carrier != playerBird) {
            return;
        }

        if (carrier == null && chessController.getSideToMove() != playerBird.side) {
            return;
        }

        String carriedPieceSymbol = chessController.toggleCarryAt(getBirdSquare(playerBird.bird));
        playerBird.view.setCarriedPieceSymbol(carriedPieceSymbol);
        carrier = carriedPieceSymbol == null ? null : playerBird;
        if (chessController.hasPendingPromotion()) {
            promotionChoiceView.show(chessController.getPromotionRequest().side());
            updateHud();
            return;
        }

        ChessController.CompletedMove completedMove = chessController.consumeCompletedMove();
        if (completedMove != null) {
            moveHistoryView.addMove(completedMove.side(), completedMove.notation());
            endGameIfChessFinished();
        }
        updateHud();
    }

    private void handlePromotionKeyPressed(KeyCode keyCode) {
        PieceType promotionType = promotionTypeForKey(keyCode);
        if (promotionType == null) {
            return;
        }

        ChessController.CompletedMove completedMove = chessController.choosePromotion(promotionType);
        if (completedMove == null) {
            return;
        }

        promotionChoiceView.hide();
        if (carrier != null) {
            carrier.view.setCarriedPieceSymbol(null);
        }
        carrier = null;
        moveHistoryView.addMove(completedMove.side(), completedMove.notation());
        endGameIfChessFinished();
        updateHud();
    }

    private PieceType promotionTypeForKey(KeyCode keyCode) {
        return switch (keyCode) {
            case Q -> PieceType.QUEEN;
            case R -> PieceType.ROOK;
            case B -> PieceType.BISHOP;
            case N -> PieceType.KNIGHT;
            default -> null;
        };
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

    private boolean endGameIfClockExpired() {
        Side sideToMove = chessController.getSideToMove();
        if (!chessClock.isExpired(sideToMove)) {
            return false;
        }

        Side winner = sideToMove.flip();
        showEndScreen(formatWinner(winner), formatSide(sideToMove) + " ran out of time");
        return true;
    }

    private void endGameIfChessFinished() {
        GameStatus status = chessController.getGameStatus();
        if (status == GameStatus.ACTIVE) {
            return;
        }

        if (status == GameStatus.CHECKMATE) {
            showEndScreen(formatWinner(chessController.getWinner()), "Checkmate");
        } else if (status == GameStatus.STALEMATE) {
            showEndScreen("Draw", "Stalemate");
        } else {
            showEndScreen("Draw", "Draw by chess rules");
        }
    }

    private void showEndScreen(String title, String reason) {
        if (gameOver) {
            return;
        }

        gameOver = true;
        if (gameLoop != null) {
            gameLoop.stop();
        }
        endScreenView.show(title, reason);
        updateHud();
    }

    private String formatWinner(Side winner) {
        return formatSide(winner) + " wins";
    }

    private String formatSide(Side side) {
        return side == Side.WHITE ? "White" : "Black";
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
