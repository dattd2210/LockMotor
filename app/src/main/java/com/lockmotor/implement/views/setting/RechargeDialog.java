package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;

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
                sendRequest();
                dismiss();
            }
        }));
    }

    private void sendRequest()
    {
        //TODO send request and listen server
    }
}
