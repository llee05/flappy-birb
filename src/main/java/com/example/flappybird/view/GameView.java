package com.example.flappybird.view;

import javafx.scene.layout.Pane;

public class GameView {
    private final Pane root;
    private final Pane boardLayer;
    private final HudView hudView;

    public GameView(ChessBoardView chessBoardView, HudView hudView, double boardWidth, BirbView... birdViews) {
        this.hudView = hudView;
        root = new Pane();
        boardLayer = new Pane();

        boardLayer.getChildren().add(chessBoardView.getNode());
        for (BirbView birdView : birdViews) {
            boardLayer.getChildren().add(birdView.getNode());
        }

        hudView.getNode().setLayoutX(boardWidth);
        root.getChildren().addAll(boardLayer, hudView.getNode());
    }

    public Pane getRoot() {
        return root;
    }

    public HudView getHudView() {
        return hudView;
    }
}
