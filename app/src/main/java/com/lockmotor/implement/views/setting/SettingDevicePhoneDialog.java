package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.lockmotor.R;
import com.lockmotor.base.utils.DeviceUtils;
import com.lockmotor.global.GlobalConstant;
import com.lockmotor.global.PhoneUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 8/28/16.
 */
public class SettingDevicePhoneDialog extends Dialog {

    @BindView(R.id.btn_phone_number_cancel)
    Button btn_phone_number_cancel;
    @BindView(R.id.btn_phone_number_ok)
    Button btn_phone_number_ok;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.sn_phone_number_type)
    Spinner sn_phone_number_type;

    private boolean isValidNumber = false;
    private SettingDevicePhoneListener listener;

    public static final CompositeSubscription subscriptions = new CompositeSubscription();

    public SettingDevicePhoneDialog(Context context, SettingDevicePhoneListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

    }

    @Override
    public void show() {
        super.show();
        initSubscription();
        hideNetProvider(sn_phone_number_type);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        subscriptions.clear();
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private void initSubscription() {
        subscriptions.add(RxView.clicks(btn_phone_number_cancel)
        .subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        }));

        subscriptions.add(RxTextView.textChanges(et_phone_number)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        if (charSequence.toString().length() >= 10) {
                            isValidNumber = true;
                            return true;
                        } else {
                            isValidNumber = false;
                            return false;
                        }
                    }
                })
                .skip(1)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isValid) {
                        if (!isValid) {
                            hideNetProvider(sn_phone_number_type);
                            showErrorInput(et_phone_number);
                        } else {
                            showNetProvider(sn_phone_number_type, et_phone_number.getText().toString());
                            hideErrorInput(et_phone_number);
                        }
                    }
                }));

        subscriptions.add(RxView.clicks(btn_phone_number_ok)
        .subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(GlobalConstant.DEVICE_PHONE_NUMBER == "")
                {
                    SuperToast.create(getContext(),
                            getContext().getResources().getString(R.string.error_message_lack_config),
                            SuperToast.Duration.SHORT,
                            Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
                    return;
                }

                if(!isValidNumber){
                    SuperToast.create(getContext(),
                            getContext().getResources().getString(R.string.error_message_lack_phone_number),
                            SuperToast.Duration.SHORT,
                            Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
                    return;
                }

                sendRequest();
                dismiss();
            }
        }));
    }

    private void sendRequest()
    {
        DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER, GlobalConstant.CONTENT_UPDATE_PHONE + " " + et_phone_number.getText().toString());
        listener.settingPhoneBtnOkClick();
    }

    private void showErrorInput(EditText et) {
        et.setBackgroundResource(R.drawable.et_red_border);
        et.setTextColor(this.getContext().getResources().getColor(R.color.red_color));
    }

    private void hideErrorInput(EditText et) {
        et.setBackgroundResource(R.drawable.et_highlight_border);
        et.setTextColor(this.getContext().getResources().getColor(R.color.black_color));
    }

    private void hideNetProvider(Spinner sn) {
        sn.setEnabled(false);
        sn.setSelection(0, true);
    }

    private void showNetProvider(Spinner sn, String phoneNumber) {
        sn.setEnabled(true);
        sn.setSelection(PhoneUtils.getNetProviderFromPhoneNumber(phoneNumber), true);
    }

    //----------------------------------------------------------------------------------------------
    //Implement listener
    //----------------------------------------------------------------------------------------------
    public interface SettingDevicePhoneListener{
        void settingPhoneBtnOkClick();
    }
}
