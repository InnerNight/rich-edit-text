package com.liye.richedittextdemo.richedittext.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.liye.richedittextdemo.richedittext.Emoji;
import com.liye.richedittextdemo.richedittext.utils.DisplayUtils;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/1/12 下午3:28
 */
public class EmojiSpan extends ImageSpan {
    private static final int EMOJI_DISPLAY_SIZE = 22;

    private String mName;

    public EmojiSpan(Context context, Emoji emoji) {
        super(convertBitmapToSizedDrawable(context, emoji.getBitmap()));
        mName = emoji.getName();
        setUrl(emoji.getUrl());
    }

    public String getName() {
        return mName;
    }

    private static Drawable convertBitmapToSizedDrawable(Context context, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setBounds(0, 0, DisplayUtils.dp2px(EMOJI_DISPLAY_SIZE),
                DisplayUtils.dp2px(EMOJI_DISPLAY_SIZE));
        return drawable;
    }
}
