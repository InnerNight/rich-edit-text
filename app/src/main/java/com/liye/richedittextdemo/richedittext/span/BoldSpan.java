package com.liye.richedittextdemo.richedittext.span;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/28 下午4:07
 */
public class BoldSpan extends StyleSpan implements Span<Boolean> {
    public BoldSpan() {
        super(Typeface.BOLD);
    }

    @Override
    public Boolean getValue() {
        return Boolean.TRUE;
    }
}
