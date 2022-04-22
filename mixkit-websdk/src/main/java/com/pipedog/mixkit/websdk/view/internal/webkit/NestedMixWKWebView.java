package com.pipedog.mixkit.websdk.view.internal.webkit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.pipedog.mixkit.web.view.MixWKWebView;

import static androidx.customview.widget.ViewDragHelper.INVALID_POINTER;

/**
 * 继承自 MixWKWebView，为了解决视图嵌套引起的手势冲突问题
 */
public class NestedMixWKWebView extends MixWKWebView implements NestedScrollingChild2 {

    private static final String TAG = "NestedMixWKWebView";

    private NestedScrollingChildHelper mScrollingChildHelper;
    private int mLastMotionY;

    /**
     * 用于跟踪触摸事件速度的辅助类，用于实现
     * fling 和其他类似的手势。
     */
    private VelocityTracker mVelocityTracker;

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */

    private boolean mIsBeingDragged = false;

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.（多点触控有用）
     */
    private int mActivePointerId = INVALID_POINTER;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private OverScroller mScroller;
    private int mNestedYOffset;
    private int mLastScrollerY;
    private int mLastY;
    private int moveDistance;

    public NestedMixWKWebView(Context context) {
        this(context, null);
    }

    public NestedMixWKWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(getContext());
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        // 设置支持嵌套滑动
        setNestedScrollingEnabled(true);
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return mScrollingChildHelper;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        boolean eventAddedToVelocityTracker = false;
        // 复制一个 event
        MotionEvent vtev = MotionEvent.obtain(event);
        // 类似 getAction
        final int actionMasked = event.getActionMasked();
        boolean result = false;
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
            isFlinging = false;
        }
        vtev.offsetLocation(0, mNestedYOffset);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                if ((mIsBeingDragged = !mScroller.isFinished())) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        // 不让父布局拦截事件
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.//如果在fling 的过程中用户触摸屏幕，则停止fling
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionY = (int) event.getY();
                mLastY = mLastMotionY;
                mActivePointerId = event.getPointerId(0);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                result = super.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }
                final int y = (int) event.getY(activePointerIndex);

                int deltaY = mLastMotionY - y;
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset, ViewCompat.TYPE_TOUCH)) {
                    // 纵轴位移 - 被父布局消费的滑动距离
                    deltaY -= mScrollConsumed[1];
                }
                moveDistance = deltaY;
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                }

                if (mIsBeingDragged) {
                    // 上一次的坐标
                    mLastMotionY = y - mScrollOffset[1];
                    int scrolledDeltaY = 0;
                    int unconsumedY = deltaY;
                    if (Math.abs(deltaY) > 0) {
                        if (deltaY <= 0) {
                            // 向顶部滑动
                            if (canScrollVertically(-1)) {
                                if (getScrollY() + deltaY < 0) {
                                    scrolledDeltaY = -getScrollY();
                                    unconsumedY = getScrollY() + deltaY;
                                    vtev.offsetLocation(0, unconsumedY);//这行不知对不对
                                    mNestedYOffset += unconsumedY;
                                } else {
                                    scrolledDeltaY = deltaY;
                                    unconsumedY = 0;
                                }
                            }
                        } else {
                            if (canScrollVertically(1)) {
                                if (deltaY - getTop() > 0) {
                                    scrolledDeltaY = deltaY - getTop();
                                    unconsumedY = getTop();
                                    vtev.offsetLocation(0, unconsumedY);//这行不知对不对
                                    mNestedYOffset += unconsumedY;
                                } else {
                                    scrolledDeltaY = deltaY;
                                    unconsumedY = 0;
                                }
                            }
                        }
                    }
                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1];
                        vtev.offsetLocation(0, mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                    }
                }
                if (deltaY == 0 && mIsBeingDragged) {
                    result = true;
                } else {
                    result = super.onTouchEvent(vtev);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(vtev);
                }
                eventAddedToVelocityTracker = true;
                caculateV(mActivePointerId, (int) event.getY());
                mLastY = (int) event.getY();
                mActivePointerId = INVALID_POINTER;
                endDrag();
                if (mVelocityTracker != null) {
                    mVelocityTracker.clear();
                }
                stopNestedScroll(ViewCompat.TYPE_TOUCH);
                result = super.onTouchEvent(vtev);
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                endDrag();
                if (mVelocityTracker != null) {
                    mVelocityTracker.clear();
                }
                stopNestedScroll(ViewCompat.TYPE_TOUCH);
                result = super.onTouchEvent(event);
                break;
        }
        if (!eventAddedToVelocityTracker) {
            if (mVelocityTracker != null) {
                mVelocityTracker.addMovement(vtev);
            }
        }
        vtev.recycle();
        return result;
    }

    private boolean isFlinging;

    /**
     * 处理fling 速度问题
     * @param mActivePointerId
     * @param curY
     */
    private void caculateV(int mActivePointerId, int curY) {
        mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
        int initialVelocity = (int) mVelocityTracker.getYVelocity(mActivePointerId);
        if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
            mLastScrollerY = getScrollY();
            isFlinging = true;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (isFlinging) {
            int dy = getScrollY() - mLastScrollerY;
            if (getScrollY() == 0) {
                int velocityY = 1000;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
                if (moveDistance < 0) {
                    dispatchNestedScroll(0, dy, 0, -velocityY, null,
                            ViewCompat.TYPE_NON_TOUCH);
                }
                isFlinging = false;
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
            }
        }
    }

    private void endDrag() {
        mIsBeingDragged = false;
        recycleVelocityTracker();
        stopNestedScroll(ViewCompat.TYPE_TOUCH);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void flingScroll(int vx, int vy) {
        super.flingScroll(vx, vy);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return getScrollingChildHelper().startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        getScrollingChildHelper().stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return getScrollingChildHelper().hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

}
