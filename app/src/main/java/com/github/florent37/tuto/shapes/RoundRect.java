package com.github.florent37.tuto.shapes;

import android.graphics.Canvas;
import android.graphics.Path;

public class RoundRect extends Shape {

    private int x;
    private int y;
    private int width;
    private int height;
    private float rx;
    private float ry;

    public RoundRect(int x, int y, int width, int height, float rx, float ry) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rx = rx;
        this.ry = ry;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }

    @Override
    public void drawOn(Canvas canvas) {
        Path path = new Path();
        path.addRoundRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), getRx(), getRy(), Path.Direction.CW);
        canvas.drawPath(path, getPaint());
    }
}
