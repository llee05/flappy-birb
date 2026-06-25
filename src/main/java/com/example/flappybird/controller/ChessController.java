package com.example.flappybird.controller;

import com.example.flappybird.model.ChessGameModel;
import com.example.flappybird.view.ChessBoardView;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.Collections;

public class ChessController {
    private final ChessGameModel model;
    private final ChessBoardView view;
    private final MoveList moveHistory = new MoveList();
    private Square carriedSquare = Square.NONE;
    private CompletedMove completedMove;

    public record CompletedMove(Side side, String notation) {
    }

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
        completedMove = null;
        if (carriedSquare == Square.NONE) {
            pickUpAt(square);
        } else {
            dropAt(square);
        }

        return getCarriedPieceSymbol();
    }

    public CompletedMove consumeCompletedMove() {
        CompletedMove move = completedMove;
        completedMove = null;
        return move;
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
        if (square == carriedSquare) {
            carriedSquare = Square.NONE;
            view.setHiddenPieceSquare(Square.NONE);
            refresh();
            return;
        }

        Square from = carriedSquare;
        Side movingSide = model.getSideToMove();
        Move move = model.getLegalMove(from, square);
        if (move != null && model.tryMove(move)) {
            completedMove = new CompletedMove(movingSide, recordMove(move));
            carriedSquare = Square.NONE;
            view.setHiddenPieceSquare(Square.NONE);
            refresh();
            return;
        }

        updateSelectionHighlights();
    }

    private String recordMove(Move move) {
        moveHistory.add(move);
        try {
            String[] sanMoves = moveHistory.toSanArray();
            return sanMoves[sanMoves.length - 1];
        } catch (MoveConversionException exception) {
            return move.toString();
        }
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
