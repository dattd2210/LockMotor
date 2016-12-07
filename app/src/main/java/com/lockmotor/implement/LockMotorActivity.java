package com.lockmotor.implement;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.lockmotor.R;
import com.lockmotor.base.baseView.BaseActivity;
import com.lockmotor.global.dagger.DIComponent;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public abstract class LockMotorActivity extends BaseActivity {
    private Dialog loadingFingerSetupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectComponent(((LockMotorApplication) getApplication()).getDiComponent());
        initialiseFingerSetupLoadingDialog();
    }

    protected abstract void injectComponent(DIComponent component);

    //----------------------------------------------------------------------------------------------
    // Loading UI Helpers
    //----------------------------------------------------------------------------------------------

    protected void initialiseFingerSetupLoadingDialog() {
        loadingFingerSetupDialog = new Dialog(this, R.style.CustomDialogLoading);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_loading_finger, null);
        loadingFingerSetupDialog.setContentView(v);
        loadingFingerSetupDialog.setCancelable(false);
    }

    public void showFingerSetupLoadingDialog() {
        if (loadingFingerSetupDialog != null && !loadingFingerSetupDialog.isShowing()) {
            loadingFingerSetupDialog.show();
        }
    }

    public boolean isShowFingerSetupLoadingDialog() {
        if (loadingFingerSetupDialog == null)
            return false;
        return loadingFingerSetupDialog.isShowing();
    }

    public void dismissFingerSetupLoadingDialog() {
        if (loadingFingerSetupDialog != null) {
            loadingFingerSetupDialog.dismiss();
        }
    }
}
