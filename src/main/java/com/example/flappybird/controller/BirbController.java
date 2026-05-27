package com.example.flappybird.controller;

import com.example.flappybird.model.Birb;
import javafx.scene.input.KeyCode;

public class BirbController {
    private final Birb bird;
    private final double maxWidth;
    private final double maxHeight;

    private boolean jumpPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    public BirbController(Birb bird, double maxWidth, double maxHeight) {
        this.bird = bird;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public void handleKeyPressed(KeyCode keyCode) {
        if (keyCode == KeyCode.SPACE) {
            if (!jumpPressed) {
                bird.jump();
            }
            jumpPressed = true;
        }

        if (keyCode == KeyCode.A) {
            leftPressed = true;
        }

        if (keyCode == KeyCode.D) {
            rightPressed = true;
        }
    }

    public void handleKeyReleased(KeyCode keyCode) {
        if (keyCode == KeyCode.SPACE) {
            jumpPressed = false;
        }

        if (keyCode == KeyCode.A) {
            leftPressed = false;
        }

        if (keyCode == KeyCode.D) {
            rightPressed = false;
        }
    }

    public void update() {
        bird.setHorizontalInput(getHorizontalInput());
        bird.update();
        bird.constrainToBounds(maxWidth, maxHeight);
    }

    private int getHorizontalInput() {
        if (leftPressed == rightPressed) {
            return 0;
        }

        return leftPressed ? -1 : 1;
    }
}
