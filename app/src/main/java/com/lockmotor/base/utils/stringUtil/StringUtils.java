package com.lockmotor.base.utils.stringUtil;

import android.content.Context;

import java.util.AbstractCollection;

/**
 * Created by VietHoa on 17/01/16.
 */
public interface StringUtils {

    boolean isNotNull(String value);

    boolean isNull(String value);

    boolean isHttp(String value);

    boolean isValidEmail(String value);

    String capitalize(String value);

    String encodeUrl(String value);

    int convertToInt(String value, int defaultValue);

    void copyToClipboard(Context context, String value);

    String replaceKeyword(String value, AbstractCollection<String> keywords);
}
