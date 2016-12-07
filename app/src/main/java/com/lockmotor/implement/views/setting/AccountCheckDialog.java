package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 12/7/16.
 */

public class AccountCheckDialog extends Dialog {
    @BindView(R.id.tv_account_info)
    TextView tv_account_info;
    @BindView(R.id.btn_account_check_ok)
    Button btn_account_check_ok;

    public static final CompositeSubscription subscriptions = new CompositeSubscription();

    public AccountCheckDialog(Context context) {
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
        subscriptions.add(RxView.clicks(btn_account_check_ok)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dismiss();
                    }
                }));
    }

    public void setAccountInfo(String info){
        tv_account_info.setText(info);
    }
}
