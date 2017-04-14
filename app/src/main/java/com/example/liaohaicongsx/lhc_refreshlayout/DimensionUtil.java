package com.example.liaohaicongsx.lhc_refreshlayout;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by liaohaicongsx on 2017/04/14.
 */
public class DimensionUtil {

    public static int dp2px(Context context,int dp){
        float density;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        density = metrics.density;
        return (int)(dp * density);
    }

}
