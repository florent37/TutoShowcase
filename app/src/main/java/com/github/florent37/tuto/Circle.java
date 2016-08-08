/*
 *
 *  * Meetic
 *  * Copyright (c) 2016. All rights reserved.
 *
 */

package com.github.florent37.tuto;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class Circle {
    private final int circleColor = Color.argb(0, 0, 0, 0);
    private int x;
    private int y;
    private int radius;
    private Paint paint;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.paint = new Paint();

        this.paint = new Paint();
        this.paint.setColor(circleColor);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
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

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getCircleColor() {
        return circleColor;
    }
}
