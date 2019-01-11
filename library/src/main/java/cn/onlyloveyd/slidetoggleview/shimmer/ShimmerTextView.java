package cn.onlyloveyd.slidetoggleview.shimmer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import cn.onlyloveyd.slidetoggleview.R;

/**
 * ShimmerTextView
 *
 * @author onlyloveyd
 * @date 2019/1/10 09:54
 */
public class ShimmerTextView extends AppCompatTextView {
    private final Paint mContentPaint = new Paint();
    private final ShimmerDrawable mShimmerDrawable = new ShimmerDrawable();

    public ShimmerTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ShimmerTextView(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShimmerTextView(Context context, @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setWillNotDraw(false);
        mShimmerDrawable.setCallback(this);

        if (attrs == null) {
            setShimmer(new Shimmer.AlphaHighlightBuilder().build());
            return;
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShimmerTextView, 0, 0);
        try {
            Shimmer.Builder shimmerBuilder =
                    a.hasValue(R.styleable.ShimmerTextView_shimmer_colored)
                            && a.getBoolean(R.styleable.ShimmerTextView_shimmer_colored, false)
                            ? new Shimmer.ColorHighlightBuilder()
                            : new Shimmer.AlphaHighlightBuilder();
            setShimmer(shimmerBuilder.consumeAttributes(a).build());
        } finally {
            a.recycle();
        }
    }

    public ShimmerTextView setShimmer(@Nullable Shimmer shimmer) {
        mShimmerDrawable.setShimmer(shimmer);
        if (shimmer != null && shimmer.clipToChildren) {
            setLayerType(LAYER_TYPE_HARDWARE, mContentPaint);
        } else {
            setLayerType(LAYER_TYPE_NONE, null);
        }

        return this;
    }

    /** Starts the shimmer animation. */
    public void startShimmer() {
        mShimmerDrawable.startShimmer();
    }

    /** Stops the shimmer animation. */
    public void stopShimmer() {
        mShimmerDrawable.stopShimmer();
    }

    /** Return whether the shimmer animation has been started. */
    public boolean isShimmerStarted() {
        return mShimmerDrawable.isShimmerStarted();
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int width = getWidth();
        final int height = getHeight();
        mShimmerDrawable.setBounds(0, 0, width, height);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mShimmerDrawable.maybeStartShimmer();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopShimmer();
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mShimmerDrawable.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == mShimmerDrawable;
    }
}
