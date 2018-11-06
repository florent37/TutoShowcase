/*
 *
 *  * Meetic
 *  * Copyright (c) 2016. All rights reserved.
 *
 */

package com.github.florent37.tutoshowcase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
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

import com.example.tutoshowcase.R;
import com.github.florent37.tutoshowcase.shapes.Circle;
import com.github.florent37.tutoshowcase.shapes.Rectangle;
import com.github.florent37.tutoshowcase.shapes.RoundRect;

public final class TutoShowcase {

    public interface Listener {
        void onDismissed();
    }

    public static final float DEFAULT_ADDITIONAL_RADIUS_RATIO = 1.5f;
    private static final String SHARED_TUTO = "SHARED_TUTO";
    private FrameLayout container;
    private TutoView tutoView;
    private SharedPreferences sharedPreferences;
    private boolean fitsSystemWindows = false;
    private Listener listener;
    private View inflatedLayout;

    private TutoShowcase(@NonNull Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences(SHARED_TUTO, Context.MODE_PRIVATE);
        this.container = new FrameLayout(activity);
        this.tutoView = new TutoView(activity);
        Window window = activity.getWindow();
        if (window != null) {
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            if (decorView != null) {
                ViewGroup content = decorView.findViewById(android.R.id.content);
                if (content != null) {
                    content.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    this.container.addView(tutoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        View inflatedLayout = content.getChildAt(0);
                        this.fitsSystemWindows = inflatedLayout != null && inflatedLayout.getFitsSystemWindows();
                    }
                }
            }
        }
        this.container.setVisibility(View.GONE);
        ViewCompat.setAlpha(container, 0f);
    }

    @NonNull
    public static TutoShowcase from(@NonNull Activity activity) {
        return new TutoShowcase(activity);
    }

    public TutoShowcase setBackgroundColor(@ColorInt int color) {
        tutoView.setBackgroundOverlayColor(color);
        return this;
    }

    public TutoShowcase setFitsSystemWindows(boolean fitsSystemWindows) {
        this.fitsSystemWindows = fitsSystemWindows;
        return this;
    }

    public TutoShowcase setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public View getInflatedLayout() {
        return inflatedLayout;
    }

    public void setInflatedLayout(View inflatedLayout) {
        this.inflatedLayout = inflatedLayout;
    }

