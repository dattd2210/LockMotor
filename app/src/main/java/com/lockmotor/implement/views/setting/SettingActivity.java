package com.lockmotor.implement.views.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

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

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 8/13/16.
 */
public class SettingActivity extends LockMotorActivity implements
        RechargeDialog.RechargeDialogListener,
        SettingDevicePhoneDialog.SettingDevicePhoneListener,
        SettingFingerDialog.SettingFingerListener{
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    LockMotorAPI service;

    @BindView(R.id.iv_back_press)
    ImageView iv_back_press;
    @BindView(R.id.btn_account_check)
    Button btn_account_check;
    @BindView(R.id.btn_account_recharge)
    Button btn_account_recharge;
    @BindView(R.id.btn_setting_finger_printer)
    Button btn_setting_finger_printer;
    @BindView(R.id.btn_setting_phone_number)
    Button btn_setting_phone_number;
    @BindView(R.id.btn_change_password)
    Button btn_change_password;
    @BindView(R.id.btn_help)
    Button btn_help;

    final private CompositeSubscription subscriptions = new CompositeSubscription();
    private RechargeDialog rechargeDialog;
    private SettingFingerDialog settingFingerDialog;
    private SettingDevicePhoneDialog settingDevicePhoneDialog;
    private ChangePasswordDialog changePasswordDialog;
    private  AccountCheckDialog accountCheckDialog;
    private int count = 0;

    @Override
    protected void injectComponent(DIComponent component) {
        component.inject(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConstant.HAS_OPEN_SETTING = true;
        initDialog();
        initSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
        subscriptions.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.acitivity_in_from_left_to_right, R.anim.hold);
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private void initSubscription() {
        //clear subscriptions to ensure action won't duplicate
        subscriptions.clear();

        subscriptions.add(RxView.clicks(iv_back_press)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                        overridePendingTransition(R.anim.acitivity_in_from_left_to_right, R.anim.hold);
                    }
                }));

        subscriptions.add(RxView.clicks(btn_account_check)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(GlobalConstant.DEVICE_PHONE_NUMBER == "")
                        {
                            SuperToast.create(SettingActivity.this,
                                    getResources().getString(R.string.error_message_lack_config),
                                    SuperToast.Duration.SHORT,
                                    Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
                        }else {
                            DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER,GlobalConstant.CHECK_ACCOUNT_STRING);
                            callAPI(GlobalConstant.CHECK_ACCOUNT_ID);
                        }
                    }
                }));

        subscriptions.add(RxView.clicks(btn_account_recharge)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        rechargeDialog.show();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_setting_finger_printer)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        GlobalConstant.FINGER_ID_LIST = sharedPreferences.getString(GlobalConstant.FINGER_ID_KEY,"");
                        settingFingerDialog.show();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_setting_phone_number)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        settingDevicePhoneDialog.show();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_change_password)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        changePasswordDialog.show();
                    }
                }));
    }

    private void initDialog() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        rechargeDialog = new RechargeDialog(this,this);
        rechargeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        rechargeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rechargeDialog.setContentView(R.layout.dialog_account_recharge);
        lp.copyFrom(rechargeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        rechargeDialog.getWindow().setAttributes(lp);

        settingFingerDialog = new SettingFingerDialog(this,this);
        settingFingerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        settingFingerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingFingerDialog.setContentView(R.layout.dialog_setting_finger_printer);
        lp.copyFrom(settingFingerDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        settingFingerDialog.getWindow().setAttributes(lp);

        settingDevicePhoneDialog = new SettingDevicePhoneDialog(this,this);
        settingDevicePhoneDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        settingDevicePhoneDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingDevicePhoneDialog.setContentView(R.layout.dialog_setting_phone_number);
        lp.copyFrom(settingDevicePhoneDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        settingDevicePhoneDialog.getWindow().setAttributes(lp);

        changePasswordDialog = new ChangePasswordDialog(this,sharedPreferences);
        changePasswordDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        changePasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        lp.copyFrom(changePasswordDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        changePasswordDialog.getWindow().setAttributes(lp);

        accountCheckDialog = new AccountCheckDialog(this);
        accountCheckDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        accountCheckDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        accountCheckDialog.setContentView(R.layout.dialog_account_check);
        lp.copyFrom(accountCheckDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        accountCheckDialog.getWindow().setAttributes(lp);
    }

    //----------------------------------------------------------------------------------------------
    //Call API part
    //----------------------------------------------------------------------------------------------
    void callAPI(final int id){
        if(id != GlobalConstant.FINGER_UPDATE_ID) {
            showLoadingDialog();
        }
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

                        if (!response.body().getMessage().equals("") && !response.body().getMessage().equals("null_dattd2210")) {
                            handler.removeCallbacksAndMessages(null);
                            dismissLoadingDialog();
                            count = 0;
                            switch (id){
                                case GlobalConstant.CHECK_ACCOUNT_ID:
                                    accountCheckDialog.setAccountInfo(response.body().getMessage());
                                    accountCheckDialog.show();
                                    break;
                                case GlobalConstant.FINGER_UPDATE_ID:
                                    showConfirmDialog(true);
                                    dismissFingerSetupLoadingDialog();

                                    GlobalConstant.FINGER_ID_LIST += GlobalConstant.ADDED_ID;
                                    sharedPreferences.edit().putString(GlobalConstant.FINGER_ID_KEY,
                                            GlobalConstant.FINGER_ID_LIST).apply();
                                    break;
                                case GlobalConstant.FINGER_DELETE_ID:
                                    showConfirmDialog(true);

                                    GlobalConstant.FINGER_ID_LIST = GlobalConstant.FINGER_ID_LIST.replace(GlobalConstant.DELETE_ID,"");
                                    sharedPreferences.edit().putString(GlobalConstant.FINGER_ID_KEY,
                                            GlobalConstant.FINGER_ID_LIST).apply();
                                    break;
                                default:
                                    showConfirmDialog(true);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<InfoResponse> call, Throwable t) {

                    }
                });
                if (isReachMaxWaitingTime()) {
                    handler.removeCallbacksAndMessages(null);
                } else {
                    handler.postDelayed(this, GlobalConstant.AUTO_CALL_API_TIME);
                }
            }
        }, GlobalConstant.AUTO_CALL_API_TIME);
    }

    private boolean isReachMaxWaitingTime() {
        count++;
        if (count >= GlobalConstant.MAX_WAITING_TIME / GlobalConstant.AUTO_CALL_API_TIME) {
            dismissLoadingDialog();
            dismissFingerSetupLoadingDialog();
            count = 0;
            showConfirmDialog(false);
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //Implement listeners
    //----------------------------------------------------------------------------------------------

    @Override
    public void rechargeBtnOKClick() {
        callAPI(GlobalConstant.RECHARGE_ACCOUNT_ID);
    }

    @Override
    public void settingPhoneBtnOkClick() {
        callAPI(GlobalConstant.PHONE_NUMBER_UPDATE_ID);
    }

    @Override
    public void settingFingerBtnAddClick() {
        showFingerSetupLoadingDialog();
        callAPI(GlobalConstant.FINGER_UPDATE_ID);
    }

    @Override
    public void settingFingerBtnDeleteClick() {
        callAPI(GlobalConstant.FINGER_DELETE_ID);
    }
}
