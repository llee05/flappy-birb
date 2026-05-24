package com.example.flappybird.controller;

import com.example.flappybird.model.ChessGameModel;
import com.example.flappybird.view.ChessBoardView;

public class ChessController {
    private final ChessGameModel model;
    private final ChessBoardView view;

    public ChessController(ChessBoardView view) {
        this.model = new ChessGameModel();
        this.view = view;
        this.view.update(model.getBoard());
    }

    public void refresh() {
        view.update(model.getBoard());
    }
}
