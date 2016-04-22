package com.liye.richedittextdemo.richedittext.effects;


import com.liye.richedittextdemo.richedittext.span.BoldSpan;
import com.liye.richedittextdemo.richedittext.span.Span;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/1/7 下午3:23
 */
public class BoldEffect extends Effect<Boolean> {
    @Override
    protected Class<? extends Span> getSpanClazz() {
        return BoldSpan.class;
    }

    @Override
    protected Span<Boolean> newSpan(Boolean value) {
        return value ? new BoldSpan() : null;
    }
}
