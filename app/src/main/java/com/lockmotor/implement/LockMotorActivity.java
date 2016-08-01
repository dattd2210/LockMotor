package com.lockmotor.implement;

import android.os.Bundle;

import com.lockmotor.base.baseView.BaseActivity;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public class LockMotorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((LockMotorApplication)getApplication()).getDiComponent().inject(this);
    }

    @Override
    protected void setContentView() {

    }
}
