package com.github.florent37.tuto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class TutoView extends View {

    static final int DEFAULT_ALPHA_COLOR = 220;
    int backgroundOverlayColor = Color.argb(DEFAULT_ALPHA_COLOR, 0, 0, 0);
    List<Circle> circles;

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
        this.circles.add(circle);
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
        for (Circle circle : circles) {
            canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), circle.getPaint());
        }

    }

    private void initialize() {
        circles = new ArrayList<>();

        setDrawingCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }
}
