package com.example.flappybird.controller;

import com.example.flappybird.view.ChessBoardView;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ChessControllerTest {
    @Test
    void constructorRendersInitialBoard() {
        RecordingChessBoardView view = new RecordingChessBoardView();

        new ChessController(view);

        assertEquals(1, view.updateCount);
        assertEquals(Side.WHITE, view.lastBoard.getSideToMove());
    }

    @Test
    void cannotPickUpOpposingPieceAtStart() {
        RecordingChessBoardView view = new RecordingChessBoardView();
        ChessController controller = new ChessController(view);

        assertNull(controller.toggleCarryAt(Square.E7));

        assertEquals(Square.NONE, view.hiddenPieceSquare);
        assertEquals(1, view.updateCount);
        assertEquals(Square.NONE, view.selectedSquare);
        assertEquals(Collections.emptyList(), view.legalTargets);
    }

    @Test
    void pickingUpCurrentSidePieceHidesItAndShowsLegalTargets() {
        RecordingChessBoardView view = new RecordingChessBoardView();
        ChessController controller = new ChessController(view);

        String carriedSymbol = controller.toggleCarryAt(Square.E2);

        assertEquals(Piece.WHITE_PAWN.getFanSymbol(), carriedSymbol);
        assertEquals(Square.E2, view.hiddenPieceSquare);
        assertEquals(2, view.updateCount);
        assertEquals(Square.E2, view.selectedSquare);
        assertEquals(List.of(Square.E3, Square.E4), view.legalTargets);
    }

    @Test
    void droppingOnLegalTargetMovesPieceAndClearsCarryState() {
        RecordingChessBoardView view = new RecordingChessBoardView();
        ChessController controller = new ChessController(view);

        controller.toggleCarryAt(Square.E2);
        String carriedSymbolAfterDrop = controller.toggleCarryAt(Square.E4);

        assertNull(carriedSymbolAfterDrop);
        assertEquals(Square.NONE, view.hiddenPieceSquare);
        assertEquals(Side.BLACK, view.lastBoard.getSideToMove());
        assertEquals(Piece.WHITE_PAWN, view.lastBoard.getPiece(Square.E4));
        assertEquals(Square.NONE, view.selectedSquare);
        assertEquals(Collections.emptyList(), view.legalTargets);
    }

    @Test
    void droppingOnSameSquareCancelsCarryWithoutMoving() {
        RecordingChessBoardView view = new RecordingChessBoardView();
        ChessController controller = new ChessController(view);

        controller.toggleCarryAt(Square.E2);
        String carriedSymbolAfterDrop = controller.toggleCarryAt(Square.E2);

        assertNull(carriedSymbolAfterDrop);
        assertEquals(Square.NONE, view.hiddenPieceSquare);
        assertEquals(Side.WHITE, view.lastBoard.getSideToMove());
        assertEquals(Piece.WHITE_PAWN, view.lastBoard.getPiece(Square.E2));
        assertEquals(Square.NONE, view.selectedSquare);
        assertEquals(Collections.emptyList(), view.legalTargets);
    }

    @Test
    void illegalDropKeepsPieceCarriedAndSelectionVisible() {
        RecordingChessBoardView view = new RecordingChessBoardView();
        ChessController controller = new ChessController(view);

        controller.toggleCarryAt(Square.E2);
        String carriedSymbolAfterDrop = controller.toggleCarryAt(Square.E5);

        assertEquals(Piece.WHITE_PAWN.getFanSymbol(), carriedSymbolAfterDrop);
        assertEquals(Square.E2, view.hiddenPieceSquare);
        assertEquals(Side.WHITE, view.lastBoard.getSideToMove());
        assertEquals(Piece.WHITE_PAWN, view.lastBoard.getPiece(Square.E2));
        assertEquals(Square.E2, view.selectedSquare);
        assertEquals(List.of(Square.E3, Square.E4), view.legalTargets);
    }

    private static class RecordingChessBoardView extends ChessBoardView {
        private int updateCount;
        private Board lastBoard;
        private Square hiddenPieceSquare = Square.NONE;
        private Square selectedSquare = Square.NONE;
        private List<Square> legalTargets = Collections.emptyList();

        @Override
        public void update(Board board) {
            updateCount++;
            lastBoard = board;
        }

        @Override
        public void setHiddenPieceSquare(Square hiddenPieceSquare) {
            this.hiddenPieceSquare = hiddenPieceSquare == null ? Square.NONE : hiddenPieceSquare;
        }

        @Override
        public void showSelection(Square selectedSquare, List<Square> legalTargets) {
            this.selectedSquare = selectedSquare;
            this.legalTargets = new ArrayList<>(legalTargets);
        }
    }
}
