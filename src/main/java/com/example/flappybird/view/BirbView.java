package com.example.flappybird.view;

import com.example.flappybird.model.Birb;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Objects;

public class BirbView {
    private final Group root;
    private final ImageView imageView;
    private final Text carriedPieceText;

    public BirbView() {
        this(false);
    }

    public BirbView(boolean darkVariant) {
        Image wingsImage = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/images/wings.png")
                ).toExternalForm()
        );

        imageView = new ImageView(wingsImage);
        if (darkVariant) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-0.35);
            colorAdjust.setBrightness(-0.35);
            colorAdjust.setContrast(0.35);
            imageView.setEffect(colorAdjust);
        }

        carriedPieceText = new Text();
        carriedPieceText.setFont(Font.font("Serif", FontWeight.BOLD, 34));
        carriedPieceText.setFill(Color.WHITE);
        carriedPieceText.setStroke(Color.BLACK);
        carriedPieceText.setStrokeWidth(1.5);
        carriedPieceText.setVisible(false);

        root = new Group(imageView, carriedPieceText);
        root.setMouseTransparent(true);
    }

    public void render(Birb bird) {
        imageView.setFitWidth(bird.getWidth());
        imageView.setFitHeight(bird.getHeight());
        imageView.setX(bird.getX());
        imageView.setY(bird.getY());

        carriedPieceText.setX(bird.getX() + bird.getWidth() / 2 - 13);
        carriedPieceText.setY(bird.getY() + bird.getHeight() / 2 + 12);
    }

    public void setCarriedPieceSymbol(String symbol) {
        carriedPieceText.setText(symbol == null ? "" : symbol);
        carriedPieceText.setVisible(symbol != null && !symbol.isEmpty());
    }

    public Group getNode() {
        return root;
    }
}
