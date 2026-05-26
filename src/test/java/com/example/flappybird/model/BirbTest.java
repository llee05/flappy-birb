package com.example.flappybird.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BirbTest {
    private static final double EPSILON = 0.0000001;

    @Test
    void startsAtGivenPositionAndSizeWithNoVerticalVelocity() {
        Birb birb = new Birb(10, 20, 30, 40);

        assertEquals(10, birb.getX(), EPSILON);
        assertEquals(20, birb.getY(), EPSILON);
        assertEquals(30, birb.getWidth(), EPSILON);
        assertEquals(40, birb.getHeight(), EPSILON);
        assertEquals(0, birb.getVelocityY(), EPSILON);
    }

    @Test
    void updateAppliesGravityAndMovesVertically() {
        Birb birb = new Birb(10, 20, 30, 40);

        birb.update();

        assertEquals(0.003, birb.getVelocityY(), EPSILON);
        assertEquals(20.003, birb.getY(), EPSILON);
        assertEquals(10, birb.getX(), EPSILON);
    }

    @Test
    void jumpAppliesUpwardVelocityBeforeGravityOnNextUpdate() {
        Birb birb = new Birb(10, 20, 30, 40);

        birb.jump();
        birb.update();

        assertEquals(-0.797, birb.getVelocityY(), EPSILON);
        assertEquals(19.203, birb.getY(), EPSILON);
    }

    @Test
    void fallSpeedIsCapped() {
        Birb birb = new Birb(0, 0, 30, 40);

        for (int i = 0; i < 1_000; i++) {
            birb.update();
        }

        assertEquals(1.0, birb.getVelocityY(), EPSILON);
    }

    @Test
    void horizontalInputAcceleratesTowardMoveSpeed() {
        Birb birb = new Birb(10, 20, 30, 40);

        birb.setHorizontalInput(1);
        for (int i = 0; i < 6; i++) {
            birb.update();
        }

        assertEquals(11.0, birb.getX(), EPSILON);
    }

    @Test
    void horizontalInputIsNormalizedToDirection() {
        Birb birb = new Birb(10, 20, 30, 40);

        birb.setHorizontalInput(999);
        birb.update();

        assertEquals(10.05, birb.getX(), EPSILON);
    }

    @Test
    void horizontalVelocityDecaysAfterInputStops() {
        Birb birb = new Birb(10, 20, 30, 40);

        birb.setHorizontalInput(1);
        for (int i = 0; i < 5; i++) {
            birb.update();
        }
        double movingRightX = birb.getX();

        birb.setHorizontalInput(0);
        birb.update();
        double firstDrift = birb.getX() - movingRightX;
        birb.update();
        double secondDrift = birb.getX() - movingRightX - firstDrift;

        assertTrue(firstDrift > 0);
        assertTrue(secondDrift > 0);
        assertTrue(secondDrift < firstDrift);
    }

    @Test
    void constrainToBoundsClampsPositionAndStopsOutwardVerticalMovement() {
        Birb birb = new Birb(-12, -4, 30, 40);
        birb.setVelocityX(-0.5);
        birb.jump();

        birb.constrainToBounds(800, 800);

        assertEquals(0, birb.getX(), EPSILON);
        assertEquals(0, birb.getY(), EPSILON);
        assertEquals(0, birb.getVelocityY(), EPSILON);
    }

    @Test
    void constrainToBoundsClampsRightAndBottomEdges() {
        Birb birb = new Birb(790, 790, 30, 40);

        birb.constrainToBounds(800, 800);

        assertEquals(770, birb.getX(), EPSILON);
        assertEquals(760, birb.getY(), EPSILON);
    }
}
