package com.lockmotor.BaseAppication.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.TextView;

import com.lockmotor.R;

public class AnimationUtils {

    /**
     *  Animation Standard
     */
    public static void fadeIn(View view, int duration) {
        ObjectAnimator.ofFloat(view, "alpha", 1).setDuration(duration).start();
    }

    public static void fadeOut(View view, int duration) {
        ObjectAnimator.ofFloat(view, "alpha", 0).setDuration(duration).start();
    }

    public static void scaleX(View view, float start, float end, int duration) {
        ObjectAnimator.ofFloat(view, "scaleX", start, end).setDuration(duration).start();
    }

    public static void scaleY(View view, float start, float end, int duration) {
        ObjectAnimator.ofFloat(view, "scaleY", start, end).setDuration(duration).start();
    }

    public static void translationX(View view, float start, float end, int duration) {
        ObjectAnimator.ofFloat(view, "translationX", start, end).setDuration(duration).start();
    }

    public static void translationY(View view, float start, float end, int duration) {
        ObjectAnimator.ofFloat(view, "translationY", start, end).setDuration(duration).start();
    }

    public static void translationYInt(View view, float start, float end, int duration, int delay) {
        fadeOut(view, 0);

        AnimatorSet translate = new AnimatorSet();
        translate.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", start, end),
                ObjectAnimator.ofFloat(view, "alpha", 1)
        );

        translate.setStartDelay(delay);
        translate.setDuration(duration);
        translate.start();
    }

    public static void translationYOut(View view, float start, float end, int duration, int delay) {
        AnimatorSet translate = new AnimatorSet();
        translate.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", start, end),
                ObjectAnimator.ofFloat(view, "alpha", 0)
        );

        translate.setStartDelay(delay);
        translate.setDuration(duration);
        translate.start();
    }

    /**
     * Animation rebound standard
     */
    public static void translateReboundX(View view, float x, int duration, int delay) {
        view.animate().translationX(x)
                .setDuration(duration)
                .setInterpolator(new OvershootInterpolator())
                .setStartDelay(delay);
    }

    public static void translateReboundY(View view, float y, int duration, int delay) {
        view.animate().translationY(y)
                .setDuration(duration)
                .setInterpolator(new OvershootInterpolator())
                .setStartDelay(delay);
    }

    public static void shakeAnimationEditText(Context context, final EditText editText) {
        // shake
        Animation shake = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.shake);

        Integer currentColor = editText.getCurrentTextColor();
        String currentText = editText.getText().toString();
        final boolean isEmpty = currentText.length() <= 0;
        if (isEmpty)
            currentColor = editText.getHintTextColors().getDefaultColor();

        // fade-in color
        Integer errorColor = Color.RED;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), errorColor, currentColor);
        colorAnimation.setDuration(1500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (isEmpty)
                    editText.setHintTextColor((Integer) animator.getAnimatedValue());
                else
                    editText.setTextColor((Integer) animator.getAnimatedValue());
            }
        });

        // start animation
        editText.startAnimation(shake);
        colorAnimation.start();
    }

    public static void shakeAnimationTextView(Context context, final TextView textView) {
        // shake
        Animation shake = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.shake);

        // fade-in color
        Integer currentColor = textView.getCurrentTextColor();
        Integer errorColor = Color.RED;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), errorColor, currentColor);
        colorAnimation.setDuration(1500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                textView.setTextColor((Integer) animator.getAnimatedValue());
            }
        });

        // start animation
        textView.startAnimation(shake);
        colorAnimation.start();
    }

    public static void fadeInAndScale(View view, int milliseconds, float scaleStart, float scaleEnd, boolean setVisibilityBeforeStart) {
        if (setVisibilityBeforeStart)
            view.setVisibility(View.VISIBLE);

        float currentAlpha = view.getAlpha();
        if (currentAlpha >= 1)
            return;

        // fade in
        ObjectAnimator fadeAnimation = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        fadeAnimation.setDuration(milliseconds);
        // scale
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(view, "scaleX", scaleStart, scaleEnd);
        scaleXAnimation.setDuration(milliseconds);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(view, "scaleY", scaleStart, scaleEnd);
        scaleYAnimation.setDuration(milliseconds);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeAnimation).with(scaleXAnimation).with(scaleYAnimation);
        animatorSet.start();
    }

    public static void fadeOutAndScale(final View view, int milliseconds, float scaleStart, float scaleEnd, final boolean hideWhenDone) {
        // fade out
        ObjectAnimator fadeAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeAnimation.setDuration(milliseconds);

        float currentAlpha = view.getAlpha();
        if (currentAlpha <= 0)
            return;

        // scale
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(view, "scaleX", scaleStart, scaleEnd);
        scaleXAnimation.setDuration(milliseconds);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(view, "scaleY", scaleStart, scaleEnd);
        scaleYAnimation.setDuration(milliseconds);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeAnimation).with(scaleXAnimation).with(scaleYAnimation);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (hideWhenDone) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    public static void rotateWithDuration(View view, int milliseconds, int radiusFrom, int radiusTo) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotation", radiusFrom, radiusTo);
        animation.setDuration(milliseconds);
        animation.start();
    }

    //**********************************************************************************************
    // for dialog
    //**********************************************************************************************

    public static void fadeAndScaleAnimationForDialog(Context context, View v) {
        Animation scaleAndFadeAnimation =
                android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale_and_fade_fordialog);
        v.startAnimation(scaleAndFadeAnimation);
    }

    public static void AnimationWheelInfinity(Context context, View viewContainer) {
        if (viewContainer == null)
            return;

        Animation rotation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.loading_animate);
        viewContainer.setVisibility(View.VISIBLE);
        viewContainer.startAnimation(rotation);
        rotation.setDuration(800);
    }

    /**
     * Collapse/expand view transition animation.
     */
    public static void collapseView(final View view, Number collapseHeight) {
        if (view == null)
            return;

        // This is optionally, default collapse to zero point
        if (collapseHeight == null)
            collapseHeight = 0;
        final Number finalCollapseHeight = collapseHeight;

        view.post(new Runnable() {
            @Override
            public void run() {
                int currentViewHeight = view.getHeight();
                if (currentViewHeight <= finalCollapseHeight.intValue()) {
                    Log.e("Animation collapse", "make sure the collapse height is small more to current view height");
                    return;
                }

                ExpandCollapseView(view, currentViewHeight, finalCollapseHeight.intValue());
            }
        });
    }

    public static void expandView(final View view, final Number expandHeight) {
        if (view == null || expandHeight == null)
            return;

        view.post(new Runnable() {
            @Override
            public void run() {
                int currentHeight = view.getHeight();
                if (currentHeight >= expandHeight.intValue())
                    return;

                ExpandCollapseView(view, currentHeight, expandHeight.intValue());
            }
        });
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static void ExpandCollapseView(final View view, final int start, final int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });

        animator.start();
    }
}
