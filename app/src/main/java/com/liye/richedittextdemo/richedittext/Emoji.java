package com.liye.richedittextdemo.richedittext;

import android.graphics.Bitmap;


/**
 * @author liye
 * @version 4.1.0
 * @since: 16/1/11 下午8:31
 */
public class Emoji {
    private String mName;

    private String mUrl;

    private String mFile;

    private Bitmap mEmojiBitmap;

    public void setBitmap(Bitmap bitmap) {
        mEmojiBitmap = bitmap;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Bitmap getBitmap() {
        return mEmojiBitmap;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFile() {
        return mFile;
    }
}
