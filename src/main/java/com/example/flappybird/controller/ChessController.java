package com.example.flappybird.controller;

import com.example.flappybird.model.ChessGameModel;
import com.example.flappybird.view.ChessBoardView;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

import java.util.Collections;

public class ChessController {
    private final ChessGameModel model;
    private final ChessBoardView view;
    private Square carriedSquare = Square.NONE;

    public ChessController(ChessBoardView view) {
        this.model = new ChessGameModel();
        this.view = view;
        this.view.update(model.getBoard());
    }

    public void refresh() {
        view.update(model.getBoard());
        updateSelectionHighlights();
    }

    public Side getSideToMove() {
        return model.getSideToMove();
    }

    public String toggleCarryAt(Square square) {
        if (carriedSquare == Square.NONE) {
            pickUpAt(square);
        } else {
            dropAt(square);
        }

        return getCarriedPieceSymbol();
    }

    private void pickUpAt(Square square) {
        if (!model.hasCurrentSidePiece(square)) {
            return;
        }

        carriedSquare = square;
        view.setHiddenPieceSquare(carriedSquare);
        refresh();
    }

    private void dropAt(Square square) {
        if (square == carriedSquare || model.tryMove(carriedSquare, square)) {
            carriedSquare = Square.NONE;
            view.setHiddenPieceSquare(Square.NONE);
            refresh();
            return;
        }

        updateSelectionHighlights();
    }

    private String getCarriedPieceSymbol() {
        if (carriedSquare == Square.NONE) {
            return null;
        }

        Piece piece = model.getPiece(carriedSquare);
        return piece == Piece.NONE ? null : piece.getFanSymbol();
    }

    private void updateSelectionHighlights() {
        if (carriedSquare == Square.NONE) {
            view.showSelection(Square.NONE, Collections.emptyList());
            return;
        }

        view.showSelection(carriedSquare, model.getLegalTargetSquares(carriedSquare));
    }
}
