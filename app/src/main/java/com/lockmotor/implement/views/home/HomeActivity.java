package com.lockmotor.implement.views.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;
import com.lockmotor.base.utils.DeviceUtils;
import com.lockmotor.global.GlobalConstant;
import com.lockmotor.global.LockMotorAPI;
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.implement.LockMotorActivity;
import com.lockmotor.implement.models.InfoRequest;
import com.lockmotor.implement.models.InfoResponse;
import com.lockmotor.implement.viewModels.HomeViewModel;
import com.lockmotor.implement.views.setting.PasswordInputDialog;
import com.lockmotor.implement.views.setting.SettingActivity;
import com.lockmotor.implement.views.vehicle_location.VehicleLocationActivity;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public class HomeActivity extends LockMotorActivity implements ConfigDialog.EventHandler, PasswordInputDialog.EventHandler {

    //Environment variables
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    LockMotorAPI service;

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

    //TODO delete
    @BindView(R.id.btn_delete)
    Button btn_delete;

    //Local variables
    private HomeViewModel viewModel;
    private ConfigDialog configDialog;
    private int count = 0;

    final private CompositeSubscription subscriptions = new CompositeSubscription();
    private boolean isTurnOnAntiThief = true;


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
        requestPermission();
    }

    private void requestPermission() {
        //Call phone permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.SEND_SMS},
                    GlobalConstant.REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length == 0) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
        subscriptions.unsubscribe();

        if (configDialog != null) {
            configDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set up rx java event
        initSubscription();

        //Check for first set up
        setupConfigInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscriptions.clear();
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private void showConfigDialog() {
        if (configDialog != null) {
            configDialog.show();
            return;
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        //config device number and password
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
        lp.copyFrom(configDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        configDialog.getWindow().setAttributes(lp);
        configDialog.setCanceledOnTouchOutside(false);
        configDialog.showSkipButton();

        if (!configDialog.isCanSkip()) {
            configDialog.showSkipButton();
        }

        configDialog.show();
    }

    private void setupConfigInfo() {

        //Config device phone number
        if (sharedPreferences.getString(GlobalConstant.DEVICE_PHONE_NUMBER_KEY, "").equals("")) {
            showConfigDialog();
        } else {
            GlobalConstant.DEVICE_PHONE_NUMBER =
                    sharedPreferences.getString(GlobalConstant.DEVICE_PHONE_NUMBER_KEY, "");
            GlobalConstant.PASSWORD = sharedPreferences.getString(GlobalConstant.PASSWORD_KEY, "");
            GlobalConstant.PASSWORD_LENGTH = sharedPreferences.getInt(GlobalConstant.PASSWORD_LENGTH_KEY, 0);
            GlobalConstant.PHONE_NUMBER = sharedPreferences.getString(GlobalConstant.PHONE_NUMBER_KEY, "");
            isTurnOnAntiThief = sharedPreferences.getBoolean(GlobalConstant.ANTI_THIEF_STATUS_KEY, true);
            viewModel.updateAntiThiefView(iv_anti_thief, iv_anti_thief_sub,
                    tv_anti_thief, !isTurnOnAntiThief);
        }

    }

    private void initSubscription() {
        subscriptions.clear();
        //Button turn off engine cheat
        subscriptions.add(RxView.touches(btn_turn_off_engine1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
                        viewModel.turnOffEngine(motionEvent);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
                }

            }
        }));

        subscriptions.add(RxView.touches(btn_turn_off_engine2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
                        viewModel.turnOffEngine(motionEvent);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
                }
            }
        }));


        //Button find location cheat
        subscriptions.add(RxView.touches(btn_find_location1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        viewModel.updateViewAfterClick(iv_find_location, motionEvent);
                        viewModel.findLocation(motionEvent);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewModel.updateViewAfterClick(iv_find_location, motionEvent);

                    Intent intent = new Intent(HomeActivity.this, VehicleLocationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.acitivity_in_from_right_to_left, R.anim.hold);
                }
            }
        }));

        subscriptions.add(RxView.touches(btn_find_location2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        viewModel.updateViewAfterClick(iv_find_location, motionEvent);
                        viewModel.findLocation(motionEvent);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewModel.updateViewAfterClick(iv_find_location, motionEvent);

                    Intent intent = new Intent(HomeActivity.this, VehicleLocationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.acitivity_in_from_right_to_left, R.anim.hold);
                }
            }
        }));


        //Button find my bike cheat
        subscriptions.add(RxView.touches(btn_find_my_bike1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
                        viewModel.findMyBike(motionEvent);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
                }
            }
        }));

        subscriptions.add(RxView.touches(btn_find_my_bike2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
                        viewModel.findMyBike(motionEvent);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
                }
            }
        }));


        //Button anti thief cheat
        subscriptions.add(RxView.touches(btn_anti_thief1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        isTurnOnAntiThief = viewModel.updateAntiThiefView(iv_anti_thief, iv_anti_thief_sub,
                                tv_anti_thief, isTurnOnAntiThief);
                        viewModel.toggleAntiThief(isTurnOnAntiThief);
                        sharedPreferences.edit().putBoolean(GlobalConstant.ANTI_THIEF_STATUS_KEY,
                                isTurnOnAntiThief).apply();
                        showLoadingDialog();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InfoRequest param = new InfoRequest(GlobalConstant.decryptPhoneNumber());
                                Call<InfoResponse> call = service.getInfo(param);
                                call.enqueue(new Callback<InfoResponse>() {
                                    @Override
                                    public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                                        if(!response.body().getStatus().equals("OK")){
                                            return;
                                        }

                                        if (!response.body().getMessage().equals("")
                                                && !response.body().getMessage().equals("null_dattd2210")) {
                                            handler.removeCallbacksAndMessages(null);
                                            dismissLoadingDialog();
                                            count = 0;
                                            showConfirmDialog(true);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<InfoResponse> call, Throwable t) {

                                    }
                                });
                                if (isReachMaxWaitingTime(false)) {
                                    handler.removeCallbacksAndMessages(null);
                                } else {
                                    handler.postDelayed(this, GlobalConstant.AUTO_CALL_API_TIME);
                                }
                            }
                        }, GlobalConstant.AUTO_CALL_API_TIME);
                    }
                }
            }
        }));

        subscriptions.add(RxView.touches(btn_anti_thief2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (checkConditionConfig()) {
                        isTurnOnAntiThief = viewModel.updateAntiThiefView(iv_anti_thief, iv_anti_thief_sub,
                                tv_anti_thief, isTurnOnAntiThief);
                        viewModel.toggleAntiThief(isTurnOnAntiThief);
                        sharedPreferences.edit().putBoolean(GlobalConstant.ANTI_THIEF_STATUS_KEY,
                                isTurnOnAntiThief).apply();
                        showLoadingDialog();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InfoRequest param = new InfoRequest(GlobalConstant.decryptPhoneNumber());
                                Call<InfoResponse> call = service.getInfo(param);
                                call.enqueue(new Callback<InfoResponse>() {
                                    @Override
                                    public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                                        if(!response.body().getStatus().equals("OK")){
                                            return;
                                        }

                                        if (!response.body().getMessage().equals("")
                                                && !response.body().getMessage().equals("null_dattd2210")) {
                                            handler.removeCallbacksAndMessages(null);
                                            dismissLoadingDialog();
                                            count = 0;
                                            showConfirmDialog(true);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<InfoResponse> call, Throwable t) {

                                    }
                                });
                                if (isReachMaxWaitingTime(false)) {
                                    handler.removeCallbacksAndMessages(null);
                                } else {
                                    handler.postDelayed(this, GlobalConstant.AUTO_CALL_API_TIME);
                                }
                            }
                        }, GlobalConstant.AUTO_CALL_API_TIME);
                    }
                }
            }
        }));


        subscriptions.add(RxView.clicks(iv_setting).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (!GlobalConstant.PASSWORD.equals("")) {
                    PasswordInputDialog passwordInputDialog;
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    passwordInputDialog = new PasswordInputDialog(HomeActivity.this, HomeActivity.this);
                    passwordInputDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    passwordInputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    passwordInputDialog.setContentView(R.layout.dialog_password_input);
                    lp.copyFrom(passwordInputDialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    passwordInputDialog.getWindow().setAttributes(lp);
                    passwordInputDialog.show();
                } else {
                    Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.acitivity_in_from_right_to_left, R.anim.hold);
                }
            }
        }));

        //TODO delete
        subscriptions.add(RxView.clicks(btn_delete).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                BtnDeleteClick();
            }
        }));
    }

    private boolean checkConditionConfig() {
        if (GlobalConstant.DEVICE_PHONE_NUMBER.equals("")) {
            SuperToast.create(HomeActivity.this,
                    getResources().getString(R.string.error_message_lack_config),
                    SuperToast.Duration.SHORT,
                    Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
            configDialog.show();
            return false;
        }
        return true;
    }

    private boolean isReachMaxWaitingTime(boolean isFingerLoading) {
        count++;
        if (count >= GlobalConstant.MAX_WAITING_TIME / GlobalConstant.AUTO_CALL_API_TIME) {
            if(!isFingerLoading){
                isTurnOnAntiThief = viewModel.updateAntiThiefView(iv_anti_thief, iv_anti_thief_sub,
                        tv_anti_thief, isTurnOnAntiThief);
                viewModel.toggleAntiThief(isTurnOnAntiThief);
                sharedPreferences.edit().putBoolean(GlobalConstant.ANTI_THIEF_STATUS_KEY,
                        isTurnOnAntiThief).apply();
            }
            dismissLoadingDialog();
            count = 0;
            showConfirmDialog(false);
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //Event for config dialog
    //----------------------------------------------------------------------------------------------
    @Override
    public void btnQuitClicked() {
        this.finish();
    }

    @Override
    public void btnDoneClicked() {
        //Send data to device
        DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER, GlobalConstant.CONTENT_UPDATE_PHONE + " " + configDialog.getPhoneNumber());
        DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER, GlobalConstant.CONTENT_UPDATE_FINGER + " 0");

        configDialog.hide();
        configDialog.dismiss();

        //show config finger dialog
        showFingerSetupLoadingDialog();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlobalConstant.PHONE_NUMBER = configDialog.getPhoneNumber();
                InfoRequest param = new InfoRequest(GlobalConstant.decryptPhoneNumber());
                Call<InfoResponse> call = service.getInfo(param);
                call.enqueue(new Callback<InfoResponse>() {
                    @Override
                    public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                        if(!response.body().getStatus().equals("OK")){
                            return;
                        }

                        if (!response.body().getMessage().equals("")
                                && !response.body().getMessage().equals("null_dattd2210")) {
                            handler.removeCallbacksAndMessages(null);
                            count = 0;
                            showConfirmDialog(true);
                            saveUserData();
                            dismissFingerSetupLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<InfoResponse> call, Throwable t) {

                    }
                });
                if (isReachMaxWaitingTime(true)) {
                    handler.removeCallbacksAndMessages(null);
                    dismissFingerSetupLoadingDialog();
                    GlobalConstant.PHONE_NUMBER = "";
                } else {
                    handler.postDelayed(this, GlobalConstant.AUTO_CALL_API_TIME);
                }
            }
        }, GlobalConstant.AUTO_CALL_API_TIME);
    }

    private void saveUserData(){
        //Save to Preferences
        sharedPreferences.edit().putString(GlobalConstant.DEVICE_PHONE_NUMBER_KEY,
                configDialog.getDeviceNumber()).apply();
        sharedPreferences.edit().putString(GlobalConstant.PASSWORD_KEY,
                GlobalConstant.encryptPassword(configDialog.getPassword())).apply();
        sharedPreferences.edit().putInt(GlobalConstant.PASSWORD_LENGTH_KEY,
                configDialog.getPassword().length()).apply();
        sharedPreferences.edit().putString(GlobalConstant.PHONE_NUMBER_KEY,
                configDialog.getPhoneNumber()).apply();
        sharedPreferences.edit().putBoolean(GlobalConstant.ANTI_THIEF_STATUS_KEY,
                true).apply();

        //Set data to globalConstant thus we can reuse it later
        GlobalConstant.DEVICE_PHONE_NUMBER = configDialog.getDeviceNumber();
        GlobalConstant.PASSWORD = GlobalConstant.encryptPassword(configDialog.getPassword());
        GlobalConstant.PASSWORD_LENGTH = configDialog.getPassword().length();
        GlobalConstant.PHONE_NUMBER = configDialog.getPhoneNumber();
        isTurnOnAntiThief = true;
    }

    //----------------------------------------------------------------------------------------------
    //Event for password input dialog
    //----------------------------------------------------------------------------------------------
    @Override
    public void passwordChecking() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.acitivity_in_from_right_to_left, R.anim.hold);
    }

    @Override
    public void btnSkipClicked() {
        configDialog.setCanSkip(false);
        configDialog.showSkipButton();
        configDialog.hide();
    }


    //TODO delete
    public void BtnDeleteClick() {
        File deletePrefFile = new File("/data/user/0/com.trandat.lockmotor/shared_prefs/SHARE_PREFERENCES_NAME.xml.bak");
        deletePrefFile.delete();
        File deletePrefFile2 = new File("/data/user/0/com.trandat.lockmotor/shared_prefs/SHARE_PREFERENCES_NAME.xml");
        deletePrefFile2.delete();

        GlobalConstant.DEVICE_PHONE_NUMBER = "";
        GlobalConstant.PASSWORD = "";
        GlobalConstant.PASSWORD_LENGTH = 0;
    }
}
