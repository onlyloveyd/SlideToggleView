package cn.onlyloveyd.slidetoggleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.onlyloveyd.slidetoggleview.ext.DisplayUtils;
import cn.onlyloveyd.slidetoggleview.shimmer.ShimmerTextView;

/**
 * 滑动开关
 *
 * @author jiang
 * @date 2019/1/7
 */
public class SlideToggleView extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    /**
     * 文本
     */
    private ShimmerTextView mShimmerTextView;

    /**
     * 滑块
     */
    private ImageView mBlockView;

    /**
     * 滑块外边距
     */
    private int mBlockLeftMargin;
    private int mBlockRightMargin;
    private int mBlockTopMargin;
    private int mBlockBottomMargin;

    /**
     * 触发打开事件允许剩余的距离
     */
    private int mRemainDistance;


    private SlideToggleListener mSlideToggleListener;
    private ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return view == mBlockView;
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            ViewParent parent = capturedChild.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int min = getPaddingLeft() + mBlockLeftMargin;
            if (left < min) {
                left = min;
            }
            int max = getMeasuredWidth() - getPaddingRight() - mBlockRightMargin
                    - mBlockView.getMeasuredWidth();
            if (left > max) {
                left = max;
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return getPaddingTop() + mBlockTopMargin;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx,
                                          int dy) {
            if (mSlideToggleListener != null) {
                int total =
                        getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mBlockLeftMargin
                                - mBlockRightMargin - mBlockView.getMeasuredWidth();
                int slide = left - getPaddingLeft() - mBlockLeftMargin;
                mSlideToggleListener.onBlockPositionChanged(SlideToggleView.this, left, total,
                        slide);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (releasedChild == mBlockView) {
                int total =
                        getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mBlockLeftMargin
                                - mBlockRightMargin - mBlockView.getMeasuredWidth();
                int slide = releasedChild.getLeft() - getPaddingLeft() - mBlockLeftMargin;
                if (total - slide <= mRemainDistance) {
                    openToggle();

                    if (mSlideToggleListener != null) {
                        mSlideToggleListener.onSlideOpen(SlideToggleView.this);
                    }
                } else {
                    int finalLeft = getPaddingLeft() + mBlockLeftMargin;
                    int finalTop = getPaddingTop() + mBlockTopMargin;
                    mViewDragHelper.settleCapturedViewAt(finalLeft, finalTop);
                    invalidate();
                }
            }
        }
    };

    public SlideToggleView(@NonNull Context context) {
        this(context, null);
    }

    public SlideToggleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideToggleView(@NonNull Context context, @Nullable AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, mDragCallback);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideToggleView,
                defStyleAttr, 0);
        String text = a.getString(R.styleable.SlideToggleView_stv_text);
        int textColor = a.getColor(R.styleable.SlideToggleView_stv_textColor, 0xffffffff);
        int textSize = a.getDimensionPixelSize(R.styleable.SlideToggleView_stv_textSize,
                DisplayUtils.dp2px(context, 14));
        Drawable slideBlock = a.getDrawable(R.styleable.SlideToggleView_stv_slideBlock);
        mBlockLeftMargin = a.getDimensionPixelSize(R.styleable.SlideToggleView_stv_blockLeftMargin,
                4);
        mBlockRightMargin = a.getDimensionPixelSize(
                R.styleable.SlideToggleView_stv_blockRightMargin, 4);
        mBlockTopMargin = a.getDimensionPixelSize(R.styleable.SlideToggleView_stv_blockTopMargin,
                4);
        mBlockBottomMargin = a.getDimensionPixelSize(
                R.styleable.SlideToggleView_stv_blockBottomMargin, 4);
        mRemainDistance = a.getDimensionPixelSize(R.styleable.SlideToggleView_stv_remain, 10);

        a.recycle();

        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        mShimmerTextView = new ShimmerTextView(context);
        mShimmerTextView.setText(text);
        mShimmerTextView.setTextColor(textColor);
        mShimmerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(mShimmerTextView, textParams);


        mBlockView = new ImageView(context);
        mBlockView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mBlockView.setImageDrawable(slideBlock);
        LayoutParams blockParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        blockParams.setMargins(mBlockLeftMargin, mBlockTopMargin, mBlockRightMargin,
                mBlockBottomMargin);
        addView(mBlockView, blockParams);
    }

    /**
     * 设置文本内容
     *
     * @param text 显示内容
     */
    public void setText(String text) {
        mShimmerTextView.setText(text);
    }

    /**
     * 设置文字颜色
     *
     * @param color 文字颜色
     */
    public void setTextColor(int color) {
        mShimmerTextView.setTextColor(color);
    }

    /**
     * 设置文字大小
     *
     * @param size 文字大小，单位{@link TypedValue#COMPLEX_UNIT_SP}
     */
    public void setTextSize(float size) {
        mShimmerTextView.setTextSize(size);
    }

    /**
     * 设置滑块图片，drawable必须有确定的大小
     *
     * @param drawable 滑块图片
     */
    public void setBlockDrawable(Drawable drawable) {
        mBlockView.setImageDrawable(drawable);
    }

    /**
     * 设置滑块外边距
     *
     * @param left   左边距
     * @param top    上边距
     * @param right  右边距
     * @param bottom 下边距
     */
    public void setBlockMargin(int left, int top, int right, int bottom) {
        mBlockLeftMargin = left;
        mBlockRightMargin = right;
        mBlockTopMargin = top;
        mBlockBottomMargin = bottom;

        LayoutParams params = (LayoutParams) mBlockView.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        mBlockView.setLayoutParams(params);
    }

    /**
     * 设置触发打开事件时可以剩余的距离
     *
     * @param remain 剩余距离
     */
    public void setRemainDistance(int remain) {
        mRemainDistance = remain;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        } else {
            super.computeScroll();
        }
    }

    /**
     * 设置滑动开关监听器
     *
     * @param listener 滑动开关监听器
     */
    public void setSlideToggleListener(SlideToggleListener listener) {
        this.mSlideToggleListener = listener;
    }

    /**
     * 打开滑动开关
     */
    public void openToggle() {
        int finalLeft = getMeasuredWidth() - getPaddingRight() - mBlockRightMargin
                - mBlockView.getMeasuredWidth();
        int finalTop = getPaddingTop() + mBlockTopMargin;
        mViewDragHelper.smoothSlideViewTo(mBlockView, finalLeft, finalTop);
        invalidate();
    }

    /**
     * 关闭欢动开关
     */
    public void closeToggle() {
        int finalLeft = getPaddingLeft() + mBlockLeftMargin;
        int finalTop = getPaddingTop() + mBlockTopMargin;
        mViewDragHelper.smoothSlideViewTo(mBlockView, finalLeft, finalTop);
        invalidate();
    }

    public interface SlideToggleListener {
        /**
         * 滑块位置改变回调
         *
         * @param left  滑块左侧位置，值等于{@link #getLeft()}
         * @param total 滑块可以滑动的总距离
         * @param slide 滑块已经滑动的距离
         */
        void onBlockPositionChanged(SlideToggleView view, int left, int total, int slide);

        /**
         * 滑动打开
         */
        void onSlideOpen(SlideToggleView view);
    }
}
