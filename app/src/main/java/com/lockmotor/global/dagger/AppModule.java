package com.lockmotor.global.dagger;

import android.content.Context;

import com.lockmotor.base.baseView.BaseView;
import com.lockmotor.base.managers.cacheManager.CacheManager;
import com.lockmotor.base.managers.cacheManager.CacheManagerImpl;
import com.lockmotor.base.utils.stringUtil.StringUtilImpl;
import com.lockmotor.base.utils.stringUtil.StringUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by VietHoa on 03/05/15.
 */
@Module
public class AppModule {
    private Context context;

    public AppModule(){

    }

    public AppModule(Context activity) {
        this.context = activity;
    }

    @Provides
    @Singleton
    BaseView provideBaseView() {
        return (BaseView) context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    StringUtils provideStringUtils(StringUtilImpl stringUtil) {
        return stringUtil;
    }

    @Provides
    @Singleton
    CacheManager provideCacheManager(CacheManagerImpl cacheManager) {
        return cacheManager;
    }

    @Provides
    @Singleton
    Realm provideRealm(Context context) {
        return Realm.getInstance(context);
    }
}

