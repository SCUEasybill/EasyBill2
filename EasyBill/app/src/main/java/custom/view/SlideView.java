package custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import xyz.anmai.easybill.R;

/**
 * Created by anquan on 2016/3/2.
 */
public class SlideView extends LinearLayout {
    private static final String TAG = "SlideView";
    private Context mContext;
    private LinearLayout mViewContent;//用来放置所有view的容器
    private RelativeLayout mHolder;//用来放置内置view的容器，如删除 按钮
    private Scroller mScroller;//弹性滑动对象，提供弹性滑动效果
    private OnSlideListener mOnSlideListener;//滑动回调接口，用来向上层通知滑动事件
    private int mHolderWidth = 150;//内置容器的宽度，单位dp
    //分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    //用来控制滑动角度，仅当角度满足如下条件才会进行滑动：tan a = deltaX / deltaY >2
    private static final int TAN = 2;

    public SlideView(Context context) {
        super(context);
        initView();
    }
    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public interface OnSlideListener {
        //slideView的三种状态
        public static final int SLIDE_STATUS_OFF = 0;
        public static final int SLIDE_STATUS_START_SCROLL = 1;
        public static final int SLIDE_STATUS_ON = 2;

        public void onSlide(View view, int status);
    }

    private void initView() {
        mContext = getContext();
        //初始化弹性滑动对象
        mScroller = new Scroller(mContext);
        //设置其方向为横向
        setOrientation(LinearLayout.HORIZONTAL);
        //将布局文件加载进来
        View.inflate(mContext, R.layout.slide_view, this);
        mViewContent = (LinearLayout) findViewById(R.id.view_content);
        mHolderWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources().getDisplayMetrics()));
    }

    //将view加入到viewContent中
    public void setContentView(View view) {
        mViewContent.addView(view);
    }

    //设置滑动回调
    public void setOnSlideListener(OnSlideListener onSlideListener) {
        mOnSlideListener = onSlideListener;
    }

    //将当前状态设置为关闭
    public void shrink() {
        if (getScaleX() != 0) {
            this.smoothScrollTo(0, 0);
        }
    }

    //根据Motionevent来进行滑动，这个方法的作用相当于onTouchEvent
    //如果你不需要处理滑动冲突，可以直接重命名，照样能正常工作
    public void  onRequireTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();
        Log.d(TAG, "x=" + x + " y=" + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, OnSlideListener.SLIDE_STATUS_START_SCROLL);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    //滑动不满足条件，不做横向滑动
                    break;
                }
                //计算滑动终点是否合法，放止滑动越界
                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mHolderWidth) {
                        newScrollX = mHolderWidth;
                    }
                    this.scrollTo(newScrollX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                int newScrollX = 0;
                //当松手的时候，会自动向两边滑动，具体滑向哪边，要看当前所处的位置
                if (scrollX - mHolderWidth * 0.75 > 0) {
                    newScrollX = mHolderWidth;
                }
                //慢慢滑向终点
                this.smoothScrollTo(newScrollX, 0);
                //通知上层滑动事件
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, newScrollX == 0?OnSlideListener.SLIDE_STATUS_OFF : OnSlideListener.SLIDE_STATUS_ON);
                }
                break;
            }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
    }

    private void smoothScrollTo(int destX, int destY) {
        //缓缓滚动到指定位置
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        //以三倍时长滑向destX，效果就是慢慢滑动
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
