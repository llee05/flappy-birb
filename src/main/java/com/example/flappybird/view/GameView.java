package com.example.flappybird.view;

import javafx.scene.layout.Pane;

public class GameView {
    private final Pane root;
    private final Pane boardLayer;
    private final MoveHistoryView moveHistoryView;
    private final HudView hudView;
    private final EndScreenView endScreenView;

    public GameView(
            ChessBoardView chessBoardView,
            MoveHistoryView moveHistoryView,
            HudView hudView,
            EndScreenView endScreenView,
            double historyWidth,
            double boardWidth,
            BirbView... birdViews
    ) {
        this.moveHistoryView = moveHistoryView;
        this.hudView = hudView;
        this.endScreenView = endScreenView;
        root = new Pane();
        boardLayer = new Pane();

        boardLayer.getChildren().add(chessBoardView.getNode());
        for (BirbView birdView : birdViews) {
            boardLayer.getChildren().add(birdView.getNode());
        }

        boardLayer.setLayoutX(historyWidth);
        hudView.getNode().setLayoutX(historyWidth + boardWidth);
        root.getChildren().addAll(moveHistoryView.getNode(), boardLayer, hudView.getNode(), endScreenView.getNode());
    }

    public Pane getRoot() {
        return root;
    }

    public HudView getHudView() {
        return hudView;
    }

    public MoveHistoryView getMoveHistoryView() {
        return moveHistoryView;
    }

    public EndScreenView getEndScreenView() {
        return endScreenView;
    }
}
