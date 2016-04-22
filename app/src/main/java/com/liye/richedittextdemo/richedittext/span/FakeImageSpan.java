package com.liye.richedittextdemo.richedittext.span;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

/**
 * 该类用来在imagespan的图片未加载前占位
 *
 * @author liye
 * @version 4.1.0
 * @since: 15/12/30 下午5:33
 */
public class FakeImageSpan extends CharacterStyle implements Span<String> {
    private String mUrl;

    public FakeImageSpan(String url) {
        mUrl = url;
    }

    @Override
    public String getValue() {
        return mUrl;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
    }
}
