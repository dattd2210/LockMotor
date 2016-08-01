package com.lockmotor.base.baseView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lockmotor.R;
import com.lockmotor.base.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VietHoa on 20/01/16.
 */
public abstract class BaseFragment extends Fragment implements BaseView {
    protected final String TAG = "BaseFragment";
    private Dialog mLoadingDialog;

    @Nullable
    @BindView(R.id.progress)
    View mProgressLoading;
    @Nullable
    @BindView(R.id.no_data_view)
    View mNoDataView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = setContentView(inflater, container);
        ButterKnife.bind(this, contentView);

        initialiseDialogLoading();

        return contentView;
    }

    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);

    //----------------------------------------------------------------------------------------------
    // Setup Dagger Injection
    //----------------------------------------------------------------------------------------------


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //----------------------------------------------------------------------------------------------
    // Animation Open Activity
    //----------------------------------------------------------------------------------------------

    protected void slideOutFromLeft(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.acitivity_in_from_left_to_right,
                R.anim.activity_out_from_right);
    }

    protected void slideInFromRight(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.acitivity_in_from_right_to_left,
                R.anim.acitivity_out_from_left);
    }

    protected void slideOutFromLeftZoom(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_zoom_in,
                R.anim.activity_out_from_right);
    }

    protected void slideInFromRightZoom(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.acitivity_in_from_right_to_left,
                R.anim.activity_zoom_out);
    }

    protected void slideInFromLeftZoom(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.acitivity_in_from_left_to_right,
                R.anim.activity_zoom_out);
    }

    //----------------------------------------------------------------------------------------------
    // No data view
    //----------------------------------------------------------------------------------------------

    @Override
    public void showNoDataView() {
        if (mNoDataView == null)
            return;
        mNoDataView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoDataView() {
        if (mNoDataView == null)
            return;
        mNoDataView.setVisibility(View.GONE);
    }

    //----------------------------------------------------------------------------------------------
    // Loading progress
    //----------------------------------------------------------------------------------------------

    @Override
    public void showLoadingProgress() {
        if (mProgressLoading == null)
            return;
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingProgress() {
        if (mProgressLoading == null)
            return;
        mProgressLoading.setVisibility(View.GONE);
    }

    //----------------------------------------------------------------------------------------------
    // Loading Dialog Helpers
    //----------------------------------------------------------------------------------------------

    protected void initialiseDialogLoading() {
        //mLoadingDialog = new Dialog(getActivity(), R.style.CustomDialogLoading);
        //View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_progress_wheel_layout, null);
        //mLoadingDialog.setContentView(v);
        mLoadingDialog.setCancelable(false);
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            //View loadingContent = mLoadingDialog.findViewById(R.id.loading_progress_container);
            //ImageView imgLoading = (ImageView) loadingContent.findViewById(R.id.iv_loading);
            //AnimationUtils.AnimationWheelInfinity(getActivity(), imgLoading);
            mLoadingDialog.show();
        }
    }

    @Override
    public boolean isShowLoadingDialog() {
        if (mLoadingDialog == null)
            return false;
        return mLoadingDialog.isShowing();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            try {
                mLoadingDialog.dismiss();
            } catch (Exception e) {
                // dismiss dialog after destroy activity
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // Error & Logging Helpers
    //----------------------------------------------------------------------------------------------

    @Override
    public void logError(String message) {
        Log.e(TAG, message);
    }

    @Override
    public void logDebug(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void logWarning(String message) {
        Log.w(TAG, message);
    }

    @Override
    public void logInfo(String message) {
        Log.i(TAG, message);
    }

    @Override
    public void showToastMessage(String message) {
        ToastUtils.showToastMessageWithSuperToast(message);
    }

    @Override
    public void showToastMessageDelay(String message, int timeDelay) {
        ToastUtils.showToastMessageWithSuperToastDelay(message, timeDelay);
    }

    @Override
    public void showToastErrorMessage(String message) {
        ToastUtils.showErrorMessageWithSuperToast(message, TAG);
    }
}
