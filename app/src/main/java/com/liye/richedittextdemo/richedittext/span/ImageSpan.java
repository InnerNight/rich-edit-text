package com.liye.richedittextdemo.richedittext.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/29 上午11:39
 */
public class ImageSpan extends android.text.style.ImageSpan implements Span<String> {
    private String mFilePath;
    private String mUrl;

    public ImageSpan(Context context, Bitmap bitmap, String filePath) {
        super(context, bitmap);
        mFilePath = filePath;
    }

    public ImageSpan(Drawable drawable) {
        super(drawable);
    }

    public String getFilePath() {
        return mFilePath;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String getValue() {
        return mUrl;
    }
}
