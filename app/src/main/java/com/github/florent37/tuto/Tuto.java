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
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.florent37.tuto.shapes.Circle;
import com.github.florent37.tuto.shapes.RoundRect;

public final class Tuto {

    public static final float DEFAULT_ADDITIONAL_RADIUS_RATIO = 1.5f;
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

    @Nullable
    private View findViewById(@IdRes int viewId) {
        Context context = tutoView.getContext();
        View view = null;
        if (context instanceof Activity) {
            view = ((Activity) context).findViewById(viewId);
        }
        return view;

    }

    public ViewActions on(@IdRes int viewId) {
        return new ViewActions(this, findViewById(viewId));
    }

    public ViewActions on(View view) {
        return new ViewActions(this, view);
    }

    public static class ViewActions {
        private final Tuto tuto;
        private final View view;
        private boolean animated = true;
        @Nullable
        private View.OnClickListener onClickListener;

        public ViewActions(final Tuto tuto, View view) {
            this.tuto = tuto;
            this.view = view;
        }

        public ViewActions on(@IdRes int viewId) {
            return tuto.on(viewId);
        }

        public ViewActions on(View view) {
            return tuto.on(view);
        }

        public Tuto show() {
            return tuto.show();
        }


        private void displaySwipable(final boolean left) {
            final Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);

            final ImageView hand = new ImageView(view.getContext());
            if (left) {
                hand.setImageResource(R.drawable.finger_moving_left);
            } else {
                hand.setImageResource(R.drawable.finger_moving_right);
            }
            hand.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            hand.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    int x = (int) (rect.centerX() - hand.getWidth() / 2f);
                    int y = (int) (rect.centerY() - hand.getHeight() / 2f);

                    ViewCompat.setTranslationY(hand, y);
                    ViewCompat.setTranslationX(hand, x);

                    if (animated) {
                        float tX;
                        if (left) {
                            tX = rect.left;
                        } else {
                            tX = rect.left + rect.width() * 0.7f;
                        }
                        ViewCompat.animate(hand).translationX(tX).setStartDelay(500).setDuration(600).setInterpolator(new DecelerateInterpolator());
                    }

                    hand.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });

            tuto.container.addView(hand);
            tuto.container.invalidate();
        }

        public ViewActionsEditor displaySwipableLeft() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displaySwipable(true);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ViewActionsEditor(this);
        }

        public ViewActionsEditor displaySwipableRight() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displaySwipable(false);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ViewActionsEditor(this);
        }

        private void displayScrollable() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displayScrollableOnView();
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }

        private ViewActionsEditor displayScrollableOnView() {
            final Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            final int height = rect.height();

            final ImageView hand = new ImageView(view.getContext());
            hand.setImageResource(R.drawable.finger_moving_down);
            hand.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            hand.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    int x = (int) (rect.centerX() - hand.getWidth() / 2f);
                    int y = (int) (rect.centerY() - hand.getHeight() / 2f);

                    ViewCompat.setTranslationY(hand, y);
                    ViewCompat.setTranslationX(hand, x);

                    if (animated)
                        ViewCompat.animate(hand).translationY(y + height * 0.8f).setStartDelay(500).setDuration(600).setInterpolator(new DecelerateInterpolator());

                    hand.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });

            tuto.container.addView(hand);
            tuto.container.invalidate();

            return new ViewActionsEditor(this);
        }


        private void addCircleOnView(float additionalRadiusRatio) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);

            int cx = rect.centerX();
            int cy = rect.centerY() - getStatusBarHeight();
            int radius = (int) (Math.max(rect.width(), rect.height()) / 2f * additionalRadiusRatio);
            tuto.tutoView.addCircle(new Circle(cx, cy, radius));

            addClickableView(rect, onClickListener, additionalRadiusRatio);

            tuto.tutoView.postInvalidate();
        }

        public ViewActionsEditor addRoundRect() {
            return addRoundRect(DEFAULT_ADDITIONAL_RADIUS_RATIO);
        }

        public ViewActionsEditor addRoundRect(final float additionalRadiusRatio) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    addRoundRectOnView(additionalRadiusRatio);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ViewActionsEditor(this);
        }

        public ViewActionsEditor addCircle() {
            return addCircle(DEFAULT_ADDITIONAL_RADIUS_RATIO);
        }

        public ViewActionsEditor addCircle(final float additionalRadiusRatio) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    addCircleOnView(additionalRadiusRatio);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ViewActionsEditor(this);
        }

        private void addRoundRectOnView(float additionalRadiusRatio) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);

            int padding = 40;

            final int x = rect.left - padding;
            final int y = rect.top - getStatusBarHeight() - padding;
            final int width = rect.width() + 2 * padding;
            final int height = rect.height() + 2 * padding;
            final float ry = height / 2f;
            final float rx = ry;

            tuto.tutoView.addRoundRect(new RoundRect(x, y, width, height, rx, ry));
            addClickableView(rect, onClickListener, additionalRadiusRatio);
            tuto.tutoView.postInvalidate();
        }

        private int getStatusBarHeight() {
            int result = 0;
            Context context = view.getContext();
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        private void addClickableView(Rect rect, View.OnClickListener onClickListener, float additionalRadiusRatio) {
            View cliclableView = new View(this.view.getContext());
            int width = (int) (rect.width() * additionalRadiusRatio);
            int height = (int) (rect.height() * additionalRadiusRatio);
            int x = rect.left - (width - rect.width()) / 2;
            int y = rect.top - (height - rect.height()) / 2 - getStatusBarHeight();
            cliclableView.setLayoutParams(new ViewGroup.MarginLayoutParams(width, height));
            ViewCompat.setTranslationY(cliclableView, y);
            ViewCompat.setTranslationX(cliclableView, x);
            cliclableView.setOnClickListener(onClickListener);
            tuto.container.addView(cliclableView);
            tuto.container.invalidate();
        }

    }

    static class ViewActionsEditor {
        private final ViewActions viewActions;

        public ViewActionsEditor(ViewActions viewActions) {
            this.viewActions = viewActions;
        }

        public ViewActionsEditor animated(boolean animated) {
            this.viewActions.animated = animated;
            return this;
        }

        public ViewActionsEditor onClick(View.OnClickListener onClickListener){
            this.viewActions.onClickListener = onClickListener;
            return this;
        }

        public ViewActions on(@IdRes int viewId) {
            return viewActions.on(viewId);
        }

        public ViewActions on(View view) {
            return viewActions.on(view);
        }

        public Tuto show() {
            return viewActions.show();
        }

    }

}
