package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HudViewTest {
    @Test
    void updateSideToMoveLightsWhiteTimer() {
        HudView view = new HudView(200, 800);

        view.updateSideToMove(Side.WHITE);

        assertEquals(Color.web("#22c55e"), timerCard(view, "whiteTimerCard").getStroke());
        assertEquals(Color.web("#00ff85"), timerText(view, "whiteTimerText").getFill());
        assertEquals(Color.web("#24283a"), timerCard(view, "blackTimerCard").getStroke());
        assertEquals(Color.web("#4b5168"), timerText(view, "blackTimerText").getFill());
        assertEquals(1, visibleRunningLabels(view).size());
    }

    @Test
    void updateSideToMoveLightsBlackTimer() {
        HudView view = new HudView(200, 800);

        view.updateSideToMove(Side.BLACK);

        assertEquals(Color.web("#24283a"), timerCard(view, "whiteTimerCard").getStroke());
        assertEquals(Color.web("#4b5168"), timerText(view, "whiteTimerText").getFill());
        assertEquals(Color.web("#22c55e"), timerCard(view, "blackTimerCard").getStroke());
        assertEquals(Color.web("#00ff85"), timerText(view, "blackTimerText").getFill());
        assertEquals(1, visibleRunningLabels(view).size());
    }

    @Test
    void updateTimersFormatsBothClockValues() {
        HudView view = new HudView(200, 800);

        view.updateTimers(284, -4);

        assertEquals("04:44", timerText(view, "whiteTimerText").getText());
        assertEquals("00:00", timerText(view, "blackTimerText").getText());
    }

    private Rectangle timerCard(HudView view, String id) {
        return view.getNode().getChildren().stream()
                .filter(Rectangle.class::isInstance)
                .map(Rectangle.class::cast)
                .filter(rectangle -> id.equals(rectangle.getId()))
                .findFirst()
                .orElseThrow();
    }

    private Text timerText(HudView view, String id) {
        return view.getNode().getChildren().stream()
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .filter(text -> id.equals(text.getId()))
                .findFirst()
                .orElseThrow();
    }

    private List<Text> visibleRunningLabels(HudView view) {
        return view.getNode().getChildren().stream()
                .filter(Text.class::isInstance)
                .map(Text.class::cast)
                .filter(text -> "RUNNING".equals(text.getText()))
                .filter(Text::isVisible)
                .toList();
    }
}
