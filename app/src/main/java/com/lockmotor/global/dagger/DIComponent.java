package com.lockmotor.global.dagger;

import com.lockmotor.implement.views.home.HomeActivity;
import com.lockmotor.implement.views.setting.SettingActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by trandinhdat on 7/26/16.
 */

@Singleton
@Component(modules = {NetworkModule.class, AppModule.class})
public interface DIComponent {
    void inject(HomeActivity activity);

    void inject(SettingActivity activity);
}
