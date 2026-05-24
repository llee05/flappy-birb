package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ChessBoardView {
    private static final int BOARD_SIZE = 8;
    private static final int TILE_SIZE = 100;

    private final GridPane root = new GridPane();
    private final StackPane[][] squares = new StackPane[BOARD_SIZE][BOARD_SIZE];

    public ChessBoardView() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane square = new StackPane();

                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0
                        ? Color.web("#f0d9b5")
                        : Color.web("#b58863"));
                square.getChildren().add(tile);

                squares[row][col] = square;
                root.add(square, col, row);

            }
        }
    }

    public GridPane getNode() {
        return root;
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
            if (piece == Piece.NONE) {
                continue;
            }

            int row = BOARD_SIZE - 1 - square.getRank().ordinal();
            int col = square.getFile().ordinal();
            addPiece(row, col, piece.getFanSymbol());
        }
    }

    private void clearPieces() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].getChildren().removeIf(node -> node instanceof Text);
            }
        }
    }
}
