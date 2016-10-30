package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;
import com.lockmotor.global.GlobalConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 8/28/16.
 */
public class SettingDevicePhoneDialog extends Dialog {

    @BindView(R.id.btn_phone_number_cancel)
    Button btn_phone_number_cancel;
    @BindView(R.id.btn_phone_number_ok)
    Button btn_phone_number_ok;

    public static final CompositeSubscription subscriptions = new CompositeSubscription();

    public SettingDevicePhoneDialog(Context context) {
        super(context);
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

                sendRequest();
                dismiss();
            }
        }));
    }

    private void sendRequest()
    {
        //// TODO: 8/28/16 send request and listen to server

    }
}