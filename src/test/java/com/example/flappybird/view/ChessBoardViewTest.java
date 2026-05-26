package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChessBoardViewTest {
    @Test
    void squareForCellMapsViewRowsToChessRanks() {
        assertEquals(Square.A8, ChessBoardView.squareForCell(0, 0));
        assertEquals(Square.H8, ChessBoardView.squareForCell(0, 7));
        assertEquals(Square.A1, ChessBoardView.squareForCell(7, 0));
        assertEquals(Square.H1, ChessBoardView.squareForCell(7, 7));
        assertEquals(Square.E2, ChessBoardView.squareForCell(6, 4));
    }

    @Test
    void squareForPointMapsPixelsToBoardSquares() {
        assertEquals(Square.A8, ChessBoardView.squareForPoint(0, 0));
        assertEquals(Square.A8, ChessBoardView.squareForPoint(99.9, 99.9));
        assertEquals(Square.B8, ChessBoardView.squareForPoint(100, 0));
        assertEquals(Square.H1, ChessBoardView.squareForPoint(799.9, 799.9));
    }

    @Test
    void squareForPointReturnsNoneOutsideBoard() {
        assertEquals(Square.NONE, ChessBoardView.squareForPoint(-0.1, 0));
        assertEquals(Square.NONE, ChessBoardView.squareForPoint(0, -0.1));
        assertEquals(Square.NONE, ChessBoardView.squareForPoint(800, 799));
        assertEquals(Square.NONE, ChessBoardView.squareForPoint(799, 800));
    }

    @Test
    void updateRendersPiecesFromBoardAndHonorsHiddenPiece() {
        ChessBoardView view = new ChessBoardView();

        view.update(new Board());
        assertEquals(32, countRenderedPieces(view));

        view.setHiddenPieceSquare(Square.E2);
        view.update(new Board());

        assertEquals(31, countRenderedPieces(view));
        assertEquals(0, countRenderedPiecesAt(view, 6, 4));
    }

    private int countRenderedPieces(ChessBoardView view) {
        return view.getNode().getChildren().stream()
                .mapToInt(node -> countRenderedPieces((StackPane) node))
                .sum();
    }

    private int countRenderedPiecesAt(ChessBoardView view, int row, int col) {
        return view.getNode().getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col)
                .mapToInt(node -> countRenderedPieces((StackPane) node))
                .sum();
    }

    private int countRenderedPieces(StackPane square) {
        int count = 0;
        for (Node child : square.getChildren()) {
            if (child instanceof Text) {
                count++;
            }
        }
        return count;
    }
}
