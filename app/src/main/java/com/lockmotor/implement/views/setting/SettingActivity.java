package com.lockmotor.implement.views.setting;

import com.lockmotor.R;
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.implement.LockMotorActivity;

/**
 * Created by trandinhdat on 8/13/16.
 */
public class SettingActivity extends LockMotorActivity{

    @Override
    protected void injectComponent(DIComponent component) {
        component.inject(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
    }
}
