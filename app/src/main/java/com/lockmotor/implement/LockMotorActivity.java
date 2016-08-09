package com.lockmotor.implement;

import android.os.Bundle;

import com.lockmotor.base.baseView.BaseActivity;
import com.lockmotor.global.dagger.DIComponent;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public abstract class LockMotorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectComponent(((LockMotorApplication)getApplication()).getDiComponent());
    }

    protected abstract void injectComponent(DIComponent component);
}
