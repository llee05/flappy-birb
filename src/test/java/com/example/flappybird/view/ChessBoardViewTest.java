package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChessBoardViewTest {
    private static final double EPSILON = 0.0000001;

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

    @Test
    void updateClearsPreviouslyRenderedPieces() {
        ChessBoardView view = new ChessBoardView();
        Board kingsOnlyBoard = new Board();
        kingsOnlyBoard.loadFromFen("4k3/8/8/8/8/8/8/4K3 w - - 0 1");

        view.update(new Board());
        view.update(kingsOnlyBoard);

        assertEquals(2, countRenderedPieces(view));
        assertEquals(1, countRenderedPiecesAt(view, 0, 4));
        assertEquals(1, countRenderedPiecesAt(view, 7, 4));
    }

    @Test
    void nullHiddenPieceSquareRestoresHiddenPieceOnNextUpdate() {
        ChessBoardView view = new ChessBoardView();
        Board board = new Board();

        view.setHiddenPieceSquare(Square.E2);
        view.update(board);

        assertEquals(0, countRenderedPiecesAt(view, 6, 4));

        view.setHiddenPieceSquare(null);
        view.update(board);

        assertEquals(1, countRenderedPiecesAt(view, 6, 4));
    }

    @Test
    void showSelectionHighlightsSelectedAndLegalTargetsUntilCleared() {
        ChessBoardView view = new ChessBoardView();

        view.showSelection(Square.E2, List.of(Square.E3, Square.E4));

        Rectangle selectedTile = tileAt(view, 6, 4);
        Rectangle firstTargetTile = tileAt(view, 5, 4);
        Rectangle secondTargetTile = tileAt(view, 4, 4);

        assertEquals(Color.web("#2563eb"), selectedTile.getStroke());
        assertEquals(5, selectedTile.getStrokeWidth(), EPSILON);
        assertEquals(Color.web("#16a34a"), firstTargetTile.getStroke());
        assertEquals(4, firstTargetTile.getStrokeWidth(), EPSILON);
        assertEquals(Color.web("#16a34a"), secondTargetTile.getStroke());
        assertEquals(4, secondTargetTile.getStrokeWidth(), EPSILON);

        view.showSelection(Square.NONE, List.of());

        assertEquals(Color.TRANSPARENT, selectedTile.getStroke());
        assertEquals(0, selectedTile.getStrokeWidth(), EPSILON);
        assertEquals(Color.TRANSPARENT, firstTargetTile.getStroke());
        assertEquals(0, firstTargetTile.getStrokeWidth(), EPSILON);
        assertEquals(Color.TRANSPARENT, secondTargetTile.getStroke());
        assertEquals(0, secondTargetTile.getStrokeWidth(), EPSILON);
    }

    @Test
    void squareClickHandlerReceivesCellAndUpdatesCursor() {
        ChessBoardView view = new ChessBoardView();
        int[] clickedCell = {-1, -1};

        view.setSquareClickHandler((row, col) -> {
            clickedCell[0] = row;
            clickedCell[1] = col;
        });

        StackPane square = squareAt(view, 3, 5);
        assertEquals(Cursor.HAND, square.getCursor());

        square.getOnMouseClicked().handle(null);

        assertEquals(3, clickedCell[0]);
        assertEquals(5, clickedCell[1]);

        view.setSquareClickHandler(null);

        assertEquals(Cursor.DEFAULT, square.getCursor());
    }

    private int countRenderedPieces(ChessBoardView view) {
        return view.getNode().getChildren().stream()
                .mapToInt(node -> countRenderedPieces((StackPane) node))
                .sum();
    }

    private int countRenderedPiecesAt(ChessBoardView view, int row, int col) {
        return countRenderedPieces(squareAt(view, row, col));
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

    private StackPane squareAt(ChessBoardView view, int row, int col) {
        return (StackPane) view.getNode().getChildren().stream()
                .filter(node -> Integer.valueOf(row).equals(GridPane.getRowIndex(node)))
                .filter(node -> Integer.valueOf(col).equals(GridPane.getColumnIndex(node)))
                .findFirst()
                .orElseThrow();
    }

    private Rectangle tileAt(ChessBoardView view, int row, int col) {
        return (Rectangle) squareAt(view, row, col).getChildren().stream()
                .filter(Rectangle.class::isInstance)
                .findFirst()
                .orElseThrow();
    }
}
