package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HudView {
    private static final Color BACKGROUND = Color.web("#05060d");
    private static final Color PANEL_STROKE = Color.web("#111827");
    private static final Color ACTIVE_GREEN = Color.web("#22c55e");
    private static final Color ACTIVE_TEXT = Color.web("#00ff85");
    private static final Color ACTIVE_FILL = Color.web("#062414");
    private static final Color INACTIVE_FILL = Color.web("#0a0b13");
    private static final Color INACTIVE_STROKE = Color.web("#24283a");
    private static final Color INACTIVE_TEXT = Color.web("#4b5168");
    private static final Color TEXT = Color.web("#d1d5db");
    private static final Color MUTED = Color.web("#4b5563");

    private final Pane root;
    private final Rectangle blackTimerCard;
    private final Rectangle whiteTimerCard;
    private final Text blackTimerText;
    private final Text whiteTimerText;
    private final Text blackStatusText;
    private final Text whiteStatusText;

    public HudView(double width, double height) {
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

        Text blackPlayer = createText("• BLACK", 9, FontWeight.BOLD);
        blackPlayer.setFill(MUTED);
        blackPlayer.setX(14);
        blackPlayer.setY(22);

        blackTimerCard = createTimerCard(12, 36, width - 24, 56);
        blackTimerCard.setId("blackTimerCard");
        blackTimerText = createTimerText("04:45", 24);
        blackTimerText.setId("blackTimerText");
        blackTimerText.setX(28);
        blackTimerText.setY(72);

        blackStatusText = createTinyText("RUNNING");
        blackStatusText.setX(28);
        blackStatusText.setY(84);

        Rectangle firstDivider = createDivider(width, 118);

        Text whitePlayer = createText("▲ WHITE", 9, FontWeight.BOLD);
        whitePlayer.setFill(MUTED);
        whitePlayer.setX(14);
        whitePlayer.setY(height - 78);

        whiteTimerCard = createTimerCard(12, height - 58, width - 24, 46);
        whiteTimerCard.setId("whiteTimerCard");
        whiteTimerText = createTimerText("04:45", 22);
        whiteTimerText.setId("whiteTimerText");
        whiteTimerText.setX(28);
        whiteTimerText.setY(height - 28);

        whiteStatusText = createTinyText("RUNNING");
        whiteStatusText.setX(28);
        whiteStatusText.setY(height - 16);

        root.getChildren().addAll(
                background,
                border,
                blackPlayer,
                blackTimerCard,
                blackTimerText,
                blackStatusText,
                firstDivider,
                whitePlayer,
                whiteTimerCard,
                whiteTimerText,
                whiteStatusText
        );

        updateSideToMove(Side.WHITE);
    }

    public Pane getNode() {
        return root;
    }

    public void updateSideToMove(Side sideToMove) {
        setTimerActive(whiteTimerCard, whiteTimerText, sideToMove == Side.WHITE);
        setTimerActive(blackTimerCard, blackTimerText, sideToMove == Side.BLACK);
        whiteStatusText.setVisible(sideToMove == Side.WHITE);
        blackStatusText.setVisible(sideToMove == Side.BLACK);
    }

    private Rectangle createTimerCard(double x, double y, double width, double height) {
        Rectangle timerCard = new Rectangle(x, y, width, height);
        timerCard.setArcWidth(7);
        timerCard.setArcHeight(7);
        timerCard.setStrokeWidth(1);
        return timerCard;
    }

    private Text createTimerText(String value, int size) {
        Text text = createText(value, size, FontWeight.BOLD);
        text.setFont(Font.font("Monospaced", FontWeight.BOLD, size));
        return text;
    }

    private Rectangle createDivider(double width, double y) {
        Rectangle divider = new Rectangle(14, y, width - 28, 1);
        divider.setFill(Color.web("#1f2937"));
        return divider;
    }

    private Text createTinyText(String value) {
        Text text = createText(value, 8, FontWeight.BOLD);
        text.setFill(MUTED);
        return text;
    }

    private void setTimerActive(Rectangle timerCard, Text timerText, boolean active) {
        timerCard.setFill(active ? ACTIVE_FILL : INACTIVE_FILL);
        timerCard.setStroke(active ? ACTIVE_GREEN : INACTIVE_STROKE);
        timerText.setFill(active ? ACTIVE_TEXT : INACTIVE_TEXT);

        if (active) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.rgb(34, 197, 94, 0.65));
            glow.setRadius(22);
            glow.setSpread(0.25);
            timerCard.setEffect(glow);
        } else {
            timerCard.setEffect(null);
        }
    }

    private Text createText(String value, int size, FontWeight weight) {
        Text text = new Text(value);
        text.setFill(TEXT);
        text.setFont(Font.font("System", weight, size));
        return text;
    }
}
