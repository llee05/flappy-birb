package com.example.flappybird.controller;

import com.example.flappybird.model.ChessGameModel;
import com.example.flappybird.view.ChessBoardView;
import com.github.bhlangonijr.chesslib.Square;

import java.util.Collections;

public class ChessController {
    private final ChessGameModel model;
    private final ChessBoardView view;
    private Square selectedSquare = Square.NONE;

    public ChessController(ChessBoardView view) {
        this.model = new ChessGameModel();
        this.view = view;
        this.view.setSquareClickHandler(this::handleSquareClick);
        this.view.update(model.getBoard());
    }

    public void refresh() {
        view.update(model.getBoard());
        updateSelectionHighlights();
    }

    private void handleSquareClick(int row, int col) {
        Square clickedSquare = ChessBoardView.squareForCell(row, col);

        if (selectedSquare == Square.NONE) {
            selectIfCurrentSidePiece(clickedSquare);
            return;
        }

        if (clickedSquare == selectedSquare) {
            clearSelection();
            return;
        }

        if (model.tryMove(selectedSquare, clickedSquare)) {
            selectedSquare = Square.NONE;
            refresh();
            return;
        }

        if (model.hasCurrentSidePiece(clickedSquare)) {
            selectSquare(clickedSquare);
        } else {
            clearSelection();
        }
    }

    private void selectIfCurrentSidePiece(Square square) {
        if (model.hasCurrentSidePiece(square)) {
            selectSquare(square);
        }
    }

    private void selectSquare(Square square) {
        selectedSquare = square;
        updateSelectionHighlights();
    }

    private void clearSelection() {
        selectedSquare = Square.NONE;
        updateSelectionHighlights();
    }

    private void updateSelectionHighlights() {
        if (selectedSquare == Square.NONE) {
            view.showSelection(Square.NONE, Collections.emptyList());
            return;
        }

        view.showSelection(selectedSquare, model.getLegalTargetSquares(selectedSquare));
    }
}
