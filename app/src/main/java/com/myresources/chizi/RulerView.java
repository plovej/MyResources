package com.myresources.chizi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.myresources.R;


/**
 * 标尺类
 */
public class RulerView extends View {
    //getSimpleName()返回源代码中给出的底层类的简称。
    final String TAG = RulerView.class.getSimpleName();
    //开始范围
    private int mBeginRange;
    //结束范围
    private int mEndRange;
    /**
     * 内部宽度，也就是标尺每条的宽度
     */
    private int mInnerWidth;
    //标尺条目间的间隔
    private int mIndicatePadding;
    //显示的画笔
    private Paint mIndicatePaint;
    //文字画笔
    private Paint mTextPaint;
    //显示的宽度
    private int mIndicateWidth;
    //显示的大小
    private float mIndicateScale;
    //最后的手势的X坐标
    private int mLastMotionX;
    /**
     * 是否可以滑动
     */
    private boolean mIsDragged;
    //是否自动匹配
    private boolean mIsAutoAlign = true;
    //是否需要显示文字
    private boolean mIsWithText = true;
    //文字颜色
    private int mTextColor;
    //文字大小
    private float mTextSize;
    //标尺的颜色
    private int mIndicateColor;
    //大小比例监听器
    private OnScaleListener mListener;
    //标尺条显示的位置：top，bottom
    private int mGravity;
    /**
     * 标尺矩形（刻度条）
     */
    private Rect mIndicateLoc;

