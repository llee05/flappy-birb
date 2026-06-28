package com.example.flappybird.view;

import com.github.bhlangonijr.chesslib.Side;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PromotionChoiceView {
    private static final Color BACKDROP = Color.rgb(3, 7, 18, 0.52);
    private static final Color PANEL = Color.rgb(8, 13, 25, 0.96);
    private static final Color PANEL_STROKE = Color.web("#22c55e");
    private static final Color TITLE = Color.web("#facc15");
    private static final Color TEXT = Color.web("#e5e7eb");
    private static final Color MUTED = Color.web("#9ca3af");

    private final StackPane root;
    private final Text sideText;

    public PromotionChoiceView(double width, double height) {
        root = new StackPane();
        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);
        root.setVisible(false);
        root.setMouseTransparent(true);

        Rectangle backdrop = new Rectangle(width, height);
        backdrop.setFill(BACKDROP);

        Rectangle panel = new Rectangle(360, 190);
        panel.setArcWidth(10);
        panel.setArcHeight(10);
        panel.setFill(PANEL);
        panel.setStroke(PANEL_STROKE);
        panel.setStrokeWidth(1.4);

        Text title = createText("PROMOTE PAWN", 26, FontWeight.BOLD, TITLE);
        title.setX(36);
        title.setY(54);

        sideText = createText("", 12, FontWeight.BOLD, MUTED);
        sideText.setX(38);
        sideText.setY(82);

        Text choices = createText("Q  Queen    R  Rook", 15, FontWeight.SEMI_BOLD, TEXT);
        choices.setFont(Font.font("Monospaced", FontWeight.SEMI_BOLD, 15));
        choices.setX(38);
        choices.setY(122);

        Text secondRow = createText("B  Bishop   N  Knight", 15, FontWeight.SEMI_BOLD, TEXT);
        secondRow.setFont(Font.font("Monospaced", FontWeight.SEMI_BOLD, 15));
        secondRow.setX(38);
        secondRow.setY(152);

        Group panelContent = new Group(panel, title, sideText, choices, secondRow);
        root.getChildren().addAll(backdrop, panelContent);
        StackPane.setAlignment(panelContent, Pos.CENTER);
    }

    public StackPane getNode() {
        return root;
    }

    public void show(Side side) {
        sideText.setText((side == Side.WHITE ? "WHITE" : "BLACK") + " TO CHOOSE");
        root.setVisible(true);
        root.setMouseTransparent(false);
    }

    public void hide() {
        root.setVisible(false);
        root.setMouseTransparent(true);
    }

    private Text createText(String value, int size, FontWeight weight, Color color) {
        Text text = new Text(value);
        text.setFill(color);
        text.setFont(Font.font("System", weight, size));
        return text;
    }
}
