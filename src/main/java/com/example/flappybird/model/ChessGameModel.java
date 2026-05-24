package com.example.flappybird.model;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.List;

public class ChessGameModel {
    private final Board board;
    private Piece[][] boardState;

    public ChessGameModel() {
        board = new Board();
        boardState = new Piece[8][8];
        this.update();
    }

    public Board getBoard() {
        return board;
    }

    private void update() {
        // could be redundant; consider moving this logic to view

        for (Square square : Square.values()) {
            if (square == Square.NONE) {
                continue;
            }
            Piece piece = board.getPiece(square);
            int row_index = square.getRank().ordinal();
            int col_index = square.getFile().ordinal();
            boardState[row_index][col_index] = piece;
        }
    }

    public Piece[][] getBoardState() {
        return boardState;
    }



    public List<Move> getLegalMoves() {
        return board.legalMoves();
    }

    public boolean tryMove(Move move) {
        if (board.isMoveLegal(move, true)) {
            board.doMove(move);
            update();
            return true;
        }
        return false;
    }
}
