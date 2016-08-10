package com.github.florent37.tuto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.github.florent37.tuto.shapes.Circle;
import com.github.florent37.tuto.shapes.RoundRect;
import com.github.florent37.tuto.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

class TutoView extends View {

    static final int DEFAULT_ALPHA_COLOR = 200;
    int backgroundOverlayColor = Color.argb(DEFAULT_ALPHA_COLOR, 0, 0, 0);
    List<Shape> shapes;

    public TutoView(Context context) {
        super(context);
        initialize();
    }

    public TutoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TutoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void addCircle(Circle circle) {
        this.shapes.add(circle);
    }

    public void addRoundRect(RoundRect roundRect) {
        this.shapes.add(roundRect);
    }

    public int getBackgroundOverlayColor() {
        return backgroundOverlayColor;
    }

    public void setBackgroundOverlayColor(int backgroundOverlayColor) {
        this.backgroundOverlayColor = backgroundOverlayColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(backgroundOverlayColor);
        for (Shape shape : shapes) {
            shape.drawOn(canvas);
        }

    }

    private void initialize() {
        shapes = new ArrayList<>();

        setDrawingCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }
}
