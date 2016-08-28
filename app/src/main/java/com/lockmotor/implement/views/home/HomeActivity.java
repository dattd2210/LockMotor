package com.lockmotor.implement.views.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;
import com.lockmotor.global.GlobalConstant;
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.implement.LockMotorActivity;
import com.lockmotor.implement.viewModels.HomeViewModel;
import com.lockmotor.implement.views.setting.SettingActivity;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public class HomeActivity extends LockMotorActivity implements ConfigDialog.EventHandler {

    //Environment variables
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SmsManager smsManager;

    //Init View
    @BindView(R.id.btn_turn_off_engine1)
    Button btn_turn_off_engine1;
    @BindView(R.id.btn_turn_off_engine2)
    Button btn_turn_off_engine2;
    @BindView(R.id.iv_turn_off_engine)
    ImageView iv_turn_off_engine;

    @BindView(R.id.btn_find_location1)
    Button btn_find_location1;
    @BindView(R.id.btn_find_location2)
    Button btn_find_location2;
    @BindView(R.id.iv_find_location)
    ImageView iv_find_location;

    @BindView(R.id.btn_find_my_bike1)
    Button btn_find_my_bike1;
    @BindView(R.id.btn_find_my_bike2)
    Button btn_find_my_bike2;
    @BindView(R.id.iv_find_my_bike)
    ImageView iv_find_my_bike;

    @BindView(R.id.btn_anti_thief1)
    Button btn_anti_thief1;
    @BindView(R.id.btn_anti_thief2)
    Button btn_anti_thief2;
    @BindView(R.id.iv_anti_thief)
    ImageView iv_anti_thief;
    @BindView(R.id.iv_anti_thief_sub)
    ImageView iv_anti_thief_sub;
    @BindView(R.id.tv_anti_thief)
    TextView tv_anti_thief;

    @BindView(R.id.iv_setting_press)
    ImageView iv_setting;

    //Local variables
    private HomeViewModel viewModel;
    private ConfigDialog configDialog;

    final private CompositeSubscription subscriptions = new CompositeSubscription();
    private boolean isTurnOnAntiThief = true;
    private boolean isConfigDone = false;


    @Override
    protected void injectComponent(DIComponent component) {
        component.inject(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new HomeViewModel(this);
        viewModel.setSmsManager(smsManager);

        //Check for first set up
        setupConfigInfo();

        //set up rx java event
        initSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
        subscriptions.unsubscribe();
        configDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private void showConfigDialog() {
        configDialog = new ConfigDialog(this);
        configDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        configDialog.setContentView(R.layout.dialog_config);
        configDialog.setListener(this);
        configDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Prevent dialog close on back press button
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        configDialog.show();
    }

    private void setupConfigInfo() {

        //Config device phone number
        if (sharedPreferences.getString(GlobalConstant.DEVICE_PHONE_NUMBER_KEY, "").equals("")) {
            showConfigDialog();
        } else {
            GlobalConstant.DEVICE_PHONE_NUMBER =
                    sharedPreferences.getString(GlobalConstant.DEVICE_PHONE_NUMBER_KEY, "");
            GlobalConstant.PASSWORD =
                    GlobalConstant.decryptPassword(sharedPreferences.getString(GlobalConstant.PASSWORD_KEY, ""),
                            sharedPreferences.getInt(GlobalConstant.PASSWORD_LENGTH_KEY, 0));
            isConfigDone = true;
        }

    }

    private void initSubscription() {
        //Button turn off engine cheat
        subscriptions.add(RxView.touches(btn_turn_off_engine1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
            }
        }));

        subscriptions.add(RxView.touches(btn_turn_off_engine2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
            }
        }));


        //Button find location cheat
        subscriptions.add(RxView.touches(btn_find_location1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_location, motionEvent);
            }
        }));

        subscriptions.add(RxView.touches(btn_find_location2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_location, motionEvent);
            }
        }));


        //Button find my bike cheat
        subscriptions.add(RxView.touches(btn_find_my_bike1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
            }
        }));

        subscriptions.add(RxView.touches(btn_find_my_bike2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
            }
        }));


        //Button anti thief cheat
        subscriptions.add(RxView.touches(btn_anti_thief1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                isTurnOnAntiThief = viewModel.updateAntiThiefView(iv_anti_thief, iv_anti_thief_sub,
                        tv_anti_thief, motionEvent, isTurnOnAntiThief);
            }
        }));

        subscriptions.add(RxView.touches(btn_anti_thief2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                isTurnOnAntiThief = viewModel.updateAntiThiefView(iv_anti_thief, iv_anti_thief_sub,
                        tv_anti_thief, motionEvent, isTurnOnAntiThief);
            }
        }));


        subscriptions.add(RxView.clicks(iv_setting).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        }));
    }

    //----------------------------------------------------------------------------------------------
    //Event for config device number dialog
    //----------------------------------------------------------------------------------------------
    @Override
    public void btnQuitClicked(@NonNull View view) {
        this.finish();
    }

    @Override
    public void btnDoneClicked(@NonNull View view) {
        //TODO send sms to device
        sharedPreferences.edit().putString(GlobalConstant.DEVICE_PHONE_NUMBER_KEY,
                configDialog.getDeviceNumber()).apply();
        sharedPreferences.edit().putString(GlobalConstant.PASSWORD_KEY,
                GlobalConstant.encryptPassword(configDialog.getPassword())).apply();
        sharedPreferences.edit().putInt(GlobalConstant.PASSWORD_LENGTH_KEY,
                configDialog.getPassword().length()).apply();
        configDialog.hide();
        configDialog.dismiss();
    }
}
