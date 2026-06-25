package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HudViewTest {
    @Test
    void updateSideToMoveHighlightsWhite() {
        HudView view = new HudView(200, 800);

        view.updateSideToMove(Side.WHITE);

        assertEquals("White", sideToMoveText(view).getText());
        assertEquals(Color.web("#facc15"), indicators(view).get(0).getFill());
        assertEquals(Color.web("#6b7280"), indicators(view).get(1).getFill());
    }

    @Test
    void updateSideToMoveHighlightsBlack() {
        HudView view = new HudView(200, 800);

        view.updateSideToMove(Side.BLACK);

        assertEquals("Black", sideToMoveText(view).getText());
        assertEquals(Color.web("#6b7280"), indicators(view).get(0).getFill());
        assertEquals(Color.web("#facc15"), indicators(view).get(1).getFill());
    }

    private Text sideToMoveText(HudView view) {
        return view.getNode().getChildren().stream()
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .filter(text -> "White".equals(text.getText()) || "Black".equals(text.getText()))
                .findFirst()
                .orElseThrow();
    }

    private List<Circle> indicators(HudView view) {
        return view.getNode().getChildren().stream()
                .filter(Circle.class::isInstance)
                .map(Circle.class::cast)
                .toList();
    }
}
