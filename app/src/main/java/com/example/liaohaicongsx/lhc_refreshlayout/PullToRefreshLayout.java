package com.example.liaohaicongsx.lhc_refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by liaohaicongsx on 2017/04/14.
 */
public class PullToRefreshLayout extends LinearLayout {


    private Context context;

    private View headView;
    private ListView listView;


    private int headerHeight;  //headView的高度
    private int headerTopMargin;  //headView的topMargin

    private MarginLayoutParams headerParams;

    private int preX,preY;
    private int toX,toY;
    private int disX,disY;

    private final int touchSlop = 10;

    public PullToRefreshLayout(Context context) {
        this(context,null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        headView = LayoutInflater.from(context).inflate(R.layout.layout_header_view,null);
        headerHeight = DimensionUtil.dp2px(context,45);
//        headerParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,headerHeight);
//        headerParams.topMargin = - headerHeight;
        addView(headView);  //解决这个设置布局参数无效的问题？
        headerParams = (MarginLayoutParams) headView.getLayoutParams();
        headerParams.topMargin = - headerHeight;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        listView = (ListView) getChildAt(1);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int maxTopMargin = DimensionUtil.dp2px(context,80);
        int minTopMargin = -headerHeight;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                preY = (int) ev.getRawY();
                headerParams = (MarginLayoutParams) headView.getLayoutParams();
                break;
            case MotionEvent.ACTION_MOVE:
                if(listView.getFirstVisiblePosition() == 0) {
                    toY = (int) ev.getRawY();
                    disY = toY - preY;
                    preY = toY;
                    if(disY < 0){
                        break;
                    }
                    headerParams.topMargin += (disY * 0.4);
                    if (headerParams.topMargin >= maxTopMargin) {
                        headerParams.topMargin = maxTopMargin;
                    }
                    if(headerParams.topMargin <= minTopMargin){
                        headerParams.topMargin = minTopMargin;
                    }
                    headView.setLayoutParams(headerParams);
                }break;
            case MotionEvent.ACTION_UP:
                headerParams.topMargin = -headerHeight;
                headView.setLayoutParams(headerParams);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //处理滑动冲突,这是个大问题啊
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                headerParams = (MarginLayoutParams) headView.getLayoutParams();
                 preX = (int) ev.getRawX();
                 preY = (int) ev.getRawY();
            case MotionEvent.ACTION_MOVE:
//                if(listView.getFirstVisiblePosition() == 0){
//                    return true;
//                }else{
//
//                    return false;
//                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action  = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                preY = (int) event.getRawY();
                headerParams = (MarginLayoutParams) headView.getLayoutParams();

                break;
            case MotionEvent.ACTION_MOVE:
                toY = (int) event.getRawY();
                disY = toY - preY;
                preY = toY;
                headerParams.topMargin += (disY * 0.4);
                if(headerParams.topMargin > DimensionUtil.dp2px(context,80)){
                    headerParams.topMargin = DimensionUtil.dp2px(context,80);
                }
                headView.setLayoutParams(headerParams);
                break;
            case MotionEvent.ACTION_UP:
                headerParams.topMargin = - headerHeight;
                headView.setLayoutParams(headerParams);
                break;
            default:
                break;
        }
        return true;
    }
}
