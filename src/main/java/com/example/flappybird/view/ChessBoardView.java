package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Square;
import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.function.BiConsumer;

public class ChessBoardView {
    private static final int BOARD_SIZE = 8;
    private static final int TILE_SIZE = 100;
    private static final Color LIGHT_TILE = Color.web("#f0d9b5");
    private static final Color DARK_TILE = Color.web("#b58863");
    private static final Color SELECTED_STROKE = Color.web("#2563eb");
    private static final Color LEGAL_TARGET_STROKE = Color.web("#16a34a");

    private final GridPane root = new GridPane();
    private final StackPane[][] squares = new StackPane[BOARD_SIZE][BOARD_SIZE];
    private final Rectangle[][] tiles = new Rectangle[BOARD_SIZE][BOARD_SIZE];
    private Square hiddenPieceSquare = Square.NONE;
    private BiConsumer<Integer, Integer> squareClickHandler;

    public ChessBoardView() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane square = new StackPane();

                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? LIGHT_TILE : DARK_TILE);
                tile.setStroke(Color.TRANSPARENT);
                tile.setStrokeType(StrokeType.INSIDE);
                square.getChildren().add(tile);

                int squareRow = row;
                int squareCol = col;
                square.setOnMouseClicked(event -> {
                    if (squareClickHandler != null) {
                        squareClickHandler.accept(squareRow, squareCol);
                    }
                });

                squares[row][col] = square;
                tiles[row][col] = tile;
                root.add(square, col, row);

            }
        }
    }

    public static Square squareForCell(int row, int col) {
        return Square.encode(Rank.allRanks[BOARD_SIZE - 1 - row], File.allFiles[col]);
    }

    public static Square squareForPoint(double x, double y) {
        if (x < 0 || y < 0) {
            return Square.NONE;
        }

        int col = (int) (x / TILE_SIZE);
        int row = (int) (y / TILE_SIZE);

        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return Square.NONE;
        }

        return squareForCell(row, col);
    }

    public GridPane getNode() {
        return root;
    }

    public void setSquareClickHandler(BiConsumer<Integer, Integer> squareClickHandler) {
        this.squareClickHandler = squareClickHandler;

        Cursor cursor = squareClickHandler == null ? Cursor.DEFAULT : Cursor.HAND;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].setCursor(cursor);
            }
        }
    }

    public void addPiece(int row, int col, String symbol) {
        Text piece = new Text(symbol);
        piece.setFont(Font.font("Serif", FontWeight.BOLD, 64));
        squares[row][col].getChildren().add(piece);
    }

    public void update(Board board) {
        clearPieces();

        for (Square square : Square.values()) {
            if (square == Square.NONE) {
                continue;
            }

            Piece piece = board.getPiece(square);
            if (piece == Piece.NONE || square == hiddenPieceSquare) {
                continue;
            }

            int row = BOARD_SIZE - 1 - square.getRank().ordinal();
            int col = square.getFile().ordinal();
            addPiece(row, col, piece.getFanSymbol());
        }
    }

    public void showSelection(Square selectedSquare, List<Square> legalTargets) {
        clearHighlights();

        if (selectedSquare != Square.NONE) {
            Rectangle selectedTile = tileForSquare(selectedSquare);
            selectedTile.setStroke(SELECTED_STROKE);
            selectedTile.setStrokeWidth(5);
        }

        for (Square target : legalTargets) {
            Rectangle targetTile = tileForSquare(target);
            targetTile.setStroke(LEGAL_TARGET_STROKE);
            targetTile.setStrokeWidth(4);
        }
    }

    public void setHiddenPieceSquare(Square hiddenPieceSquare) {
        this.hiddenPieceSquare = hiddenPieceSquare == null ? Square.NONE : hiddenPieceSquare;
    }

    private void clearPieces() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].getChildren().removeIf(node -> node instanceof Text);
            }
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                tiles[row][col].setStroke(Color.TRANSPARENT);
                tiles[row][col].setStrokeWidth(0);
            }
        }
    }

    private Rectangle tileForSquare(Square square) {
        int row = BOARD_SIZE - 1 - square.getRank().ordinal();
        int col = square.getFile().ordinal();
        return tiles[row][col];
    }
}
