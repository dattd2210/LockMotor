package com.lockmotor.implement;

import com.lockmotor.base.BaseApplication;
import com.lockmotor.global.dagger.AppModule;
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.global.dagger.DaggerDIComponent;
import com.lockmotor.global.dagger.NetworkModule;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public class LockMotorApplication extends BaseApplication {

    private DIComponent diComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        diComponent = DaggerDIComponent.builder()
                .networkModule(new NetworkModule())
                .appModule(new AppModule())
                .build();
    }

    public DIComponent getDiComponent() {
        return diComponent;
    }
}
