/*
 *
 *  * Meetic
 *  * Copyright (c) 2016. All rights reserved.
 *
 */

package com.github.florent37.tuto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;

import com.github.florent37.tuto.shapes.Circle;

public final class Tuto {

    public static final float DEFAULT_ADDITIONAL_RADIUS_RATIO = 1.5f;
    public static final int SAFE_DELAY_UNTIL_INFLATED = 100;
    private static final String SHARED_TUTO = "SHARED_TUTO";
    private FrameLayout container;
    private TutoView tutoView;
    private SharedPreferences sharedPreferences;

    private Tuto(@NonNull Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences(SHARED_TUTO, Context.MODE_PRIVATE);
        this.container = new FrameLayout(activity);
        this.tutoView = new TutoView(activity);
        Window window = activity.getWindow();
        if (window != null) {
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            if (decorView != null) {
                ViewGroup content = (ViewGroup) decorView.findViewById(android.R.id.content);
                if (content != null) {
                    content.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                  this.container.addView(tutoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }
        }
        this.container.setVisibility(View.GONE);
        ViewCompat.setAlpha(container, 0f);
    }

    @NonNull
    public static Tuto from(@NonNull Activity activity) {
        return new Tuto(activity);
    }

    public Tuto setBackgroundColor(@ColorInt int color) {
        tutoView.setBackgroundOverlayColor(color);
        return this;
    }

    public Tuto setContentView(@LayoutRes int content) {
        View child = LayoutInflater.from(tutoView.getContext()).inflate(content, container, false);
        container.addView(child, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public void dismiss() {
        ViewCompat.animate(container)
            .alpha(0f)
            .setDuration(container.getResources().getInteger(android.R.integer.config_mediumAnimTime))
            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    super.onAnimationEnd(view);
                    ViewParent parent = view.getParent();
                    if (parent instanceof ViewGroup) {
                        ((ViewGroup) parent).removeView(view);
                    }
                }
            }).start();

    }

    public Tuto addCircle(final View view, final View.OnClickListener onClickListener, final float additionalRadiusRatio) {
        if (view != null) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    addCircleOnView(view, onClickListener, additionalRadiusRatio);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }
        return this;
    }

    public Tuto addCircle(View view, View.OnClickListener onClickListener) {
        return addCircle(view, onClickListener, DEFAULT_ADDITIONAL_RADIUS_RATIO);
    }

    public Tuto addCircle(@IdRes final int viewId, View.OnClickListener onClickListener) {
        return addCircle(viewId, onClickListener, DEFAULT_ADDITIONAL_RADIUS_RATIO);
    }

    public Tuto addCircle(@IdRes int viewId, View.OnClickListener onClickListener, float additionalRadiusRatio) {
        addCircleDelayed(viewId, onClickListener, additionalRadiusRatio);
        return this;
    }

    public Tuto withDismissView(@IdRes int viewId) {
        View view = container.findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        return this;
    }

    public Tuto onClick(int id, View.OnClickListener onClickListener) {
        View view = container.findViewById(id);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        return this;
    }

    public Tuto show() {
        container.setVisibility(View.VISIBLE);
        ViewCompat.animate(container)
            .alpha(1f)
            .setDuration(container.getResources().getInteger(android.R.integer.config_longAnimTime))
            .start();
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return this;
    }

    public Tuto showOnce(String key) {
        if (!sharedPreferences.contains(key)) {
            show();
            sharedPreferences.edit().putString(key, key).apply();
        }
        return this;
    }

    private int getStatusBarHeight() {
        int result = 0;
        Context context = tutoView.getContext();
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void addCircleDelayed(@IdRes final int viewId, final View.OnClickListener onClickListener, final float additionalRadiusRatio) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Context context = tutoView.getContext();
                if (context instanceof Activity) {
                    final View view = ((Activity)context).findViewById(viewId);
                    addCircleOnView(view, onClickListener, additionalRadiusRatio);
                }
            }
        }, SAFE_DELAY_UNTIL_INFLATED);
    }

    private void addRoundRectDelayed(@IdRes final int viewId, final View.OnClickListener onClickListener, final float additionalRadiusRatio) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Context context = tutoView.getContext();
                if (context instanceof Activity) {
                    final View view = ((Activity)context).findViewById(viewId);
                    addRoundRectOnView(view, onClickListener, additionalRadiusRatio);
                }
            }
        }, SAFE_DELAY_UNTIL_INFLATED);
    }

    private void addCircleOnView(View view, View.OnClickListener onClickListener, float additionalRadiusRatio) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        int cx = rect.centerX();
        int cy = rect.centerY() - getStatusBarHeight();
        int radius = (int) (Math.max(rect.width(), rect.height()) / 2f * additionalRadiusRatio);
        tutoView.addCircle(new Circle(cx, cy, radius));

        addClickableView(rect, onClickListener, additionalRadiusRatio);

        tutoView.postInvalidate();
    }

    private void addRoundRectOnView(View view, View.OnClickListener onClickListener, float additionalRadiusRatio) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        int cx = rect.centerX();
        int cy = rect.centerY() - getStatusBarHeight();
        int radius = (int) (Math.max(rect.width(), rect.height()) / 2f * additionalRadiusRatio);
        tutoView.addCircle(new Circle(cx, cy, radius));

        addClickableView(rect, onClickListener, additionalRadiusRatio);

        tutoView.postInvalidate();
    }

    private void addClickableView(Rect rect, View.OnClickListener onClickListener, float additionalRadiusRatio) {
        View view = new View(tutoView.getContext());
        int width = (int) (rect.width() * additionalRadiusRatio);
        int height = (int) (rect.height() * additionalRadiusRatio);
        int x = rect.left - (width - rect.width()) / 2;
        int y = rect.top - (height - rect.height()) / 2 - getStatusBarHeight();
        view.setLayoutParams(new ViewGroup.MarginLayoutParams(width, height));
        ViewCompat.setTranslationY(view, y);
        ViewCompat.setTranslationX(view, x);
        view.setOnClickListener(onClickListener);
        container.addView(view);
        container.invalidate();
    }
}
