package com.example.flappybird.controller;

import com.example.flappybird.model.Birb;
import javafx.scene.input.KeyCode;

public class BirbController {
    private final Birb bird;
    private final double maxWidth;
    private final double maxHeight;
    private final KeyCode leftKey;
    private final KeyCode rightKey;
    private final KeyCode jumpKey;

    private boolean jumpPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    public BirbController(Birb bird, double maxWidth, double maxHeight) {
        this(bird, maxWidth, maxHeight, KeyCode.A, KeyCode.D, KeyCode.SPACE);
    }

    public BirbController(Birb bird, double maxWidth, double maxHeight, KeyCode leftKey, KeyCode rightKey, KeyCode jumpKey) {
        this.bird = bird;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.jumpKey = jumpKey;
    }

    public void handleKeyPressed(KeyCode keyCode) {
        if (keyCode == jumpKey) {
            if (!jumpPressed) {
                bird.jump();
            }
            jumpPressed = true;
        }

        if (keyCode == leftKey) {
            leftPressed = true;
        }

        if (keyCode == rightKey) {
            rightPressed = true;
        }
    }

    public void handleKeyReleased(KeyCode keyCode) {
        if (keyCode == jumpKey) {
            jumpPressed = false;
        }

        if (keyCode == leftKey) {
            leftPressed = false;
        }

        if (keyCode == rightKey) {
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
