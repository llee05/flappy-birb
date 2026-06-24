package com.example.flappybird.view;

import javafx.scene.layout.Pane;

public class GameView {
    private final Pane root;

    public GameView(ChessBoardView chessBoardView, BirbView... birdViews) {
        root = new Pane();

        root.getChildren().add(chessBoardView.getNode());
        for (BirbView birdView : birdViews) {
            root.getChildren().add(birdView.getNode());
        }
    }

    public Pane getRoot() {
        return root;
    }
}
