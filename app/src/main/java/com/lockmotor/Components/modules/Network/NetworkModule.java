package com.lockmotor.Components.modules.Network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lockmotor.BuildConfig;
import com.lockmotor.Components.network.Config;
import com.lockmotor.Components.network.JsonHelper.JsonHelper;
import com.lockmotor.Components.network.JsonHelper.JsonHelperImpl;
import com.lockmotor.Components.network.TankFindService;
import com.lockmotor.Components.network.base.BaseErrorHandler;
import com.lockmotor.Components.network.base.BaseRequestInterceptor;
import com.lockmotor.Components.network.base.DateConverter;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by VietHoa on 17/01/16.
 */
@Module(
        library = true,
        complete = false
)
public class NetworkModule {

    @Provides
    @Singleton
    Gson provideGsonParser() {
        Gson gson = new GsonBuilder()

                // For Realm Local DataBase
//                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>(){}.getType(), new RealmConverter())
//                .setExclusionStrategies(new ExclusionStrategy() {
//                    @Override
//                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getDeclaringClass().equals(RealmObject.class);
//                    }
//
//                    @Override
//                    public boolean shouldSkipClass(Class<?> clazz) {
//                        return false;
//                    }
//                })

                // For Date converter
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();
        return gson;
    }

    @Provides
    @Singleton
    JsonHelper provideJsonHelper(Context context, JsonHelperImpl jsonHelper) {
        jsonHelper.setThreadBeRunOn(context);
        return jsonHelper;
    }

    @Provides
    @Singleton
    ErrorHandler provideErrorHandler() {
        return new BaseErrorHandler();
    }

    @Provides
    @Singleton
    RequestInterceptor provideRequestInterceptor(BaseRequestInterceptor requestInterceptor) {
        return requestInterceptor;
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(ErrorHandler errorHandler, RequestInterceptor requestInterceptor, Gson gson) {
        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder();
        restAdapterBuilder.setConverter(new GsonConverter(gson));
        restAdapterBuilder.setErrorHandler(errorHandler);
        restAdapterBuilder.setEndpoint(Config.getBaseUrl());
        restAdapterBuilder.setRequestInterceptor(requestInterceptor);

        //Full log level on development mode
        RestAdapter adapter = restAdapterBuilder.build();
        if (BuildConfig.DEBUG) {
            adapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        return adapter;
    }

    @Provides
    @Singleton
    TankFindService provideGenesisService(RestAdapter restAdapter) {
        return restAdapter.create(TankFindService.class);
    }
}
