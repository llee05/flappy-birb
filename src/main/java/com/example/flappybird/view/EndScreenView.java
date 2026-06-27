package com.example.flappybird.view;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class EndScreenView {
    private static final Color BACKDROP = Color.rgb(3, 7, 18, 0.82);
    private static final Color PANEL = Color.rgb(8, 13, 25, 0.94);
    private static final Color PANEL_STROKE = Color.web("#22c55e");
    private static final Color TITLE = Color.web("#facc15");
    private static final Color TEXT = Color.web("#e5e7eb");
    private static final Color MUTED = Color.web("#9ca3af");
    private static final Color RESTART_FILL = Color.web("#062414");

    private final StackPane root;
    private final Text titleText;
    private final Text reasonText;
    private final StackPane restartButton;

    public EndScreenView(double width, double height, Runnable restartAction) {
        root = new StackPane();
        root.setPrefSize(width, height);
        root.setMinSize(width, height);
        root.setMaxSize(width, height);
        root.setVisible(false);
        root.setMouseTransparent(true);

        Rectangle backdrop = new Rectangle(width, height);
        backdrop.setFill(BACKDROP);

        Rectangle panel = new Rectangle(420, 250);
        panel.setArcWidth(12);
        panel.setArcHeight(12);
        panel.setFill(PANEL);
        panel.setStroke(PANEL_STROKE);
        panel.setStrokeWidth(1.5);

        titleText = createText("", 34, FontWeight.BOLD, TITLE);
        reasonText = createText("", 15, FontWeight.SEMI_BOLD, MUTED);

        restartButton = createRestartButton(restartAction);

        Group panelContent = new Group(panel, titleText, reasonText, restartButton);
        titleText.setX(54);
        titleText.setY(74);
        reasonText.setX(54);
        reasonText.setY(116);
        restartButton.setLayoutX(54);
        restartButton.setLayoutY(158);

        root.getChildren().addAll(backdrop, panelContent);
        StackPane.setAlignment(panelContent, Pos.CENTER);
    }

    public StackPane getNode() {
        return root;
    }

    public void show(String title, String reason) {
        titleText.setText(title);
        reasonText.setText(reason);
        root.setVisible(true);
        root.setMouseTransparent(false);
    }

    private StackPane createRestartButton(Runnable restartAction) {
        Rectangle buttonBackground = new Rectangle(180, 46);
        buttonBackground.setArcWidth(8);
        buttonBackground.setArcHeight(8);
        buttonBackground.setFill(RESTART_FILL);
        buttonBackground.setStroke(PANEL_STROKE);
        buttonBackground.setStrokeWidth(1.2);

        Text buttonText = createText("RESTART", 16, FontWeight.BOLD, TEXT);
        StackPane button = new StackPane(buttonBackground, buttonText);
        button.setPrefSize(180, 46);
        button.setOnMouseClicked(event -> restartAction.run());
        return button;
    }

    private Text createText(String value, int size, FontWeight weight, Color color) {
        Text text = new Text(value);
        text.setFill(color);
        text.setFont(Font.font("System", weight, size));
        return text;
    }
}
