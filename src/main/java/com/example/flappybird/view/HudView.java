package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HudView {
    private static final Color BACKGROUND = Color.web("#111827");
    private static final Color PANEL_STROKE = Color.web("#374151");
    private static final Color ACTIVE = Color.web("#facc15");
    private static final Color INACTIVE = Color.web("#6b7280");
    private static final Color TEXT = Color.web("#e5e7eb");

    private final Pane root;
    private final Circle whiteIndicator;
    private final Circle blackIndicator;
    private final Text sideToMoveText;

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

        Text title = createText("FLAPPY CHESS", 18, FontWeight.BOLD);
        title.setX(22);
        title.setY(46);

        Text label = createText("SIDE TO MOVE", 12, FontWeight.BOLD);
        label.setFill(Color.web("#9ca3af"));
        label.setX(22);
        label.setY(100);

        sideToMoveText = createText("", 24, FontWeight.BOLD);
        sideToMoveText.setX(22);
        sideToMoveText.setY(136);

        whiteIndicator = createIndicator(32, 188);
        blackIndicator = createIndicator(32, 240);

        Text whiteLabel = createText("White", 16, FontWeight.SEMI_BOLD);
        whiteLabel.setX(56);
        whiteLabel.setY(194);

        Text blackLabel = createText("Black", 16, FontWeight.SEMI_BOLD);
        blackLabel.setX(56);
        blackLabel.setY(246);

        root.getChildren().addAll(
                background,
                border,
                title,
                label,
                sideToMoveText,
                whiteIndicator,
                blackIndicator,
                whiteLabel,
                blackLabel
        );

        updateSideToMove(Side.WHITE);
    }

    public Pane getNode() {
        return root;
    }

    public void updateSideToMove(Side sideToMove) {
        boolean whiteActive = sideToMove == Side.WHITE;
        boolean blackActive = sideToMove == Side.BLACK;

        whiteIndicator.setFill(whiteActive ? ACTIVE : INACTIVE);
        blackIndicator.setFill(blackActive ? ACTIVE : INACTIVE);
        sideToMoveText.setText(whiteActive ? "White" : "Black");
    }

    private Circle createIndicator(double centerX, double centerY) {
        Circle indicator = new Circle(centerX, centerY, 7);
        indicator.setStroke(Color.web("#f9fafb"));
        indicator.setStrokeWidth(1.5);
        return indicator;
    }

    private Text createText(String value, int size, FontWeight weight) {
        Text text = new Text(value);
        text.setFill(TEXT);
        text.setFont(Font.font("System", weight, size));
        return text;
    }
}
