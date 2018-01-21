package com.example.chunyu.switchcardview.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.HashMap;
import java.util.Observable;

/**
 * Created by 人间一小雨 on 2018/1/20 上午11:21
 * Email: 746431278@qq.com
 */

public class SwitchCardView extends RelativeLayout {

    private static final String TAG = "BaseSwitchCardView";
    private static final float BOTTOM_VIEW_ROTATION = -3.0f;
    private static final float FLOATING_VIEW_ROTATION = 10.0f;
    private static final float RESET_VIEW_RANGE = 150;
    private static final float RADIUS_FOR_ROTATION = 150 * 1.412f;

    private static final float FLY_DISTANCE = 400;
    private static final int ANIMATION_A_TIME = 100;
    private static final int ANIMATION_B_TIME = 300;


    private float mTouchX;
    private float mTouchY;
    private float mSumDistanceX;
    private float mSumDistanceY;
    private float mLastX;
    private float mLastY;

    private float mTopCardViewX;
    private float mTopCardViewY;

    private int mCurrentIndex = 0;

    private CardViewAdapterDataSetObserver mDataSetObserver;


    /**
     * 游离状态的View,就是跟着手指跑的那个"假View"
     */
    private ImageView mFloatingView;


    private final HashMap<String, View> mFloatingViewMaps = new HashMap<>();

    /**
     * 代表现在是否手指上有View正在跟随着拖动。
     */
    private boolean mHasDragView = false;

    private ViewGroup mDecorView;
    private boolean mNeedIntercept = false;


    private View mTopCardView;
    private View mBottomCardView;

    private Adapter mAdapter;


    public SwitchCardView(Context context) {
        super(context);
        init(context);
    }

    public SwitchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwitchCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mDecorView = getDecorView(context);
        mFloatingView = new ImageView(getContext());
        if (mDecorView != null) {
            setClickable(true);
            setFocusable(true);
            setFocusableInTouchMode(true);
        } else {
            throw new IllegalStateException("DecorView is null");
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (checkValid()) {
            //利用dispatchTouchEvent中的事件，进行滑动动作的监听。
            mNeedIntercept = isDispatchTouchEvent(event);
        } else {
            mNeedIntercept = false;
        }
        return super.dispatchTouchEvent(event);
    }


