package com.github.florent37.tuto.shapes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.CallSuper;

public abstract class Shape {
    private int color = Color.argb(0, 0, 0, 0);
    protected Paint paint;

    public Shape() {
        this.paint = new Paint();
        this.paint.setColor(getColor());
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(this.color);
    }

    public int getColor() {
        return color;
    }

    public Paint getPaint() {
        return paint;
    }

    public abstract void drawOn(Canvas canvas);
}
