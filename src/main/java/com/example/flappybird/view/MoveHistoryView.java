package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MoveHistoryView {
    private static final Color BACKGROUND = Color.web("#05060d");
    private static final Color PANEL_STROKE = Color.web("#111827");
    private static final Color TEXT = Color.web("#d1d5db");
    private static final Color MUTED = Color.web("#4b5563");

    private final Pane root;
    private final List<MoveRow> moves = new ArrayList<>();
    private final List<Text> rowTexts = new ArrayList<>();

    public MoveHistoryView(double width, double height) {
        root = new Pane();
        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);

        Rectangle background = new Rectangle(width, height);
        background.setFill(BACKGROUND);

        Rectangle border = new Rectangle(width, height);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(PANEL_STROKE);
        border.setStrokeWidth(2);

        Text title = createText("MOVE HISTORY", 13, FontWeight.BOLD);
        title.setFill(TEXT);
        title.setX(14);
        title.setY(28);

        Text header = createText("NO.   WHITE    BLACK", 9, FontWeight.BOLD);
        header.setFill(MUTED);
        header.setX(14);
        header.setY(64);

        Rectangle divider = new Rectangle(14, 78, width - 28, 1);
        divider.setFill(Color.web("#1f2937"));

        root.getChildren().addAll(background, border, title, header, divider);
    }

    public Pane getNode() {
        return root;
    }

    public void addMove(Side side, String notation) {
        if (notation == null || notation.isBlank()) {
            return;
        }

        if (side == Side.BLACK && !moves.isEmpty() && moves.get(moves.size() - 1).blackMove == null) {
            MoveRow lastMove = moves.get(moves.size() - 1);
            moves.set(moves.size() - 1, new MoveRow(lastMove.number, lastMove.whiteMove, notation));
        } else {
            int moveNumber = moves.size() + 1;
            String whiteMove = side == Side.WHITE ? notation : null;
            String blackMove = side == Side.BLACK ? notation : null;
            moves.add(new MoveRow(moveNumber, whiteMove, blackMove));
        }

        renderRows();
    }

    private void renderRows() {
        root.getChildren().removeAll(rowTexts);
        rowTexts.clear();

        int firstVisibleMove = Math.max(0, moves.size() - 26);
        for (int i = firstVisibleMove; i < moves.size(); i++) {
            MoveRow move = moves.get(i);
            Text rowText = createText(formatMoveRow(move), 13, FontWeight.SEMI_BOLD);
            rowText.setFont(Font.font("Monospaced", FontWeight.SEMI_BOLD, 13));
            rowText.setFill(TEXT);
            rowText.setX(14);
            rowText.setY(104 + (i - firstVisibleMove) * 24);
            rowTexts.add(rowText);
        }

        root.getChildren().addAll(rowTexts);
    }

    private String formatMoveRow(MoveRow move) {
        return String.format(
                "%2d.  %-7s  %-7s",
                move.number,
                displayMove(move.whiteMove),
                displayMove(move.blackMove)
        );
    }

    private String displayMove(String move) {
        return move == null ? "" : move;
    }

    private Text createText(String value, int size, FontWeight weight) {
        Text text = new Text(value);
        text.setFont(Font.font("System", weight, size));
        return text;
    }

    private record MoveRow(int number, String whiteMove, String blackMove) {
    }
}
