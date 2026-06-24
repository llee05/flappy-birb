package com.example.flappybird.controller;

import com.example.flappybird.model.Birb;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BirbControllerTest {
    private static final double EPSILON = 0.0000001;

    @Test
    void rightInputMovesBirdAndReleaseLetsItCoast() {
        Birb birb = new Birb(10, 20, 30, 40);
        BirbController controller = new BirbController(birb, 800, 800);

        controller.handleKeyPressed(KeyCode.D);
        for (int i = 0; i < 5; i++) {
            controller.update();
        }
        double movingRightX = birb.getX();
        double movingRightVelocity = birb.getVelocityX();

        controller.handleKeyReleased(KeyCode.D);
        controller.update();

        assertTrue(birb.getX() > movingRightX);
        assertTrue(birb.getVelocityX() > 0);
        assertTrue(birb.getVelocityX() < movingRightVelocity);
    }

    @Test
    void customKeyBindingsMoveAndJumpTheBird() {
        Birb birb = new Birb(10, 20, 30, 40);
        BirbController controller = new BirbController(
                birb,
                800,
                800,
                KeyCode.LEFT,
                KeyCode.RIGHT,
                KeyCode.UP
        );

        controller.handleKeyPressed(KeyCode.RIGHT);
        controller.handleKeyPressed(KeyCode.UP);
        controller.update();

        assertEquals(10.1, birb.getX(), EPSILON);
        assertEquals(-0.797, birb.getVelocityY(), EPSILON);
    }

    @Test
    void opposingHorizontalInputsCancelOut() {
        Birb birb = new Birb(10, 20, 30, 40);
        BirbController controller = new BirbController(birb, 800, 800);

        controller.handleKeyPressed(KeyCode.A);
        controller.handleKeyPressed(KeyCode.D);
        controller.update();

        assertEquals(10, birb.getX(), EPSILON);
        assertEquals(0, birb.getVelocityX(), EPSILON);
    }

    @Test
    void releasingOneOfOpposingHorizontalInputsUsesRemainingDirection() {
        Birb birb = new Birb(10, 20, 30, 40);
        BirbController controller = new BirbController(birb, 800, 800);

        controller.handleKeyPressed(KeyCode.A);
        controller.handleKeyPressed(KeyCode.D);
        controller.update();

        controller.handleKeyReleased(KeyCode.A);
        controller.update();

        assertEquals(10.1, birb.getX(), EPSILON);
        assertTrue(birb.getVelocityX() > 0);
    }

    @Test
    void heldJumpOnlyTriggersOnceUntilReleased() {
        Birb birb = new Birb(10, 20, 30, 40);
        BirbController controller = new BirbController(birb, 800, 800);

        controller.handleKeyPressed(KeyCode.SPACE);
        controller.update();

        assertEquals(-0.797, birb.getVelocityY(), EPSILON);

        controller.handleKeyPressed(KeyCode.SPACE);
        controller.update();

        assertEquals(-0.794, birb.getVelocityY(), EPSILON);

        controller.handleKeyReleased(KeyCode.SPACE);
        controller.handleKeyPressed(KeyCode.SPACE);
        controller.update();

        assertEquals(-0.797, birb.getVelocityY(), EPSILON);
    }

    @Test
    void updateConstrainsBirdToBounds() {
        Birb birb = new Birb(790, 790, 30, 40);
        BirbController controller = new BirbController(birb, 800, 800);

        controller.update();

        assertEquals(770, birb.getX(), EPSILON);
        assertEquals(760, birb.getY(), EPSILON);
    }
}
