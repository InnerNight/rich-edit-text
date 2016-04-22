package com.liye.richedittextdemo.richedittext.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/4/20 上午11:54
 */
public class DisplayUtils {
    private static final float ROUND_DIFFERENCE = 0.5f;
    private static DisplayMetrics sDisplayMetrics = null;

    public static void init(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * dp 转 px
     * 注意正负数的四舍五入规则
     *
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static int dp2px(int dp) {
        return (int) (dp * sDisplayMetrics.density + (dp > 0 ? ROUND_DIFFERENCE : - ROUND_DIFFERENCE));
    }

    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位：像素
     *
     * @return 屏幕高度
     */
    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }
}
