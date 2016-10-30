package com.lockmotor.implement.views.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.implement.LockMotorActivity;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 8/13/16.
 */
public class SettingActivity extends LockMotorActivity {
    @Inject
    SharedPreferences sharedPreferences;

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
                            //TODO check for net provide and send correspond message
                            DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER,GlobalConstant.CHECK_ACCOUNT_VIETEL);
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

        rechargeDialog = new RechargeDialog(this);
        rechargeDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        rechargeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rechargeDialog.setContentView(R.layout.dialog_account_recharge);
        lp.copyFrom(rechargeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        rechargeDialog.getWindow().setAttributes(lp);

        settingFingerDialog = new SettingFingerDialog(this);
        settingFingerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        settingFingerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingFingerDialog.setContentView(R.layout.dialog_setting_finger_printer);
        lp.copyFrom(settingFingerDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        settingFingerDialog.getWindow().setAttributes(lp);

        settingDevicePhoneDialog = new SettingDevicePhoneDialog(this);
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
    }
}
