package com.lockmotor.BaseAppication.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lockmotor.BaseAppication.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.mime.TypedFile;

/**
 * Created by VietHoa on 12/01/16.
 */
public class Utils {
    private static final String TAG = "Utils";

    /**
     * RetroFix support
     *
     * Convert bitmap to TypeFile
     */
    public static TypedFile convertBitmapToTypedFile(Context context, Bitmap bitmap) {
        if (bitmap == null)
            return null;

        File imageFileFolder = new File(FileUtils.getInternalStorageFileDir(context), "photo");
        if (!imageFileFolder.exists()) {
            imageFileFolder.mkdirs();
        }

        FileOutputStream out = null;
        File imageFileName = new File(imageFileFolder, "frontImage.jpg");

        try {
            out = new FileOutputStream(imageFileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException ex) {
            Log.d("IOException", ex.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.d("IOException", e.getMessage());
            }
        }

        return new TypedFile("image/jpeg", imageFileName);
    }

    /**
     * Android Support TextView
     *
     * Make link clickable inside TextView
     */
    public static TextView makeClickableTextView(TextView textView) {
        Log.i(TAG, "makeClickableTextView with text = " + textView.getText());
        textView.setLinksClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence charSequence = textView.getText();
        SpannableStringBuilder sp = new SpannableStringBuilder(charSequence);
        URLSpan[] spans = sp.getSpans(0, charSequence.length(), URLSpan.class);
        for (URLSpan urlSpan : spans) {
            Log.d(TAG, "urlSpan.getURL() = " + urlSpan.getURL());
            MySpan mySpan = new MySpan(urlSpan.getURL());
            sp.setSpan(mySpan, sp.getSpanStart(urlSpan),
                    sp.getSpanEnd(urlSpan), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        textView.setText(sp);

        return textView;
    }

    private static class MySpan extends ClickableSpan {
        private String mUrl;

        public MySpan(String url) {
            super();
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            if (!mUrl.startsWith("http://") && !mUrl.startsWith("https://"))
                mUrl = "http://" + mUrl;
            Log.i(TAG, "Open up URL link from app with url = " + mUrl);

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.getAppContext().startActivity(browserIntent);
        }
    }
}
