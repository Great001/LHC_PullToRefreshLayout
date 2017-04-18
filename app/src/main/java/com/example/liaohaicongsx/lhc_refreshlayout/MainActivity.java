package com.example.liaohaicongsx.lhc_refreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {

    public static final int MSG_REFRESH_ERROR = 1000;
    public static final int MSG_FINISH_REFRESH = 1002;

    private PullToRefreshLayout mPrefreshlayout;
    private ListView mLvTest;
    private lvAdapter adapter;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_REFRESH:
                    mPrefreshlayout.refreshComplete();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefreshlayout = (PullToRefreshLayout) findViewById(R.id.prfl_test);
        mLvTest = (ListView) findViewById(R.id.lv_test);
        adapter = new lvAdapter(this);
        mLvTest.setAdapter(adapter);
        mPrefreshlayout.setOnRefreshLister(this);

        /*
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        ApplicationInfo applicationInfo = packageInfos.get(0).applicationInfo;
        Drawable appIcon = applicationInfo.loadIcon(packageManager);
        String appLabel = applicationInfo.loadLabel(packageManager).toString();
        String sourceDir = applicationInfo.sourceDir;  //若是系统应用：/system/app/***.apk 否则：/data/app/***.apk

        //判断是否是系统应用
        if((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) != 0){
            Toast.makeText(MainActivity.this, "系统应用", Toast.LENGTH_SHORT).show();
        }
        */

    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(MSG_FINISH_REFRESH,500);
    }
}
