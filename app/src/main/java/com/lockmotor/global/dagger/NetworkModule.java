package com.lockmotor.global.dagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lockmotor.base.baseNetwork.DateConverter;
import com.lockmotor.global.Config;
import com.lockmotor.global.LockMotorAPI;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VietHoa on 17/01/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    Gson provideGsonParser() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateConverter())
                .create();
        return gson;
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(60000,TimeUnit.MILLISECONDS);

        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson,OkHttpClient okHttpClient)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return  retrofit;
    }

    @Provides
    @Singleton
    LockMotorAPI provideLockMotorService(Retrofit retrofit) {
        return retrofit.create(LockMotorAPI.class);
    }
}
