package com.example.flappybird.model;

public class Birb {
    private static final double GRAVITY = 0.03;
    private static final double JUMP_VELOCITY = -1.5;
    private static final double DASH_VELOCITY = 1.25;
    private static final double MAX_FALL_SPEED = 2.0;
    private static final double HORIZONTAL_DAMPING = 0.92;
    private static final double MIN_HORIZONTAL_SPEED = 0.02;

    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    private final double width;
    private final double height;

    public Birb(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public void jump() {
        velocityY = JUMP_VELOCITY;
    }

    public void dashLeft() {
        velocityX = -DASH_VELOCITY;
    }

    public void dashRight() {
        velocityX = DASH_VELOCITY;
    }

    public void update() {
        velocityY += GRAVITY;
        velocityY = Math.min(velocityY, MAX_FALL_SPEED);

        x += velocityX;
        y += velocityY;

        velocityX *= HORIZONTAL_DAMPING;
        if (Math.abs(velocityX) < MIN_HORIZONTAL_SPEED) {
            velocityX = 0;
        }
    }

    public void constrainToBounds(double maxWidth, double maxHeight) {
        if (x < 0) {
            x = 0;
            velocityX = Math.max(0, velocityX);
        } else if (x + width > maxWidth) {
            x = maxWidth - width;
            velocityX = Math.min(0, velocityX);
        }

        if (y < 0) {
            y = 0;
            velocityY = Math.max(0, velocityY);
        } else if (y + height > maxHeight) {
            y = maxHeight - height;
            velocityY = Math.min(0, velocityY);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVelocityX(double x) { this.velocityX = x; }
}