    public TutoShowcase setContentView(@LayoutRes int content) {
        inflatedLayout = LayoutInflater.from(tutoView.getContext()).inflate(content, container, false);
        container.addView(inflatedLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
                        if (listener != null) {
                            listener.onDismissed();
                        }
                    }
                }).start();

    }


    public TutoShowcase withDismissView(@IdRes int viewId) {
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

    public TutoShowcase onClickContentView(int id, View.OnClickListener onClickListener) {
        View view = container.findViewById(id);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
        return this;
    }

    public TutoShowcase show() {
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

    public TutoShowcase showOnce(String key) {
        if (!sharedPreferences.contains(key)) {
            show();
            sharedPreferences.edit().putString(key, key).apply();
        }
        return this;
    }

    public boolean isShowOnce(String key) {
        if (sharedPreferences.contains(key)) {
            return true;
        }

        return false;
    }

    public TutoShowcase resetShowOnce(String key) {
        sharedPreferences.edit().remove(key).apply();
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
        return new ViewActions(this, findViewById(viewId), fitsSystemWindows);
    }

    public ViewActions on(View view) {
        return new ViewActions(this, view, fitsSystemWindows);
    }

    private static class ViewActionsSettings {
        private boolean animated = true;
        private boolean withBorder = false;
        @Nullable
        private View.OnClickListener onClickListener;

        private Integer delay = 0;
        private Integer duration = 300;
    }

    public static class ViewActions {
        private final TutoShowcase tutoShowcase;
        private final View view;
        private final ViewActionsSettings settings;
        private final boolean fitsSystemWindow;

        public ViewActions(final TutoShowcase tutoShowcase, View view, boolean fitsSystemWindow) {
            this.tutoShowcase = tutoShowcase;
            this.view = view;
            this.settings = new ViewActionsSettings();
            this.fitsSystemWindow = fitsSystemWindow;
        }

        public ViewActions on(@IdRes int viewId) {
            return tutoShowcase.on(viewId);
        }

        public ViewActions on(View view) {
            return tutoShowcase.on(view);
        }

        public TutoShowcase show() {
            return tutoShowcase.show();
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

                    if (settings.animated) {
                        float tX;
                        if (left) {
                            tX = rect.left;
                        } else {
                            tX = rect.left + rect.width() * 0.7f;
                        }
                        ViewCompat.animate(hand).translationX(tX)
                                .setStartDelay(settings.delay != null ? settings.delay : 500)
                                .setDuration(settings.duration != null ? settings.duration : 600)
                                .setInterpolator(new DecelerateInterpolator());
                    }

                    hand.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });

            tutoShowcase.container.addView(hand);
            tutoShowcase.container.invalidate();
        }

        public ActionViewActionsEditor displaySwipableLeft() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displaySwipable(true);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ActionViewActionsEditor(this);
        }

        public ActionViewActionsEditor displaySwipableRight() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displaySwipable(false);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ActionViewActionsEditor(this);
        }

        public ActionViewActionsEditor displayScrollable() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    displayScrollableOnView();
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ActionViewActionsEditor(this);
        }

        private void displayScrollableOnView() {
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
                    int y = (int) (rect.centerY() - hand.getHeight() / 2f) - getStatusBarOffset();
                    ViewCompat.setTranslationY(hand, y);
                    ViewCompat.setTranslationX(hand, x);

                    if (settings.animated)
                        ViewCompat.animate(hand)
                                .translationY(y + height * 0.8f - getStatusBarOffset())
                                .setStartDelay(settings.delay != null ? settings.delay : 500)
                                .setDuration(settings.duration != null ? settings.duration : 600)
                                .setInterpolator(new DecelerateInterpolator());

                    hand.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });

            tutoShowcase.container.addView(hand);
            tutoShowcase.container.invalidate();
        }


        private void addCircleOnView(float additionalRadiusRatio) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);

            int cx = rect.centerX();
            int cy = rect.centerY() - getStatusBarOffset();
            int radius = (int) (Math.max(rect.width(), rect.height()) / 2f * additionalRadiusRatio);
            Circle circle = new Circle(cx, cy, radius);
            circle.setDisplayBorder(settings.withBorder);
            tutoShowcase.tutoView.addCircle(circle);

            addClickableView(rect, settings.onClickListener, additionalRadiusRatio);

            tutoShowcase.tutoView.postInvalidate();
        }

        public ShapeViewActionsEditor addRoundRect() {
            return addRoundRect(DEFAULT_ADDITIONAL_RADIUS_RATIO);
        }

        public ShapeViewActionsEditor addRoundRect(final float additionalRadiusRatio) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    addRoundRectOnView(additionalRadiusRatio);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ShapeViewActionsEditor(this);
        }

        public ShapeViewActionsEditor addRect() {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    addRectOnView();
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ShapeViewActionsEditor(this);
        }

        public ShapeViewActionsEditor addCircle() {
            return addCircle(DEFAULT_ADDITIONAL_RADIUS_RATIO);
        }

        public ShapeViewActionsEditor addCircle(final float additionalRadiusRatio) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    addCircleOnView(additionalRadiusRatio);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            return new ShapeViewActionsEditor(this);
        }

        private void addRoundRectOnView(float additionalRadiusRatio) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);

            int padding = 40;

            final int x = rect.left - padding;
            final int y = rect.top - getStatusBarOffset() - padding;
            final int width = rect.width() + 2 * padding;
            final int height = rect.height() + 2 * padding;

            RoundRect roundRect = new RoundRect(x, y, width, height);
            roundRect.setDisplayBorder(settings.withBorder);
            tutoShowcase.tutoView.addRoundRect(roundRect);
            addClickableView(rect, settings.onClickListener, additionalRadiusRatio);
            tutoShowcase.tutoView.postInvalidate();
        }

        private void addRectOnView() {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);


            final int x = rect.left;
            final int y = rect.top - getStatusBarOffset();
            final int width = rect.width();
            final int height = rect.height();

            Rectangle rectangle = new Rectangle(x, y, width, height);
            rectangle.setDisplayBorder(settings.withBorder);
            tutoShowcase.tutoView.addRect(rectangle);
            addClickableView(rect, settings.onClickListener, 0f);
            tutoShowcase.tutoView.postInvalidate();
        }


        /**
         * Status bar offset depends on content layout fitsSystemWindow flag.
         * For layouts that fit system window, we should not apply status bar offset.
         *
         * @return Status bar offset or 0 if content fits system window
         */
        private int getStatusBarOffset() {
            int result = 0;
            if (!this.fitsSystemWindow) {
                Context context = view.getContext();
                Resources resources = context.getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = resources.getDimensionPixelSize(resourceId);
                }
            }
            return result;
        }

        private void addClickableView(Rect rect, View.OnClickListener onClickListener, float additionalRadiusRatio) {
            View cliclableView = new View(this.view.getContext());
            int width = (int) (rect.width() * additionalRadiusRatio);
            int height = (int) (rect.height() * additionalRadiusRatio);
            int x = rect.left - (width - rect.width()) / 2;
            int y = rect.top - (height - rect.height()) / 2 - getStatusBarOffset();
            cliclableView.setLayoutParams(new ViewGroup.MarginLayoutParams(width, height));
            ViewCompat.setTranslationY(cliclableView, y);
            ViewCompat.setTranslationX(cliclableView, x);
            cliclableView.setOnClickListener(onClickListener);
            tutoShowcase.container.addView(cliclableView);
            tutoShowcase.container.invalidate();
        }

        public TutoShowcase showOnce(String key) {
            return tutoShowcase.showOnce(key);
        }

        public TutoShowcase onClickContentView(@IdRes int viewId, View.OnClickListener onClickListener) {
            return tutoShowcase.onClickContentView(viewId, onClickListener);
        }
    }

    public static class ViewActionsEditor {
        protected final ViewActions viewActions;

        public ViewActionsEditor(ViewActions viewActions) {
            this.viewActions = viewActions;
        }

        public ViewActions on(@IdRes int viewId) {
            return viewActions.on(viewId);
        }

        public ViewActions on(View view) {
            return viewActions.on(view);
        }

        public TutoShowcase show() {
            return viewActions.show();
        }

        public TutoShowcase showOnce(String key) {
            return viewActions.showOnce(key);
        }

        public TutoShowcase onClickContentView(@IdRes int viewId, View.OnClickListener onClickListener) {
            return viewActions.onClickContentView(viewId, onClickListener);
        }
    }

    public static class ShapeViewActionsEditor extends ViewActionsEditor {
        public ShapeViewActionsEditor(ViewActions viewActions) {
            super(viewActions);
        }

        public ShapeViewActionsEditor withBorder() {
            this.viewActions.settings.withBorder = true;
            return this;
        }

        public ShapeViewActionsEditor onClick(View.OnClickListener onClickListener) {
            this.viewActions.settings.onClickListener = onClickListener;
            return this;
        }
    }

    public static class ActionViewActionsEditor extends ViewActionsEditor {
        public ActionViewActionsEditor(ViewActions viewActions) {
            super(viewActions);
        }

        public ActionViewActionsEditor delayed(int delay) {
            this.viewActions.settings.delay = delay;
            return this;
        }

        public ActionViewActionsEditor duration(int duration) {
            this.viewActions.settings.duration = duration;
            return this;
        }

        public ActionViewActionsEditor animated(boolean animated) {
            this.viewActions.settings.animated = animated;
            return this;
        }
    }
}
