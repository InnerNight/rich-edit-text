package com.liye.richedittextdemo.richedittext.utils;

import android.content.Context;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/4/20 下午3:30
 */
public class ContextUtils {
    private static Context sContext;

    /**
     * @param context context
     */
    public static void setContext(Context context) {
        sContext = context;
    }

    /**
     * @return Context
     */
    public static Context getContext() {
        return sContext;
    }
}
