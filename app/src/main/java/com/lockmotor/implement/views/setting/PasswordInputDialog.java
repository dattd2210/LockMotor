package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by trandinhdat on 11/12/16.
 */

public class PasswordInputDialog extends Dialog {

    @BindView(R.id.btn_pwd_ok)
    Button btn_pwd_ok;
    @BindView(R.id.btn_pwd_cancel)
    Button btn_pwd_cancel;
    @BindView(R.id.et_password)
    EditText et_password;

    private EventHandler listener;

    public static final CompositeSubscription subscriptions = new CompositeSubscription();

    public PasswordInputDialog(Context context,EventHandler listener) {
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
        subscriptions.add(RxView.clicks(btn_pwd_cancel)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dismiss();
                    }
                }));
        subscriptions.add(RxView.clicks(btn_pwd_ok)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(et_password.getText().toString().equals("")
                                || !et_password.getText().toString()
                                .equals(GlobalConstant.decryptPassword(GlobalConstant.PASSWORD, GlobalConstant.PASSWORD_LENGTH))){
                            SuperToast.create(getContext(),
                                    getContext().getResources().getString(R.string.error_message_wrong_password),
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
        listener.passwordChecking();
    }

    public interface EventHandler{
        void passwordChecking();
    }
}