    /**
     * 滚动相关参数，这个类封装了滚动与超能力的界限
     */
    private OverScroller mOverScroller;
    /**
     * 帮助跟踪触摸事件的速度，用于执行投掷等动作。
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 触摸溢出
     */
    private int mTouchSlop;
    //最小滑动速率
    private int mMinimumVelocity;
    //最大速率
    private int mMaximumVelocity;


    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 最终都是调用此构造方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性数据集，并写入缺省值,自定义了8个属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RulerView);
        mIndicateColor = ta.getColor(R.styleable.RulerView_indicateColor, Color.BLACK);
        mTextColor = ta.getColor(R.styleable.RulerView_textColor, Color.GRAY);
        mTextSize = ta.getDimension(R.styleable.RulerView_textSize, 18);
        mBeginRange = ta.getInt(R.styleable.RulerView_begin, 0);
        mEndRange = ta.getInt(R.styleable.RulerView_end, 100);
        //标尺宽度
        mIndicateWidth = (int) ta.getDimension(R.styleable.RulerView_indicateWidth, 5);
        //标尺的间隙
        mIndicatePadding = (int) ta.getDimension(R.styleable.RulerView_indicatePadding, 15);
        ta.recycle();
        //标尺条显示的位置，缺省值为显示在底部
        int[] indices = new int[]{android.R.attr.gravity};
        ta = context.obtainStyledAttributes(attrs, indices);
        mGravity = ta.getInt(ta.getIndex(0), Gravity.BOTTOM);
        ta.recycle();
        //默认显示比例为0.7倍
        mIndicateScale = 0.7f;

        initValue();
    }

    /**
     * 初始化数值
     */
    private void initValue() {
        /**  创建这个滚动类，并设置滚动模式为：1.OVER_SCROLL_ALWAYS 标准模式
         * 还有两种滚动模式为：2.OVER_SCROLL_IF_CONTENT_SCROLLS 内容滚动
         * 3.OVER_SCROLL_NEVER 不滚动
         */
        mOverScroller = new OverScroller(getContext());
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        //获取视图配置，设置触摸溢出，和最小和最大的触摸速率
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        //设置标尺的画笔，实心画
        mIndicatePaint = new Paint();
        mIndicatePaint.setStyle(Paint.Style.FILL);
        //设置文字画笔，实心画，并消除锯齿
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        //内部宽度（标尺结束范围-标尺开始范围）*指示宽度
        mInnerWidth = (mEndRange - mBeginRange) * getIndicateWidth();
        //标尺定位为一个矩形
        mIndicateLoc = new Rect();
    }


    /**
     * 重写绘制方法
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        /**
         * 当我们对画布进行旋转，缩放，平移等操作的时候其实我们是想对特定的元素进行操作，
         * 比如图片，一个矩形等，但是当你用canvas的方法来进行这些操作的时候，
         * 其实是对整个画布进行了操作，那么之后在画布上的元素都会受到影响，
         * 所以我们在操作之前调用canvas.save()来保存画布当前的状态，
         * 当操作之后取出之前保存过的状态，这样就不会对其他的元素进行影响
         */
        int count = canvas.save();
        //循环绘制标尺条（刻度），根据最大值和最小值来绘制
        for (int value = mBeginRange, position = 0; value <= mEndRange; value++, position++) {
            drawIndicate(canvas, position);
            //如果需要数字，还需要在刻度下绘制数字
            if (mIsWithText)
                drawText(canvas, position, String.valueOf(value));

        }

        //恢复Canvas的状态
        canvas.restoreToCount(count);

    }

    /**
     * 绘制标尺条（刻度），0到100就会显示100个刻度
     *
     * @param canvas   画布
     * @param position
     */
    private void drawIndicate(Canvas canvas, int position) {
        computeIndicateLoc(mIndicateLoc, position);
        int left = mIndicateLoc.left + mIndicatePadding;
        int right = mIndicateLoc.right - mIndicatePadding;
        int top = mIndicateLoc.top;
        int bottom = mIndicateLoc.bottom;

        if (position % 5 != 0) {
            int indicateHeight = bottom - top;
            if (isAlignTop()) {
                bottom = (int) (top + indicateHeight * mIndicateScale);
            } else {
                top = (int) (bottom - indicateHeight * mIndicateScale);
            }
        }

        mIndicatePaint.setColor(mIndicateColor);
        canvas.drawRect(left, top, right, bottom, mIndicatePaint);
    }

    /**
     * 绘制文字，每5个刻度绘制一个文字用于提示
     *
     * @param canvas
     * @param position
     * @param text
     */
    private void drawText(Canvas canvas, int position, String text) {
        if (position % 5 != 0)
            return;

        computeIndicateLoc(mIndicateLoc, position);
        int textHeight = computeTextHeight();

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        int x = (mIndicateLoc.left + mIndicateLoc.right) / 2;
        int y = mIndicateLoc.bottom + textHeight;

        if (!isAlignTop()) {
            y = mIndicateLoc.top;
            mTextPaint.getTextBounds(text, 0, text.length(), mIndicateLoc);
            y += mIndicateLoc.top / 2;  //增加一些偏移
        }

        canvas.drawText(text, x, y, mTextPaint);
    }

    /**
     * 计算指示器的位置：设置左上右下
     * 最终是设置了此矩形（刻度的左上右下）
     *
     * @param outRect  矩形
     * @param position 位置数值（代表第几个刻度）
     */
    private void computeIndicateLoc(Rect outRect, int position) {
        if (outRect == null)
            return;

        int height = getHeight();
        int indicate = getIndicateWidth();

        int left = (indicate * position);
        int right = left + indicate;
        int top = getPaddingTop();//获得当前View的顶内距
        int bottom = height - getPaddingBottom();//视图高度-视图低内距

        if (mIsWithText) {
            int textHeight = computeTextHeight();
            if (isAlignTop())
                bottom -= textHeight;//如果是刻度显示在顶部，底部要减去文字的高度
            else
                top += textHeight;//如果是刻度显示在底部，顶部要加上文字的高度
        }
        //文字偏移量，左边和右边都加上一个偏移量
        int offsets = getStartOffsets();
        left += offsets;
        right += offsets;
        outRect.set(left, top, right, bottom);
    }

    /**
     * 开始偏移，如果要包含文字的话才需要偏移。
     *
     * @return
     */
    private int getStartOffsets() {
        if (mIsWithText) {
            String text = String.valueOf(mBeginRange);
            //返回文字的宽度
            int textWidth = (int) mTextPaint.measureText(text, 0, text.length());
            return textWidth / 2;//实际偏移文字宽度的一半，使其居中显示
        }
        return 0;
    }

    /**
     * 触摸相关事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //如果不存在初始速度跟踪
        initVelocityTrackerIfNotExists();
        //速度追踪者 添加移动事件
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下时如果滑动还没结束
                if (mIsDragged = !mOverScroller.isFinished()) {
                    if (getParent() != null)
                        //要求禁止拦截触摸事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                }
                //如果动画没结束，就结束动画
                if (!mOverScroller.isFinished())
                    mOverScroller.abortAnimation();
                //记录按下的x的坐标
                mLastMotionX = (int) event.getX();

                return true;

            case MotionEvent.ACTION_MOVE:
                //移动时x的值，并得到（按下x值-移动x）值的差值
                int curX = (int) event.getX();
                int deltaX = mLastMotionX - curX;
                //如果滑动未结束，且移动距离的绝对值大于触摸溢出量
                if (!mIsDragged && Math.abs(deltaX) > mTouchSlop) {
                    if (getParent() != null)
                        //如果有父级控件，就告诉父级控件不要拦截我的触摸事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    //并设置滑动结束
                    mIsDragged = true;
                    //如果触摸差值》0，触摸差值需要-触摸溢出量，否则加上
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                //如果滑动结束，最后的x坐标就是当前触摸的的点
                if (mIsDragged) {
                    mLastMotionX = curX;
                    //如果滚动的X值《0或者大于最大的滚动值了，让触摸差值*0.7
                    if (getScrollX() <= 0 || getScrollX() >= getMaximumScroll())
                        deltaX *= 0.7;
                    //滚动超出正常的标准行为的视图,速率监听清除？？？？？？？？？？？？？
                    if (overScrollBy(deltaX, 0, getScrollX(), getScrollY(), getMaximumScroll(), 0, getWidth(), 0, true)) {
                        mVelocityTracker.clear();
                    }

                }

                break;
            case MotionEvent.ACTION_UP: {
                if (mIsDragged) {
                    //检查滑动的速度，1000单位
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    //获得X轴上的流速
                    int initialVelocity = (int) mVelocityTracker.getXVelocity();
                    //如果x轴流速》最小流速
                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        fling(-initialVelocity);
                    } else {
                        //alignCenter();
                        //回弹到末尾
                        sprintBack();
                    }
                }
                //滑动结束
                mIsDragged = false;
                //释放追踪器资源
                recycleVelocityTracker();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                //如果滑动结束，且滚动结束，就回滚
                if (mIsDragged && mOverScroller.isFinished()) {
                    sprintBack();
                }

                mIsDragged = false;
                recycleVelocityTracker();
                break;
            }
        }

        return true;
    }

    /**
     * 刷新参数值
     */
    private void refreshValues() {
        //内部宽度 = （最大值-开始值）*刻度宽度
        mInnerWidth = (mEndRange - mBeginRange) * getIndicateWidth();
        invalidateView();

    }

    /**
     * 最终指示宽度 ：刻度宽度+刻度内边距+刻度内边距
     *
     * @return
     */
    private int getIndicateWidth() {
        return mIndicateWidth + mIndicatePadding + mIndicatePadding;
    }

    /**
     * 获取最小滚动值。
     *
     * @return
     */
    private int getMinimumScroll() {
        return -(getWidth() - getIndicateWidth()) / 2 + getStartOffsets();
    }

    /**
     * 获取最大滚动值。
     *
     * @return
     */
    private int getMaximumScroll() {
        return mInnerWidth + getMinimumScroll();
    }

    /**
     * 调整刻度，使其居中。
     */
    private void adjustIndicate() {
        if (!mOverScroller.isFinished())
            mOverScroller.abortAnimation();

        int position = computeSelectedPosition();
        int scrollX = getScrollByPosition(position);
        scrollX -= getScrollX();

        if (scrollX != 0) {
            //滚动边界开始滚动
            mOverScroller.startScroll(getScrollX(), getScrollY(), scrollX, 0);
            invalidateView();
        }
    }

    /**
     * 投掷
     *
     * @param velocityX 根据x轴滑动速率，来回退刷新界面
     */
    public void fling(int velocityX) {
        mOverScroller.fling(getScrollX(), getScrollY(), velocityX, 0, getMinimumScroll(), getMaximumScroll(), 0, 0, getWidth() / 2, 0);
        invalidateView();
    }

    /**
     * 回弹
     */
    public void sprintBack() {
        mOverScroller.springBack(getScrollX(), getScrollY(), getMinimumScroll(), getMaximumScroll(), 0, 0);
        invalidateView();
    }


    public void setOnScaleListener(OnScaleListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }

    /**
     * 获取position的绝对滚动位置。
     *
     * @param position
     * @return
     */
    private int getScrollByPosition(int position) {
        computeIndicateLoc(mIndicateLoc, position);
        int scrollX = mIndicateLoc.left - getStartOffsets() + getMinimumScroll();
        return scrollX;
    }

    /**
     * 计算当前已选择的位置
     *
     * @return
     */
    public int computeSelectedPosition() {
        //计算出两个刻度的中间位置
        int centerX = getScrollX() - getMinimumScroll() + getIndicateWidth() / 2;
        //通过中间位置来判断选择的刻度值位置
        centerX = Math.max(0, Math.min(mInnerWidth, centerX));
        int position = centerX / getIndicateWidth();
        return position;
    }

    /**
     * 平滑滚动
     *
     * @param position
     */
    public void smoothScrollTo(int position) {
        //如果选择的位置<0或者开始值+选择位置大于最终值，就直接返回吧
        if (position < 0 || mBeginRange + position > mEndRange)
            return;
        //如果滚动没有完成，中断它的动画吧
        if (!mOverScroller.isFinished())
            mOverScroller.abortAnimation();

        int scrollX = getScrollByPosition(position);
        mOverScroller.startScroll(getScrollX(), getScrollY(), scrollX - getScrollX(), 0);
        invalidateView();
    }

    /**
     * 平滑滚动到的值
     *
     * @param value
     */
    public void smoothScrollToValue(int value) {
        int position = value - mBeginRange;
        smoothScrollTo(position);
    }

    /**
     * 触发放大缩小事件
     *
     * @param scale
     */
    private void onScaleChanged(int scale) {
        if (mListener != null)
            mListener.onScaleChanged(scale);
    }

    /**
     * 重新在滚动时的事件
     *
     * @param scrollX
     * @param scrollY
     * @param clampedX 固定的x
     * @param clampedY 固定的Y
     */
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        //如果滚动没有完成，设置滚动x参数，并监听滚动
        if (!mOverScroller.isFinished()) {
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            setScrollX(scrollX);
            onScrollChanged(scrollX, scrollY, oldX, oldY);
            if (clampedX) {
                //sprintBack();
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
        //如果监听器不为null，赋值当前选择的位置，并触发缩放改变事件
        if (mListener != null) {
            int position = computeSelectedPosition();
            onScaleChanged(position + mBeginRange);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    /**
     * 计算文字高度
     *
     * @return
     */
    private int computeTextHeight() {
        //使用FontMetrics对象，计算文字的坐标。
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        return (int) textHeight;
    }

    private boolean isAlignTop() {
        //&为位运算符，就是32位二进制值得比较
        return (mGravity & Gravity.TOP) == Gravity.TOP;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
        invalidateView();
    }

    /**
     * 计算滚动
     */
    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            // 返回滚动中的电流偏移量,百度居然这么翻译
            int x = mOverScroller.getCurrX();
            int y = mOverScroller.getCurrY();
            //滚动过多得操作
            overScrollBy(x - oldX, y - oldY, oldX, oldY, getMaximumScroll(), 0, getWidth(), 0, false);
            invalidateView();
        } else if (!mIsDragged && mIsAutoAlign) {//如果不再滚动且开启了自动对齐
            adjustIndicate();
        }
    }

    @Override
    protected int computeHorizontalScrollRange() {
        return getMaximumScroll();
    }

    /**
     * 刷新界面
     * 如果版本大于16（4.1）
     * 使用postInvalidate可以直接在线程中更新界面
     * invalidate()必须在UI线程中使用
     */
    public void invalidateView() {
        if (Build.VERSION.SDK_INT >= 16) {
            postInvalidateOnAnimation();
        } else
            invalidate();
    }

    /**
     * 获得周转率追踪器
     */
    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            //获得当前周转率追踪
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    /**
     * 释放 周转率追踪器资源
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 放大缩小监听接口
     */
    public interface OnScaleListener {
        void onScaleChanged(int scale);

    }

    /**
     * 设置刻度的宽度
     *
     * @param indicateWidth
     */
    public void setIndicateWidth(@IntegerRes int indicateWidth) {
        this.mIndicateWidth = indicateWidth;
        refreshValues();
    }

    /**
     * 设置刻度内间距
     *
     * @param indicatePadding
     */
    public void setIndicatePadding(@IntegerRes int indicatePadding) {
        this.mIndicatePadding = indicatePadding;
        refreshValues();
    }

    public void setWithText(boolean withText) {
        this.mIsWithText = withText;
        refreshValues();
    }

    public void setAutoAlign(boolean autoAlign) {
        this.mIsAutoAlign = autoAlign;
        refreshValues();
    }

    /**
     * 是否显示文字
     *
     * @return
     */
    public boolean isWithText() {
        return mIsWithText;
    }

    /**
     * 自动对齐刻度
     *
     * @return
     */
    public boolean isAutoAlign() {
        return mIsAutoAlign;
    }
}
