package com.liye.richedittextdemo.richedittext.span;

import android.text.style.ForegroundColorSpan;

import com.liye.richedittextdemo.R;
import com.liye.richedittextdemo.richedittext.utils.ContextUtils;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/28 下午4:00
 */
public class UrlSpan extends ForegroundColorSpan implements Span<String> {
    private String mUrl;

    public UrlSpan(String url) {
        super(ContextUtils.getContext().getResources().getColor(R.color.theme_3));
        mUrl = url;
    }

    @Override
    public String getValue() {
        return mUrl;
    }
}