    private boolean isDispatchTouchEvent(MotionEvent event) {
        boolean comsumed = false;
        if (checkValid()) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (mTopCardViewX == -1) {
                        int[] xy = new int[2];
                        mTopCardView.getLocationOnScreen(xy);
                        mTopCardViewX = xy[0];
                        mTopCardViewY = xy[1];
                    }
                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mLastX = mTouchX;
                    mLastY = mTouchY;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    if (mHasDragView) {
                        comsumed = true;
                    }

                    resetView(String.valueOf(mFloatingView.hashCode()));
                    break;
                case MotionEvent.ACTION_MOVE:
                    mSumDistanceX = event.getRawX() - mTouchX;
                    mSumDistanceY = event.getRawY() - mTouchY;
                    float dealtX = event.getRawX() - mLastX;
                    float dealtY = event.getRawY() - mLastY;
                    if (!mHasDragView && Math.sqrt(mSumDistanceX * mSumDistanceX + mSumDistanceY * mSumDistanceY) > ViewConfiguration.getTouchSlop()) {
                        addViewToDecorView(mTopCardView);
                        mHasDragView = true;
                    }

                    if (mHasDragView) {
                        changeViewProperty(dealtX, dealtY, mSumDistanceX, mSumDistanceY);
                        comsumed = true;
                    }


                    mLastX = event.getRawX();
                    mLastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    if (mHasDragView) {
                        comsumed = true;
                    }
                    resetView(String.valueOf(mFloatingView.hashCode()));
                    break;
                default:
                    break;
            }
        }
        return comsumed;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //这里通过isDispatchTouchEvent()进行决策，是否让此事件往下层传，如果当isDispatchTouchEvent()
        //返回true的时候，代表此事件，已经被当前的View消费，不再往下传。
        return mNeedIntercept;
    }


    /**
     * @param x    deltaX
     * @param y    deltaY
     * @param sumX 从手指触摸的位置算起，累计滑动的X轴距离
     * @param sumY 从手指触摸的位置算起，累计滑动的Y轴距离
     */
    private void changeViewProperty(float x, float y, float sumX, float sumY) {


        //卡片跟手滑动
        float translationX = mFloatingView.getTranslationX();
        float translationY = mFloatingView.getTranslationY();
        mFloatingView.setTranslationY(translationY + y);
        mFloatingView.setTranslationX(translationX + x);

        float widthRule = mFloatingView.getWidth();
        float heightRule = mFloatingView.getHeight();

        //套用y = x平方模型，来模拟，非线性动画效果()。
        float rate = (float) Math.pow(Math.sqrt(sumX * sumX + sumY * sumY) / widthRule, 2.0);
        //角度是线性变化（根据x轴的偏移）
        float floatingViewRotation = FLOATING_VIEW_ROTATION * (float) (sumX / (widthRule / 2.0));
        //透明度是非线性变化，先慢后快。
        float floatingViewAlpha = 1.0f - rate;

        if (floatingViewAlpha < 0.0f) {
            floatingViewAlpha = 0.0f;
        } else if (floatingViewAlpha > 1.0f) {
            floatingViewAlpha = 1.0f;
        }

        mFloatingView.setRotation(floatingViewRotation);
        mFloatingView.setAlpha(floatingViewAlpha);

        //底部动画效果，线性变化
        float bottomRate = (float) (Math.sqrt(sumX * sumX + sumY * sumY) / RADIUS_FOR_ROTATION);
        float bottomViewRotation = BOTTOM_VIEW_ROTATION * (1.0f - bottomRate);
        float bottomViewAlpha = bottomRate;
        if (bottomViewAlpha < 0.0f) {
            bottomViewAlpha = 0.0f;
        } else if (bottomViewAlpha >= 1.0f) {
            bottomViewAlpha = 1.0f;
        }

        if (bottomViewRotation < BOTTOM_VIEW_ROTATION) {
            bottomViewRotation = BOTTOM_VIEW_ROTATION;
        } else if (bottomViewRotation > 0) {
            bottomViewRotation = 0;
        }
        mBottomCardView.setRotation(bottomViewRotation);
        mBottomCardView.setAlpha(bottomViewAlpha);

    }


    private void resetView(final String hashcodeKey) {
        if (mHasDragView) {

            AnimatorSet animationSet = new AnimatorSet();

            if (Math.abs(mSumDistanceX) < RESET_VIEW_RANGE && Math.abs(mSumDistanceY) < RESET_VIEW_RANGE) {

                animationSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeFloatingViewA(hashcodeKey);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        removeFloatingViewA(hashcodeKey);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                mAdapter.onReleaseTouchForBeginResetAnim();
                animationSet.playTogether(
                        ObjectAnimator.ofFloat(mFloatingView, "rotation", mFloatingView.getRotation(), 0),
                        ObjectAnimator.ofFloat(mFloatingView, "translationX", mFloatingView.getTranslationX(), mTopCardViewX),
                        ObjectAnimator.ofFloat(mFloatingView, "translationY", mFloatingView.getTranslationY(), mTopCardViewY),
                        ObjectAnimator.ofFloat(mFloatingView, "alpha", mFloatingView.getAlpha(), 1.0f),
                        ObjectAnimator.ofFloat(mBottomCardView, "rotation", mBottomCardView.getRotation(), BOTTOM_VIEW_ROTATION));

                animationSet.setDuration(ANIMATION_A_TIME).start();
            } else {
                animationSet.setInterpolator(new AccelerateInterpolator());
                animationSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        removeFloatingViewB(hashcodeKey);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        removeFloatingViewB(hashcodeKey);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


                nextIndex();
                mAdapter.renderView(mCurrentIndex, mTopCardView);
                mAdapter.renderView((mCurrentIndex + 1) % mAdapter.getItemCount(), mBottomCardView);
                mTopCardView.setVisibility(View.VISIBLE);
                mBottomCardView.setRotation(BOTTOM_VIEW_ROTATION);
                mBottomCardView.setAlpha(0.0f);
                mAdapter.onReleaseTouchForBeginSwitchAnim();

                animationSet.playTogether(
                        ObjectAnimator.ofFloat(mFloatingView, "rotation", mFloatingView.getRotation(), mFloatingView.getRotation() + (float) (FLOATING_VIEW_ROTATION * getcosValue(mSumDistanceX,
                                mSumDistanceY))),
                        ObjectAnimator.ofFloat(mFloatingView, "translationX", mFloatingView.getTranslationX(), mFloatingView.getTranslationX() + FLY_DISTANCE * (float) getcosValue(mSumDistanceX,
                                mSumDistanceY)),
                        ObjectAnimator.ofFloat(mFloatingView, "translationY", mFloatingView.getTranslationY(), mFloatingView.getTranslationY() + FLY_DISTANCE * (float) getsinValue(mSumDistanceX,
                                mSumDistanceY)),
                        ObjectAnimator.ofFloat(mFloatingView, "alpha", mFloatingView.getAlpha(), 0.0f),
                        ObjectAnimator.ofFloat(mBottomCardView, "rotation", mBottomCardView.getRotation(), 0.0f));

                animationSet.setDuration(ANIMATION_B_TIME).start();
            }
        }

        mHasDragView = false;
        mTouchX = -1;
        mTouchY = -1;
        mSumDistanceY = -1;
        mSumDistanceX = -1;
        mTopCardViewX = -1;
        mTopCardViewY = -1;
        mLastX = -1;
        mLastY = -1;
    }

    private void nextIndex() {
        mCurrentIndex = (mCurrentIndex + 1) % mAdapter.getItemCount();
    }

    private double getcosValue(float x, float y) {
        return x / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    private double getsinValue(float x, float y) {
        return y / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }


    private void removeFloatingViewA(String hashcodeKey) {
        mTopCardView.setVisibility(View.VISIBLE);
        mBottomCardView.setAlpha(0.0f);
        mBottomCardView.setRotation(BOTTOM_VIEW_ROTATION);
        View tempView = mFloatingViewMaps.remove(hashcodeKey);
        if (tempView != null) {
            mDecorView.removeView(tempView);
        }
        mAdapter.onReleaseTouchForEndResetAnim();
    }


    private void removeFloatingViewB(String hashcodeKey) {

        View tempView = mFloatingViewMaps.remove(hashcodeKey);
        if (tempView != null) {
            mDecorView.removeView(tempView);
        }
        mAdapter.onReleaseTouchForEndSwitchAnim();

    }


    @Nullable
    private Bitmap createViewDrawable() {
        mTopCardView.setDrawingCacheEnabled(true);
        mTopCardView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(mTopCardView.getDrawingCache());
        //这里要销毁一下drawing 缓存，因为如果不销毁，当下次在getDrawingCache()的时候，拿的有可能是旧的缓存。
        mTopCardView.destroyDrawingCache();
        return bitmap;
    }


    private void addViewToDecorView(View view) {


        ViewGroup viewGroup = getDecorView(getContext());
        ImageView tempImageView = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tempImageView.setLayoutParams(layoutParams);
        tempImageView.setImageBitmap(createViewDrawable());
        viewGroup.addView(tempImageView);
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        tempImageView.setTranslationY(xy[1]);
        tempImageView.setTranslationX(xy[0]);
        tempImageView.setAlpha(1.0f);
        //把这个View添加到map中，动画做完后，从DecorView中移除。
        mFloatingViewMaps.put(String.valueOf(tempImageView.hashCode()), tempImageView);
        mFloatingView = tempImageView;
        mTopCardView.setVisibility(View.INVISIBLE);
    }

    @Nullable
    private ViewGroup getDecorView(Context context) {
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            return window != null ? (ViewGroup) window.getDecorView() : null;
        } else {
            throw new IllegalStateException("context is not Activity!");
        }
    }


    public void setAdapter(@NonNull SwitchCardView.Adapter adapter) {

        if (adapter == mAdapter) {
            return;
        }

        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        mDataSetObserver = new CardViewAdapterDataSetObserver();
        mAdapter.registerDataSetObserver(mDataSetObserver);
        removeAllViews();
        createChildView();

        mAdapter.notifyChangedToIndex(0);
    }

    private void createChildView() {
        mAdapter.createAndAddChildViewForShadow(this);
        mBottomCardView = mAdapter.createBottomCardView(this);
        mTopCardView = mAdapter.createTopCardView(this);
        addView(mBottomCardView);
        addView(mTopCardView);
        mBottomCardView.setRotation(BOTTOM_VIEW_ROTATION);
        mBottomCardView.setAlpha(0.0f);
    }


    @Nullable
    public Adapter getAdapter() {
        return mAdapter;
    }

    /**
     * @return 返回当前展示的卡片View，的position.
     * 例如：当卡片View在ListView/RecyclerView中使用的时候，滑动界面出界面，需要业务记录一下，滑动到哪里了（position），
     * 方便等在滑动回来的时候，bindData的时候，把原来的位置给设置回来{@link Adapter#notifyChangedToIndex(int)}，这样展示的卡片，还是原来的那个。
     */
    public int getCurrentIndex() {
        return mCurrentIndex;
    }


    public static abstract class Adapter extends Observable {

        private final CardViewDataSetObservable mCardViewDataSetObservable = new CardViewDataSetObservable();

        public void registerDataSetObserver(CardViewAdapterDataSetObserver observer) {
            mCardViewDataSetObservable.registerObserver(observer);
        }

        public void unregisterDataSetObserver(CardViewAdapterDataSetObserver observer) {
            mCardViewDataSetObservable.unregisterObserver(observer);
        }

        /**
         * Notifies the attached observers that the underlying data has been changed
         * and any View reflecting the data set should refresh itself.
         */
        public void notifyChangedToIndex(int position) {
            mCardViewDataSetObservable.notifyChangedToIndex(position);
        }


        /**
         * 这个函数的目的，是创建一个最顶部的View，也就是无滑动的时候，展示数据的那个View。
         *
         * @return 返回最顶部的一个View。
         */
        @NonNull
        public abstract View createTopCardView(@NonNull ViewGroup parent);

        /**
         * 这个函数的目的，是创建一个底部的View ，也就是滑动的时候，底部跟着旋转的View（也就是顶部view下面的那个View）。
         *
         * @return 返回底部的一个View。
         */
        @NonNull
        public abstract View createBottomCardView(@NonNull ViewGroup parent);


        /**
         * @return 返回当前数据源的个数。如果size <1的话，手势滑动将失效。
         */
        public abstract int getItemCount();


        /**
         * 调用时机：当手指已经拖动出一个卡片View（Bitmap）,移动的距离已经超过了阀值,松开手的那一刻
         * 开始做卡片飞出界面动画的那一刻调用。
         */
        public void onReleaseTouchForBeginSwitchAnim() {

        }

        /**
         * 调用时机：当手指已经拖动出一个卡片View（Bitmap），移动的距离已经超过阀值的时候，松手之后
         * 卡片飞出屏幕动画做完的一刻，会调用此函数。
         */
        public void onReleaseTouchForEndSwitchAnim() {

        }


        /**
         * 调用时机:当手指已经拖动出一个卡片View（Bitmap），但是移动的距离比较小，没有超过距离阀值，
         * 松开手之后的动画(把TopView和BottomView摆放回原来的位置的动画)开始的那一刻调用。
         */
        public void onReleaseTouchForBeginResetAnim() {

        }

        /**
         * 调用时机:当手指已经拖动出一个卡片View（Bitmap），但是移动的距离比较小，没有超过距离阀值，
         * 松开手之后的动画(把TopView和BottomView摆放回原来的位置的动画)做完的那一刻调用。
         */
        public void onReleaseTouchForEndResetAnim() {

        }

        /**
         * @param parent 这个函数的目的，创建卡片的阴影Shadow，对应的View,并Add到SwitchCardView中。
         */
        public void createAndAddChildViewForShadow(@NonNull ViewGroup parent) {

        }

        /**
         * @param position 卡片数据position
         * @param view     卡片中的item（其实是反复的利用topView和bottomView）
         */

        public abstract void renderView(int position, View view);

    }

    private boolean checkValid() {
        return mAdapter != null ? mAdapter.getItemCount() > 0 : false;
    }

    private class CardViewAdapterDataSetObserver {

        public void onChangedToIndex(int index) {
            if (checkValid()) {
                if (mTopCardView != null && mBottomCardView != null) {
                    int itemCount = mAdapter.getItemCount();
                    mCurrentIndex = index % itemCount;
                    mAdapter.renderView(mCurrentIndex, mTopCardView);
                    mAdapter.renderView((mCurrentIndex + 1) % itemCount, mBottomCardView);
                }
            }
        }
    }


    private static class CardViewDataSetObservable extends android.database.Observable<CardViewAdapterDataSetObserver> {
        /**
         * Invokes {@link CardViewAdapterDataSetObserver#onChangedToIndex(int)} on each observer.
         * Called when you wanted page to Index  CardView
         * Note:if itemCount < 0 ,or adapter is null ,this Method will not work!
         */
        public void notifyChangedToIndex(int position) {
            synchronized (mObservers) {
                // since onChanged() is implemented by the app, it could do anything, including
                // removing itself from {@link mObservers} - and that could cause problems if
                // an iterator is used on the ArrayList {@link mObservers}.
                // to avoid such problems, just march thru the list in the reverse order.
                for (int i = mObservers.size() - 1; i >= 0; i--) {
                    mObservers.get(i).onChangedToIndex(position);
                }
            }
        }
    }


}
