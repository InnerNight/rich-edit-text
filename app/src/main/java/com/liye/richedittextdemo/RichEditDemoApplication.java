package com.liye.richedittextdemo;

import android.app.Application;

import com.liye.richedittextdemo.richedittext.utils.ContextUtils;
import com.liye.richedittextdemo.richedittext.utils.DisplayUtils;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/4/20 下午6:21
 */
public class RichEditDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.setContext(this);
        DisplayUtils.init(this);
    }
}
