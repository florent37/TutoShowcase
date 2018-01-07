/*
 *
 *  * Meetic
 *  * Copyright (c) 2016. All rights reserved.
 *
 */

package com.github.florent37.tutoshowcase.shapes;

import android.graphics.Canvas;

public class Circle extends Shape {
    private int x;
    private int y;
    private int radius;

    public Circle(int x, int y, int radius) {
        super();
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void drawOn(Canvas canvas) {
        if (isDisplayBorder()) {
            canvas.drawCircle(getX(), getY(), getRadius() * 1.2f, getBorderPaint());
        }

        canvas.drawCircle(getX(), getY(), getRadius(), getPaint());
    }
}
