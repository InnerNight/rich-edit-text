package com.liye.richedittextdemo.richedittext.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;

import com.liye.richedittextdemo.richedittext.IEmojiFactory;
import com.liye.richedittextdemo.richedittext.span.BoldSpan;
import com.liye.richedittextdemo.richedittext.span.EmojiSpan;
import com.liye.richedittextdemo.richedittext.span.FakeImageSpan;
import com.liye.richedittextdemo.richedittext.span.ImageSpan;
import com.liye.richedittextdemo.richedittext.span.UrlSpan;

import java.util.Arrays;
import java.util.List;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/28 下午2:27
 */
public class RichTextUtils {
    public static Spanned convertRichTextToSpanned(String richText, IEmojiFactory iEmojiFactory) {
        return RichTextConvertor.fromRichText(richText, iEmojiFactory);
    }

    public static String convertSpannedToRichText(Spanned spanned) {
        List<CharacterStyle> spanList =
                Arrays.asList(spanned.getSpans(0, spanned.length(), CharacterStyle.class));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spanned);
        for (CharacterStyle characterStyle : spanList) {
            int start = stringBuilder.getSpanStart(characterStyle);
            int end = stringBuilder.getSpanEnd(characterStyle);
            if (start >= 0) {
                String htmlStyle = handleCharacterStyle(characterStyle,
                        stringBuilder.subSequence(start, end).toString());
                if (htmlStyle != null) {
                    stringBuilder.replace(start, end, htmlStyle);
                }
            }
        }
        return stringBuilder.toString();
    }

    private static String handleCharacterStyle(CharacterStyle characterStyle, String text) {
        if (characterStyle instanceof BoldSpan) {
            return String.format("<b>%s</b>", text);
        } else if (characterStyle instanceof UrlSpan) {
            UrlSpan span = (UrlSpan) characterStyle;
            return String.format("<a href=\"%s\">%s</a>", span.getValue(), text);
        } else if (characterStyle instanceof EmojiSpan) {
            EmojiSpan span = (EmojiSpan) characterStyle;
            return String.format("<img src=\"%s\" alt=\"[%s]\" class=\"yiqiFace\"/>",
                    span.getUrl(), span.getName());
        } else if (characterStyle instanceof FakeImageSpan) {
            FakeImageSpan span = (FakeImageSpan) characterStyle;
            return String.format("<img src=\"%s\" />", span.getValue());
        } if (characterStyle instanceof ImageSpan) {
            ImageSpan span = (ImageSpan) characterStyle;
            return String.format("<img src=\"%s\" />", TextUtils.isEmpty(span.getUrl()) ?
                    span.getFilePath() : span.getUrl());
        }
        return null;
    }
}
