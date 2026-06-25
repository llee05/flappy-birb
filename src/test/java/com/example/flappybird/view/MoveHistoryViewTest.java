package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveHistoryViewTest {
    @Test
    void addMovePairsWhiteAndBlackMovesInRows() {
        MoveHistoryView view = new MoveHistoryView(200, 800);

        view.addMove(Side.WHITE, "e2-e4");
        view.addMove(Side.BLACK, "e7-e5");

        assertEquals(List.of("1. e2-e4 e7-e5"), moveRows(view));
    }

    @Test
    void addMoveIgnoresBlankNotation() {
        MoveHistoryView view = new MoveHistoryView(200, 800);

        view.addMove(Side.WHITE, " ");

        assertEquals(List.of(), moveRows(view));
    }

    private List<String> moveRows(MoveHistoryView view) {
        return view.getNode().getChildren().stream()
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .map(Text::getText)
                .filter(text -> text.matches("\\s*\\d+\\..*"))
                .map(text -> text.replaceAll("\\s+", " ").trim())
                .toList();
    }
}
