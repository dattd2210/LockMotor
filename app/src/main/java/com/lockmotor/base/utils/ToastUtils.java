package com.lockmotor.base.utils;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.lockmotor.base.BaseApplication;

public final class ToastUtils {

    public static void showToastMessageWithSuperToast(String message) {
        if (message == null || message.isEmpty())
            return;
        SuperToast.create(BaseApplication.getAppContext(), message, SuperToast.Duration.MEDIUM, Style.getStyle(Style.GREEN, SuperToast.Animations.POPUP)).show();
    }

    public static void showToastMessageWithSuperToastDelay(String message, int timeDuration) {
        if (message == null || message.isEmpty())
            return;
        SuperToast.create(BaseApplication.getAppContext(), message, timeDuration, Style.getStyle(Style.GREEN, SuperToast.Animations.POPUP)).show();
    }

    public static void showErrorMessageWithSuperToast(String message, String logTag) {
        if (message == null || message.isEmpty())
            return;

        SuperToast.create(BaseApplication.getAppContext(), message, SuperToast.Duration.SHORT, Style.getStyle(Style.RED, SuperToast.Animations.POPUP)).show();
    }
}
