package com.lockmotor.global.dagger;

import android.content.Context;

import com.lockmotor.base.baseView.BaseActivity;
import com.lockmotor.base.baseView.BaseView;
import com.lockmotor.base.managers.cacheManager.CacheManager;
import com.lockmotor.base.utils.stringUtil.StringUtils;
import com.lockmotor.global.LockMotorAPI;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by trandinhdat on 7/26/16.
 */

@Singleton
@Component(modules = {NetworkModule.class, AppModule.class})
public interface DIComponent {
    BaseView getBaseView();
    Context getContext();
    StringUtils getStringUtils();
    CacheManager getCacheManager();
    Realm getRealm();

    Retrofit getRetrofit();
    LockMotorAPI getLockMotorAPI();

    void inject(BaseActivity activity);
}
