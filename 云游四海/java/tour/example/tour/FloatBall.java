package tour.example.tour;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by zhengjuntong on 12/28/16.
 */

public class FloatBall implements View.OnTouchListener {

    private static final int TOP_STATUS_BAR_HEIGHT = 25;
    private static final int MAX_ELEVATION = 64;
    private Params params;

    private Context context;
    private View ball;
    private int maxMarginLeft;
    private int maxMarginTop;
    private int downX;
    private int downY;
    private int xDelta;
    private int yDelta;
    private DisplayMetrics dm;

    public FloatBall(Params params) {
        this.params = params;
        this.context = params.context;
        init();
    }

    public View getBall() {
        return ball;
    }

    public void setVisibility(int visibility) {
        if (ball != null) {
            ball.setVisibility(visibility);
        }
    }

    private void init() {
        if (params.ball == null) {
            ball = new View(context);
        } else {
            ball = params.ball;
        }

        params.rootView.addView(ball);

        if (params.resId != 0) {
            ball.setBackgroundResource(params.resId);
        }

        ball.setOnTouchListener(this);
        ViewCompat.setElevation(ball, MAX_ELEVATION);

        ViewGroup.MarginLayoutParams layoutParams;

        if (params.rootView instanceof FrameLayout) {
            layoutParams = new FrameLayout.LayoutParams(params.width, params.height);
        } else if (params.rootView instanceof RelativeLayout) {
            layoutParams = new RelativeLayout.LayoutParams(params.width, params.height);
        } else {
            layoutParams = new ViewGroup.MarginLayoutParams(
                    params.width, params.height);
        }

        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        maxMarginLeft = screenWidth - params.width;
        layoutParams.width = params.width;
        layoutParams.height = params.height;
        layoutParams.leftMargin = maxMarginLeft - params.rightMargin;
        maxMarginTop = screenHeight - params.height - (int) (context.getResources().getDisplayMetrics().density * TOP_STATUS_BAR_HEIGHT);
        layoutParams.topMargin = maxMarginTop - params.bottomMargin;
        layoutParams.bottomMargin = 0;
        layoutParams.rightMargin = 0;

        ball.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int touchX = (int) event.getRawX();
        final int touchY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downX = touchX;
                downY = touchY;
                ViewGroup.MarginLayoutParams lParams = (ViewGroup.MarginLayoutParams) ball
                        .getLayoutParams();
                xDelta = touchX - lParams.leftMargin;
                yDelta = touchY - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                if (downX == touchX && downY == touchY) {
                    if (params.onClickListener != null) {
                        params.onClickListener.onClick(ball);
                    }
                } else {
                    Animation animation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ball
                                    .getLayoutParams();

                            int curLeftMargin = layoutParams.leftMargin;

                            if (touchX < dm.widthPixels / 2) {
                                layoutParams.leftMargin = (int) (curLeftMargin - curLeftMargin * interpolatedTime);
                            } else {
                                layoutParams.leftMargin = (int) (curLeftMargin + (maxMarginLeft - curLeftMargin) * interpolatedTime);
                            }

                            ball.setLayoutParams(layoutParams);
                        }
                    };
                    animation.setDuration(params.duration);
                    ball.startAnimation(animation);
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                ViewGroup.MarginLayoutParams layoutParams;

                if (params.rootView instanceof FrameLayout) {
                    layoutParams = (FrameLayout.LayoutParams) ball.getLayoutParams();
                } else if (params.rootView instanceof RelativeLayout) {
                    layoutParams = (RelativeLayout.LayoutParams) ball.getLayoutParams();
                } else {
                    layoutParams = (ViewGroup.MarginLayoutParams) ball.getLayoutParams();
                }

                int leftMargin;

                if (touchX - xDelta <= 0) {
                    leftMargin = 0;
                } else if (touchX - xDelta < maxMarginLeft) {
                    leftMargin = touchX - xDelta;
                } else {
                    leftMargin = maxMarginLeft;
                }

                layoutParams.leftMargin = leftMargin;

                int topMargin;

                if (touchY - yDelta <= 0) {
                    topMargin = 0;
                } else if (touchY - yDelta < maxMarginTop) {
                    topMargin = touchY - yDelta;
                } else {
                    topMargin = maxMarginTop;
                }

                layoutParams.topMargin = topMargin;
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                ball.setLayoutParams(layoutParams);
                break;
        }

        ball.getRootView().invalidate();

        return true;
    }

    public static class Builder {
        private Params P;

        public Builder(Context context, ViewGroup rootView) {
            P = new Params(context);
            P.rootView = rootView;
        }

        public Builder setRightMargin(int rightMargin) {
            P.rightMargin = rightMargin;
            return this;
        }

        public Builder setBottomMargin(int bottomMargin) {
            P.bottomMargin = bottomMargin;
            return this;
        }

        public Builder setWidth(int width) {
            P.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            P.height = height;
            return this;
        }

        public Builder setRes(int resId) {
            P.resId = resId;
            return this;
        }

        public Builder setBall(View view) {
            P.ball = view;
            return this;
        }

        public Builder setDuration(int duration) {
            P.duration = duration;
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            P.onClickListener = onClickListener;
            return this;
        }

        public FloatBall build() {
            FloatBall floatBall = new FloatBall(P);
            return floatBall;
        }
    }

    private static class Params {
        public static final int DEFAULT_BALL_WIDTH = 180;
        public static final int DEFAULT_BALL_HEIGHT = 180;
        public static final int DEFAULT_DURATION = 500;
        private int duration = DEFAULT_DURATION;
        private Context context;
        private int rightMargin;
        private int bottomMargin;
        private int resId;
        private int width = DEFAULT_BALL_WIDTH;
        private int height = DEFAULT_BALL_HEIGHT;
        private ViewGroup rootView;
        private View.OnClickListener onClickListener;
        private View ball;

        public Params(Context context) {
            this.context = context;
        }
    }
}
