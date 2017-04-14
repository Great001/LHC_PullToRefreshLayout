package com.example.liaohaicongsx.lhc_refreshlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView mLvTest;
    private lvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLvTest = (ListView) findViewById(R.id.lv_test);
        adapter = new lvAdapter(this);
        mLvTest.setAdapter(adapter);

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
}
