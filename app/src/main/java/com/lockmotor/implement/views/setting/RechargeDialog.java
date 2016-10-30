package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;
import com.lockmotor.base.utils.DeviceUtils;
import com.lockmotor.global.GlobalConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 8/28/16.
 */
public class RechargeDialog extends Dialog {
    @BindView(R.id.btn_recharge_ok)
    Button btn_recharge_ok;
    @BindView(R.id.btn_recharge_cancel)
    Button btn_recharge_cancel;
    @BindView(R.id.et_num_card)
    EditText et_num_card;

    public static final CompositeSubscription subscriptions = new CompositeSubscription();

    public RechargeDialog(Context context) {
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
        subscriptions.add(RxView.clicks(btn_recharge_cancel)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dismiss();
                    }
                }));
        subscriptions.add(RxView.clicks(btn_recharge_ok)
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

                if(et_num_card.getText().toString().equals("")){
                    SuperToast.create(getContext(),
                            getContext().getResources().getString(R.string.error_message_lack_card_number),
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
        //TODO listen server
        DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER,GlobalConstant.RECHARGE_VIETEL+et_num_card);
    }
}
