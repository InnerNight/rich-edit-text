package com.liye.richedittextdemo.richedittext.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.liye.richedittextdemo.richedittext.IEmojiFactory;
import com.liye.richedittextdemo.richedittext.span.BoldSpan;
import com.liye.richedittextdemo.richedittext.span.EmojiSpan;
import com.liye.richedittextdemo.richedittext.span.FakeImageSpan;
import com.liye.richedittextdemo.richedittext.span.UrlSpan;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/30 下午12:01
 */
public class RichTextConvertor implements ContentHandler {
    private SpannableStringBuilder mResult;
    private String mSource;
    private Parser mParser;
    private IEmojiFactory mIEmojiFactory;

    private static class Href {
        String mHref;

        Href(String href) {
            mHref = href;
        }
    }

    /*
         * While the spanned text is build we need to use SPAN_EXCLUSIVE_EXCLUSIVE instead of SPAN_EXCLUSIVE_INCLUSIVE
         * or each span would expand to the end of the text as we append more text.
         * Therefore we use a TemporarySpan which will be replaced by the "real" span once the full spanned text is built.
         */
    private static class TemporarySpan {
        Object mSpan;

        TemporarySpan(Object span) {
            mSpan = span;
        }

        void swapIn(SpannableStringBuilder builder) {
            int start = builder.getSpanStart(this);
            int end = builder.getSpanEnd(this);
            builder.removeSpan(this);
            if (start >= 0 && end > start && end <= builder.length()) {
                builder.setSpan(mSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
    }

    public RichTextConvertor(IEmojiFactory emojiFactory) {
        mIEmojiFactory = emojiFactory;
    }

    /**
     * Lazy initialization holder for HTML parser. This class will
     * a) be preloaded by the zygote, or b) not loaded until absolutely
     * necessary.
     */
    private static class HtmlParser {
        private static final HTMLSchema SCHEMA = new HTMLSchema();
    }

    public static Spanned fromRichText(String richText, IEmojiFactory iEmojiFactory) {
        return new RichTextConvertor(iEmojiFactory).convert(richText);
    }

    public Spanned convert(String richText) {
        mSource = richText;

        mParser = new Parser();
        try {
            mParser.setProperty(Parser.schemaProperty, HtmlParser.SCHEMA);
        } catch (SAXNotRecognizedException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        } catch (SAXNotSupportedException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        mResult = new SpannableStringBuilder();

        mParser.setContentHandler(this);
        try {
            mParser.parse(new InputSource(new StringReader(mSource)));
        } catch (IOException e) {
            // We are reading from a string. There should not be IO problems.
            throw new RuntimeException(e);
        } catch (SAXException e) {
            // TagSoup doesn't throw parse exceptions.
            throw new RuntimeException(e);
        }

        // replace all TemporarySpans by the "real" spans
        for (TemporarySpan span : mResult.getSpans(0, mResult.length(), TemporarySpan.class)) {
            span.swapIn(mResult);
        }

        return mResult;
    }

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        handleStartTag(localName, atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        handleEndTag(localName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = ch[i + start];
            sb.append(c);
        }

        mResult.append(sb);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    // ****************************************** Handle Tags *******************************************

    private void handleStartTag(String tag, Attributes attributes) {
        if (tag.equalsIgnoreCase("a")) {
            startAHref(attributes);
        } else if (tag.equalsIgnoreCase("img")) {
            startImg(attributes);
        } else if (tag.equalsIgnoreCase("b") || tag.equalsIgnoreCase("strong")) {
            start(new Bold());
        }
    }

    private void handleEndTag(String tag) {
        if (tag.equalsIgnoreCase("a")) {
            endAHref();
        } else if (tag.equalsIgnoreCase("b")|| tag.equalsIgnoreCase("strong")) {
            end(Bold.class, new BoldSpan());
        }
    }

    private void startAHref(Attributes attributes) {
        String href = attributes.getValue("", "href");
        int len = mResult.length();
        mResult.setSpan(new Href(href), len, len, Spanned.SPAN_MARK_MARK);
    }

    private void start(Object mark) {
        int len = mResult.length();
        mResult.setSpan(mark, len, len, Spanned.SPAN_MARK_MARK);
    }

    private void endAHref() {
        int len = mResult.length();
        Object obj = getLast(Href.class);
        int where = mResult.getSpanStart(obj);

        mResult.removeSpan(obj);

        if (where != len) {
            Href h = (Href) obj;
            if (h.mHref != null) {
                mResult.setSpan(new UrlSpan(h.mHref),
                        where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void end(Class<? extends Object> kind, Object repl) {
        int len = mResult.length();
        Object obj = getLast(kind);
        int where = mResult.getSpanStart(obj);

        mResult.removeSpan(obj);

        if (where != len) {
            // Note: use SPAN_EXCLUSIVE_EXCLUSIVE, the TemporarySpan will be replaced by a SPAN_EXCLUSIVE_INCLUSIVE span
            mResult.setSpan(new TemporarySpan(repl), where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private Object getLast(Class<? extends Object> kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        Object[] objs = mResult.getSpans(0, mResult.length(), kind);
        return objs.length == 0 ? null : objs[objs.length - 1];
    }

    private void startImg(Attributes attributes) {
        int len = mResult.length();
        String alt = attributes.getValue("", "alt");
        String src = attributes.getValue("", "src");
        String classString = attributes.getValue("", "class");

        if (!"yiqiFace".equals(classString)) {
            // image
            // Unicode Character 'OBJECT REPLACEMENT CHARACTER' (U+FFFC)
            // see http://www.fileformat.info/info/unicode/char/fffc/index.htm
            mResult.append("\uFFFC");
            FakeImageSpan imageSpan = new FakeImageSpan(src);
            mResult.setSpan(imageSpan, len, len + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            // emoji
            EmojiSpan emojiSpan = new EmojiSpan(ContextUtils.getContext(),
                    mIEmojiFactory.getEmoji(alt.substring(1, alt.length() - 1)));
            mResult.append(alt);
            mResult.setSpan(emojiSpan, len, len + alt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static class Bold {
    }
}
