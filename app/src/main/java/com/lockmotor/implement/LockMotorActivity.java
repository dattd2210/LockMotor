package com.lockmotor.implement;

import android.os.Bundle;

import com.lockmotor.base.baseView.BaseActivity;
import com.lockmotor.global.dagger.AppModule;
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.global.dagger.DaggerDIComponent;
import com.lockmotor.global.dagger.NetworkModule;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public class LockMotorActivity extends BaseActivity {

    protected DIComponent component;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((LockMotorApplication)getApplication()).getDiComponent().inject(this);
        component = DaggerDIComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    protected DIComponent getComponent()
    {
        return component;
    }

    @Override
    protected void setContentView() {

    }
}
