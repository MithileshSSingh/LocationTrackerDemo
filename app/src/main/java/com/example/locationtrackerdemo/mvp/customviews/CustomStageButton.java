package com.example.locationtrackerdemo.mvp.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.locationtrackerdemo.R;


/**
 * Created by mithilesh on 12/10/15.
 */

public class CustomStageButton extends FrameLayout {

    public static final int SWIPE_RIGHT = 0;
    public static final int SWIPE_LEFT = 1;

    private int viewHeight;
    private int direction = 0;
    private int mAnimDuration = 600;

    private Context mContext;
    private Button btnNextStage;
    private Button btnCurrentStage;
    private Drawable nextStageIndicator = null;

    private float endMotion = 0;
    private float startMotion = 0;
    private float totalDistance = 0;

    private boolean waitDone = false;

    private TranslateAnimation mAnimateLeft;
    private OnStageChangedListener onStateChangedListener;

    public boolean getWaitDone() {
        return waitDone;
    }

    public void setWaitDone() {
        this.waitDone = true;
    }

    public interface OnStageChangedListener {
        void onStateChanged(int direction);
    }

    public CustomStageButton(Context context) {
        super(context);
        init(context);
    }

    public CustomStageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomStage);

        int N = typedArray.getIndexCount();

        for (int i = 0; i < N; i++) {

            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.CustomStage_current_stage_text:
                    String stageOneText = typedArray.getString(attr);
                    btnCurrentStage.setText(stageOneText);
                    break;
                case R.styleable.CustomStage_next_stage_text:
                    String stageTwoText = typedArray.getString(attr);
                    btnNextStage.setText(stageTwoText);
                    break;
                case R.styleable.CustomStage_current_stage_bg_color:
                    int currentStageBgColor = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        currentStageBgColor = typedArray.getColor(attr, mContext.getResources().getColor(R.color.colorPrimary, mContext.getTheme()));
                    } else {
                        currentStageBgColor = typedArray.getColor(attr, mContext.getResources().getColor(R.color.colorPrimary));
                    }
                    btnCurrentStage.setBackgroundColor(currentStageBgColor);
                    break;
                case R.styleable.CustomStage_next_stage_bg_color:
                    int nextStageBgColor = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        nextStageBgColor = typedArray.getColor(attr, mContext.getResources().getColor(R.color.colorPrimary, mContext.getTheme()));
                    } else {
                        nextStageBgColor = typedArray.getColor(attr, mContext.getResources().getColor(R.color.colorPrimary));
                    }
                    btnNextStage.setBackgroundColor(nextStageBgColor);
                    break;
                case R.styleable.CustomStage_current_stage_text_color:
                    int currentStageTextColor = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        currentStageTextColor = typedArray.getColor(attr, mContext.getResources().getColor(android.R.color.white, mContext.getTheme()));
                    } else {
                        currentStageTextColor = typedArray.getColor(attr, mContext.getResources().getColor(android.R.color.white));
                    }
                    btnCurrentStage.setTextColor(currentStageTextColor);
                    break;
                case R.styleable.CustomStage_next_stage_text_color:
                    int nextStageTextColor = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        nextStageTextColor = typedArray.getColor(attr, mContext.getResources().getColor(android.R.color.white, mContext.getTheme()));
                    } else {
                        nextStageTextColor = typedArray.getColor(attr, mContext.getResources().getColor(android.R.color.white));
                    }
                    btnNextStage.setTextColor(nextStageTextColor);
                    break;
                case R.styleable.CustomStage_next_stage_indicator:
                    nextStageIndicator = typedArray.getDrawable(attr);

                    break;
                case R.styleable.CustomStage_direction:
                    direction = typedArray.getInteger(attr, 0);

                    break;
            }
        }
        typedArray.recycle();

        if (direction == 0) {
            btnCurrentStage.setCompoundDrawablesWithIntrinsicBounds(nextStageIndicator, null, null, null);
        } else {
            btnCurrentStage.setCompoundDrawablesWithIntrinsicBounds(null, null, nextStageIndicator, null);
        }

        btnNextStage.setTextSize(20);
        btnCurrentStage.setTextSize(20);
    }


    private void init(Context context) {
        mContext = context;

        initViews();
        initListeners();
    }


    private void initViews() {
        btnCurrentStage = new Button(mContext);
        btnNextStage = new Button(mContext);

        setButtonColors();

        btnCurrentStage.setSingleLine();
        btnNextStage.setSingleLine();

        btnCurrentStage.setTextSize(20);
        btnNextStage.setTextSize(20);

        addView(btnNextStage);
        addView(btnCurrentStage);
    }

    private void setButtonColors() {

        /**
         * Button Default Background Colors
         */
        int currentStageBgColor = 0x60000000;
        btnCurrentStage.setBackgroundColor(currentStageBgColor);

        int nextStageBgColor = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nextStageBgColor = mContext.getResources().getColor(R.color.colorPrimary, mContext.getTheme());
        } else {
            nextStageBgColor = mContext.getResources().getColor(R.color.colorPrimary);
        }
        btnNextStage.setBackgroundColor(nextStageBgColor);

        /**
         * Button Default text Color
         */

        int currentStageTextColor = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            currentStageTextColor = mContext.getResources().getColor(android.R.color.white, mContext.getTheme());
        } else {
            currentStageTextColor = mContext.getResources().getColor(android.R.color.white);
        }
        btnCurrentStage.setTextColor(currentStageTextColor);

        int nextStageTextColor = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nextStageTextColor = mContext.getResources().getColor(android.R.color.white, mContext.getTheme());
        } else {
            nextStageTextColor = mContext.getResources().getColor(android.R.color.white);
        }
        btnNextStage.setTextColor(nextStageTextColor);
    }

    private void initListeners() {
        btnCurrentStage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startMotion = event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endMotion = event.getRawX();
                        totalDistance = endMotion - startMotion;

                        if (direction == 0) {
                            mAnimateLeft = new TranslateAnimation(0f, btnCurrentStage.getWidth(), 0f, 0f);
                        } else if (direction == 1) {
                            totalDistance = Math.abs(totalDistance);
                            mAnimateLeft = new TranslateAnimation(0f, -btnCurrentStage.getWidth(), 0f, 0f);
                        }

                        if (totalDistance > (btnCurrentStage.getWidth())) {
                            mAnimateLeft.setDuration(mAnimDuration);
                            mAnimateLeft.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    viewHeight = btnCurrentStage.getHeight();
                                    btnNextStage.setHeight(viewHeight);
                                    btnCurrentStage.setVisibility(View.GONE);

                                    if (onStateChangedListener != null) {
                                        onStateChangedListener.onStateChanged(direction);
                                    }
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            btnCurrentStage.startAnimation(mAnimateLeft);

                        } else {
                            LayoutParams params = (LayoutParams) btnCurrentStage.getLayoutParams();
                            if (direction == 0) {
                                params.leftMargin = 0;
                            } else {
                                params.rightMargin = 0;
                            }
                            v.setLayoutParams(params);
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int movement = (int) (event.getRawX() - startMotion);

                        if (direction == 0) {
                            if (movement > 0) {
                                LayoutParams params = (LayoutParams) btnCurrentStage.getLayoutParams();
                                params.leftMargin = movement;
                                v.setLayoutParams(params);
                            }
                        } else {
                            if (movement < 0) {
                                LayoutParams params = (LayoutParams) btnCurrentStage.getLayoutParams();
                                params.rightMargin = Math.abs(movement);
                                v.setLayoutParams(params);
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void setCurrentStageText(String text) {
        btnCurrentStage.setText(text);
    }

    public String getCurrentStageText() {
        return btnCurrentStage.getText().toString().trim();
    }

    public void setNextStageText(String text) {
        btnNextStage.setText(text);
    }

    public String getNextStageText() {
        return btnNextStage.getText().toString().trim();
    }

    public void setOnStageChangedListener(OnStageChangedListener onStageChangedListener) {
        this.onStateChangedListener = onStageChangedListener;
    }

    public void changeTo(int visibility) {
        btnNextStage.setHeight(viewHeight);
        if (visibility == 1) {
            btnCurrentStage.setVisibility(View.VISIBLE);
        } else {
            btnCurrentStage.setVisibility(View.INVISIBLE);
        }
    }

    public void enabled(boolean b) {
        LayoutParams params;

        if (direction == 0) {
            params = (LayoutParams) btnCurrentStage.getLayoutParams();
            params.leftMargin = 0;
        } else {
            params = (LayoutParams) btnCurrentStage.getLayoutParams();
            params.rightMargin = 0;
        }

        if (b) {
            btnCurrentStage.setLayoutParams(params);
            btnCurrentStage.setVisibility(View.VISIBLE);
        } else {
            btnCurrentStage.setLayoutParams(params);
            btnCurrentStage.setVisibility(View.INVISIBLE);
        }
    }

}
