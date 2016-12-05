package com.lockmotor.base.baseView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lockmotor.R;
import com.lockmotor.base.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected final String LOG_TAG = "BaseActivity";
    private Dialog mLoadingDialog;
    private Dialog confirmDialog;

    @Nullable
    @BindView(R.id.progress)
    View mProgressLoading;
    @Nullable
    @BindView(R.id.no_data_view)
    View mNoDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ButterKnife.bind(this);
        initialiseDialogLoading();
    }

    protected abstract void setContentView();

    @Override
    protected void onPause() {
        dismissLoadingDialog();
        super.onPause();
    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected  void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //----------------------------------------------------------------------------------------------
    // Animation Open Activity
    //----------------------------------------------------------------------------------------------

    protected void slideOutFromLeft(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.acitivity_in_from_left_to_right,
                R.anim.activity_out_from_right);
    }

    protected void slideInFromRight(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.acitivity_in_from_right_to_left,
                R.anim.acitivity_out_from_left);
    }

    protected void slideOutFromLeftZoom(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.activity_zoom_in,
                R.anim.activity_out_from_right);
    }

    protected void slideInFromRightZoom(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.acitivity_in_from_right_to_left,
                R.anim.activity_zoom_out);
    }

    protected void slideInFromLeftZoom(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.acitivity_in_from_left_to_right,
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
    // Loading UI Helpers
    //----------------------------------------------------------------------------------------------

    protected void initialiseDialogLoading() {
        mLoadingDialog = new Dialog(this, R.style.CustomDialogLoading);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_progress_wheel_layout, null);
        mLoadingDialog.setContentView(v);
        mLoadingDialog.setCancelable(false);

        confirmDialog = new Dialog(this,R.style.CustomDialogLoading);
        View confirmView = LayoutInflater.from(this).inflate(R.layout.dialog_confirmation,null);
        confirmDialog.setContentView(confirmView);
        confirmDialog.setCancelable(false);
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            View loadingContent = mLoadingDialog.findViewById(R.id.loading_progress_container);
            mLoadingDialog.show();
        }
    }

    @Override
    public void showConfirmDialog(boolean isSuccess) {
        if (confirmDialog != null) {
            View confirmContent = confirmDialog.findViewById(R.id.confirm_container);
            ImageView iv_confirm_icon = (ImageView)((ViewGroup)confirmContent).getChildAt(0);
            if(isSuccess) {
                iv_confirm_icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_success));
            }else {
                iv_confirm_icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_error));
            }
            confirmDialog.show();
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    confirmDialog.hide();
                }
            },3000);
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
        Log.e(LOG_TAG, message);
    }

    @Override
    public void logDebug(String message) {
        Log.d(LOG_TAG, message);
    }

    @Override
    public void logWarning(String message) {
        Log.w(LOG_TAG, message);
    }

    @Override
    public void logInfo(String message) {
        Log.i(LOG_TAG, message);
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
        ToastUtils.showErrorMessageWithSuperToast(message, LOG_TAG);
    }

    //----------------------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------------------

    protected void replaceFragment(final Fragment fg, final int containerResId, final boolean animated) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        if (animated) {
            tx.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        }

        tx.replace(containerResId, fg);
        //tx.addToBackStack(LOG_TAG);
        tx.commit();
        fm.executePendingTransactions();
    }

    protected void openUrlInBrowser(String url) {
        if (url == null || url.length() <= 0)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse(url));
        } catch (Exception e) {
            return;
        }
        startActivity(intent);
    }
}