package com.liye.richedittextdemo.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liye.richedittextdemo.R;
import com.liye.richedittextdemo.richedittext.Emoji;
import com.liye.richedittextdemo.richedittext.RichEditText;
import com.liye.richedittextdemo.richedittext.RichEditTextListener;
import com.liye.richedittextdemo.richedittext.span.FakeImageSpan;
import com.liye.richedittextdemo.richedittext.span.ImageSpan;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/24 下午4:41
 */
public abstract class RichEditTextActivity extends AppCompatActivity {

    private static final String TAG = RichEditTextActivity.class.getSimpleName();

    public final static String HTML_TAG = "[REPLACE THIS WITH YOUR TAG URL]";

    public final static String HTML_AT = "[REPLACE THIS WITH YOUR AT URL]";

    protected RichEditText mEditTextInput;
    private TextView mIconAddPicture;
    private TextView mIconAddTag;
    private TextView mIconApplyBold;
    private TextView mIconAddEmoji;
    private TextView mIconAddAt;
    private View mLayoutEmojiContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_edit_text);
        mEditTextInput = (RichEditText) findViewById(R.id.edit_text_input);
        mIconAddPicture = (TextView) findViewById(R.id.icon_text_add_picture);
        mIconAddTag = (TextView) findViewById(R.id.icon_text_add_tag);
        mIconApplyBold = (TextView) findViewById(R.id.icon_text_bold);
        mIconAddEmoji = (TextView) findViewById(R.id.icon_text_emoji);
        mIconAddAt = (TextView) findViewById(R.id.icon_text_add_at);
        mLayoutEmojiContainer = findViewById(R.id.emoji_container);

        mIconApplyBold.setOnClickListener(mOnClickListener);
        mIconAddPicture.setOnClickListener(mOnClickListener);
        mIconAddPicture.setOnClickListener(mOnClickListener);
        mIconAddTag.setOnClickListener(mOnClickListener);
        mIconAddEmoji.setOnClickListener(mOnClickListener);
        mEditTextInput.setOnClickListener(mOnClickListener);
        mIconAddAt.setOnClickListener(mOnClickListener);

//        getActionBarController().setButtonRightText(getString(R.string.publish));
//        getActionBarController().setOnClickRightListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                publish();
//            }
//        });
//        EmojiPanelHelper.initEmojiPanel(this, mEmojiPager, mLayoutEmojiPageControl, mEmojiClickListener);
        mEditTextInput.setRichEditTextListener(mRichEditTextListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 收起键盘
        toggleSoftInput(false);
    }

    protected void toggleSoftInput(boolean showOrHide) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (showOrHide) {
            imm.showSoftInput(mEditTextInput, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(mEditTextInput.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
//            case REQUEST_CODE_SELECT_PHOTO:
//                ArrayList<String> arrayList = data.getStringArrayListExtra(BundleConstants.KEY_DATA);
//                bindImage(arrayList);
//                break;
//            case REQUEST_CODE_SELECT_TAG:
//                bindTag(data.getStringExtra(BundleConstants.KEY_GROUP_TAG));
//                break;
//            case REQUEST_CODE_TYPE_IN_TAG:
//                // 先去掉主动输入的#
//                mEditTextInput.getText().delete(mEditTextInput.getSelectionStart() - 1,
//                        mEditTextInput.getSelectionStart());
//                bindTag(data.getStringExtra(BundleConstants.KEY_GROUP_TAG));
//                break;
//            case REQUEST_CODE_SELECT_LOCATION:
//                setLocationInfo((PoiInfo) data.getSerializableExtra(BundleConstants.KEY_LOCATION));
//                break;
//            case REQUEST_CODE_SELECT_AT_MEMBER:
//                addAtMemberLink(data.getLongExtra(BundleConstants.KEY_USER_ID, 0),
//                        "@" + data.getStringExtra(BundleConstants.KEY_USER_NAME));
//                break;
//            case REQUEST_CODE_TYPE_IN_AT_MEMBER:
//                addAtMemberLink(data.getLongExtra(BundleConstants.KEY_USER_ID, 0),
//                        data.getStringExtra(BundleConstants.KEY_USER_NAME));
//                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addAtMemberLink(long userId, String userName) {
        mEditTextInput.addLink(userName, HTML_AT);
    }

    private void addLocalImage() {
        // TODO: start select image activity
    }

    private void addTag() {
        // TODO: start select tag activity
    }

    private void addAt() {
        // TODO: start add at activity
    }

    private void applyBold(boolean bold) {
        mIconApplyBold.setSelected(bold);
        mEditTextInput.applyBoldEffect(bold);
    }

    private void showEmoji(boolean show) {
        if (show == mIconAddEmoji.isSelected()) {
            return;
        }
        toggleSoftInput(!show);
        mIconAddEmoji.setSelected(show);
        mLayoutEmojiContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void setRichTextContent(String richText) {
        mEditTextInput.setRichText(richText);
        FakeImageSpan[] imageSpans = mEditTextInput.getFakeImageSpans();
        if (imageSpans == null || imageSpans.length == 0) {
            return;
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (final FakeImageSpan imageSpan : imageSpans) {
            final String src = imageSpan.getValue();
            if (src.startsWith("http")) {
                // web images
                new AsyncTask<String, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(String... params) {
                        try {
                            InputStream is = new URL(src).openStream();
                            return BitmapFactory.decodeStream(is);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        mEditTextInput.replaceDownloadedImage(imageSpan, bitmap, src);
                    }
                }.executeOnExecutor(executorService, src);
            } else {
                // local images
                mEditTextInput.replaceLocalImage(imageSpan, src);
            }
        }
    }

    protected void bindImage(ArrayList<String> arrayList) {
        for (String path : arrayList) {
            mEditTextInput.addImage(path);
        }
    }

    private void bindTag(String tag) {
        String groupTag = tag;
        try {
            groupTag = URLEncoder.encode(tag, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mEditTextInput.addLink(String.format("#%s#", groupTag), HTML_TAG);
    }

    protected void finishEdit() {
        finishEdit(mEditTextInput.getRichText());
    }

    protected abstract void finishEdit(String content);

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mIconAddPicture) {
                addLocalImage();
            } else if (v == mIconAddTag) {
                addTag();
            } else if (v == mIconApplyBold) {
                applyBold(!mIconApplyBold.isSelected());
            } else if (v == mIconAddEmoji) {
                showEmoji(!mIconAddEmoji.isSelected());
            } else if (v == mEditTextInput) {
                showEmoji(false);
            } else if (v == mIconAddAt) {
                addAt();
            }
        }
    };

    private RichEditTextListener mRichEditTextListener = new RichEditTextListener() {
        @Override
        public void onSelectionChanged(int start, int end) {
            mIconApplyBold.setSelected(mEditTextInput.getBoldEffectValue());
        }
    };

//    private EmojiPagerAdapter.OnItemClickListener mEmojiClickListener = new EmojiPagerAdapter.OnItemClickListener() {
//        @Override
//        public void onItemClick(Emoji emoji) {
//            mEditTextInput.addEmoji(emoji);
//        }
//
//        @Override
//        public void onDeleteClick() {
//            mEditTextInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
//        }
//    };
}
