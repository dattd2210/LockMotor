package com.lockmotor.Components.modules;

import android.content.Context;

import com.lockmotor.BaseAppication.base.BaseView;
import com.lockmotor.BaseAppication.utils.StringUtil.StringUtilImpl;
import com.lockmotor.BaseAppication.utils.StringUtil.StringUtils;
import com.lockmotor.Components.managers.CacheManager.CacheManager;
import com.lockmotor.Components.managers.CacheManager.CacheManagerImpl;
import com.lockmotor.Components.managers.UserSessionManager.UserSessionManager;
import com.lockmotor.Components.managers.UserSessionManager.UserSessionManagerImpl;
import com.lockmotor.Components.modules.Network.NetworkModule;
import com.lockmotor.Features.Views.Home.HomeActivity;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by VietHoa on 03/05/15.
 */
@Module(
        library = true,
        complete = true,
        injects = {
                HomeActivity.class,
        },
        includes = {
                NetworkModule.class,
        }
)
public class AppModule {
    private Context context;
    private Object viewImpl;

    public AppModule(){

    }

    public AppModule(Context activity) {
        this.context = activity;
    }

    public AppModule(Context context, Object viewImpl) {
        this.context = context;
        this.viewImpl = viewImpl;
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
    Object provideViewImpl() {
        return viewImpl;
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
    UserSessionManager provideUserSessionManager(UserSessionManagerImpl userSessionManager) {
        return userSessionManager;
    }

    @Provides
    @Singleton
    Realm provideRealm(Context context) {
        return Realm.getInstance(context);
    }
}

