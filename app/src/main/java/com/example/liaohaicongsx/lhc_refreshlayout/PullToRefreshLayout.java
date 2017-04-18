package com.example.liaohaicongsx.lhc_refreshlayout;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * Created by Administrator on 2017/4/14.
 */
public class PullToRefreshLayout extends LinearLayout {

    public static final String TAG = "PullToRefreshLayout";

    public static final int STATUS_FINISH_REFRESH = 0;
    public static final int STATUS_PULL_TO_REFRESH = 1;
    public static final int STATUS_RELEASE_TO_REFRESH = 2;
    public static final int STATUS_REFRESHING = 3;

    public static final int MSG_REFRESH_ERROR = 1000;
    public static final int MSG_FINISH_REFRESH = 1002;

    private static final int touchSlop = 10;

    private Context context;

    private View headView;
    private TextView tvRefreshStatus;
    private ProgressBar pbLoading;

    private ListView listView;

    private int headerHeight;  //headView的高度
    private int maxTopMargin;
    private int minTopMargin;
    private MarginLayoutParams headerParams;

    private boolean isFirstLayout = true;

    private int fromY;
    private int toY;
    private int disY;

    private int refreshStatus = STATUS_FINISH_REFRESH;

    private OnRefreshListener onRefreshListener;

    private static final int refreshTimeOut = 5000;
    //错误处理，主要针对网络错误
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_ERROR:
                    if (refreshStatus == STATUS_REFRESHING) {
                        tvRefreshStatus.setText(R.string.refresh_failed);
                        pbLoading.setVisibility(GONE);
                        handler.sendEmptyMessageDelayed(MSG_FINISH_REFRESH, 2000);
                    }
                    break;
                case MSG_FINISH_REFRESH:
                    refreshComplete();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOrientation(VERTICAL);

        headView = LayoutInflater.from(context).inflate(R.layout.layout_header_view, null);
        tvRefreshStatus = (TextView) headView.findViewById(R.id.tv_refresh_status);
        pbLoading = (ProgressBar) headView.findViewById(R.id.pb_loading);

        addView(headView);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isFirstLayout) {
            headerHeight = headView.getHeight();
            maxTopMargin = headerHeight + DimensionUtil.dp2px(context, 25);
            minTopMargin = -headerHeight;
            headerParams = (MarginLayoutParams) headView.getLayoutParams();
            headerParams.topMargin = -headerHeight;  //隐藏headView
            listView = (ListView) getChildAt(1);  //获取listView实例
            isFirstLayout = false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canPull()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:   //这里不能拦截，否则会影响listView的滑动
                    fromY = (int) ev.getRawY();
                    headerParams = (MarginLayoutParams) headView.getLayoutParams();
                    Log.d(TAG,"actino down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    toY = (int) ev.getRawY();
                    disY = toY - fromY;
                    if (disY > 0) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        if (refreshStatus == STATUS_REFRESHING) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                fromY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                toY = (int) event.getRawY();
                disY = toY - fromY;
                fromY = toY;
                if (disY < touchSlop) {
                    break;
                }
                if (refreshStatus != STATUS_REFRESHING) {
                    headerParams.topMargin += (disY * 0.4);
                    if (headerParams.topMargin < 15) {
                        tvRefreshStatus.setText(R.string.pull_to_refresh);
                        refreshStatus = STATUS_PULL_TO_REFRESH;
                    } else {
                        tvRefreshStatus.setText(R.string.release_to_refresh);
                        refreshStatus = STATUS_RELEASE_TO_REFRESH;
                    }

                    if (headerParams.topMargin >= maxTopMargin) {
                        headerParams.topMargin = maxTopMargin;
                    } else if (headerParams.topMargin <= minTopMargin) {
                        headerParams.topMargin = minTopMargin;
                    }

                    headView.setLayoutParams(headerParams);
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                if (refreshStatus == STATUS_PULL_TO_REFRESH) {
                    headerParams.topMargin = - headerHeight;
                    headView.setLayoutParams(headerParams);
                    refreshStatus = STATUS_FINISH_REFRESH;
                } else if (refreshStatus == STATUS_RELEASE_TO_REFRESH) {
                    pbLoading.setVisibility(VISIBLE);
                    tvRefreshStatus.setText(R.string.refreshing);
                    refreshStatus = STATUS_REFRESHING;
//                        handler.sendEmptyMessageAtTime(MSG_REFRESH_ERROR, SystemClock.uptimeMillis() + refreshTimeOut);
                    doRefresh();
                }
                break;
        }
        return true;
    }

    public boolean canPull() {
        if (listView.getChildCount() != 0) {
            if (listView.getFirstVisiblePosition() == 0) {
                int firstViewTop = listView.getChildAt(0).getTop();
                return firstViewTop == listView.getTop()  && refreshStatus != STATUS_REFRESHING;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    public void doRefresh() {
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    public void refreshComplete() {
        pbLoading.setVisibility(GONE);
        headerParams.topMargin = - headerHeight;
        headView.setLayoutParams(headerParams);
        refreshStatus = STATUS_FINISH_REFRESH;
    }

    public void refreshError(){
        handler.sendEmptyMessage(MSG_REFRESH_ERROR);
    }



    public int getRefreshStatus(){
        return refreshStatus;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshLister(OnRefreshListener listener) {
        onRefreshListener = listener;
    }

}