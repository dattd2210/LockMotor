package com.lockmotor.base.baseView;

/**
 * Created by VietHoa on 19/01/16.
 */
public interface BaseView {

    void showNoDataView();

    void hideNoDataView();

    void showLoadingDialog();

    boolean isShowLoadingDialog();

    void dismissLoadingDialog();

    void showLoadingProgress();

    void dismissLoadingProgress();

    void logError(String message);

    void logDebug(String message);

    void logWarning(String message);

    void logInfo(String message);

    void showToastMessage(String message);

    void showToastMessageDelay(String message, int timeDelay);

    void showToastErrorMessage(String message);

    void showConfirmDialog(boolean isSuccess);
}
