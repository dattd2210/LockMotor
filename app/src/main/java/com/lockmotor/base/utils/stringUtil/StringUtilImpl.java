package com.lockmotor.base.utils.stringUtil;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.net.URI;
import java.net.URL;
import java.util.AbstractCollection;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by VietHoa on 17/01/16.
 */
public class StringUtilImpl implements StringUtils {

    @Inject
    public StringUtilImpl() {

    }

    @Override
    public boolean isNotNull(String string) {
        if (string == null || string.trim().isEmpty()
                || "null".equals(string.toLowerCase(Locale.US))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isNull(String string) {
        if (string == null || string.trim().isEmpty()
                || "null".equals(string.toLowerCase(Locale.US))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isHttp(String string) {
        if (string == null || string.trim().length() < 10
                || "null".equals(string.toLowerCase(Locale.US))) {
            return false;
        } else {
            return string.startsWith("http");
        }
    }

    @Override
    public boolean isValidEmail(String string) {
        if (string == null || string.length() <= 0 || !string.contains("@"))
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    @Override
    @SuppressLint("DefaultLocale")
    public String capitalize(String s) {
        if (s == null)
            return null;
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    @Override
    public String encodeUrl(final String originalUrl) {
        String encodedUrl = originalUrl;
        try {
            URL url = new URL(originalUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            encodedUrl = uri.toURL().toString();
        } catch (Exception e) {

        }
        return encodedUrl;
    }

    @Override
    public int convertToInt(String string, int defaultValue) {
        if (string == null || string.trim().isEmpty() || "null".equals(string.toLowerCase(Locale.US)))
            return 0;

        int intValue;
        try {
            intValue = Integer.parseInt(string);
        } catch (Exception e) {
            intValue = defaultValue;
        }
        return intValue;
    }

    @Override
    public void copyToClipboard(Context context, String string) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", string);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public String replaceKeyword(String string, AbstractCollection<String> keywords) {
        if (isNull(string) || string.isEmpty())
            return "";

        for (String elem : keywords) {
            string = string.replace(elem, "");
        }

        return string;
    }
}
