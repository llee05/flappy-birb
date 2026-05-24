package com.example.flappybird.view;

import com.example.flappybird.model.Birb;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class BirbView {
    private final ImageView imageView;

    public BirbView() {
        Image birdImage = new Image(
                Objects.requireNonNull(
                        getClass().getResource("/images/bird.jpeg")
                ).toExternalForm()
        );

        imageView = new ImageView(birdImage);
        imageView.setMouseTransparent(true);
    }

    public void render(Birb bird) {
        imageView.setFitWidth(bird.getWidth());
        imageView.setFitHeight(bird.getHeight());
        imageView.setX(bird.getX());
        imageView.setY(bird.getY());
    }

    public ImageView getNode() {
        return imageView;
    }
}
