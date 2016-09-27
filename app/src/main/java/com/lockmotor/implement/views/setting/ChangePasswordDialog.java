package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.lockmotor.R;
import com.lockmotor.global.GlobalConstant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 8/28/16.
 */
public class ChangePasswordDialog extends Dialog {
    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.btn_change_password_cancel)
    Button btn_change_password_cancel;
    @BindView(R.id.btn_change_password_ok)
    Button btn_change_password_ok;
    @BindView(R.id.et_old_password)
    EditText et_old_password;
    @BindView(R.id.et_new_password)
    EditText et_new_password;
    @BindView(R.id.et_confirm_password)
    EditText et_confirm_password;

    public static final CompositeSubscription subscriptions = new CompositeSubscription();

    public ChangePasswordDialog(Context context,SharedPreferences sharedPreferences) {
        super(context);
        this.sharedPreferences = sharedPreferences;
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
        subscriptions.add(RxView.clicks(btn_change_password_cancel)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dismiss();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_change_password_ok)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (!checkCondition()) {
                            SuperToast.create(getContext(),
                                    getContext().getResources().getString(R.string.error_message_input),
                                    SuperToast.Duration.SHORT,
                                    Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
                            showError();
                            return;
                        }

                        sendRequest();
                        dismiss();
                    }
                }));
        subscriptions.add(RxTextView.textChanges(et_new_password)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        if (charSequence.toString().length() > 6) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .skip(1)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isValid) {
                        setBtn_doneEnable();
                        if (!isValid) {
                            showErrorInput(et_new_password);
                        } else {
                            hideErrorInput(et_new_password);
                        }
                    }
                }));

        subscriptions.add(RxTextView.textChanges(et_confirm_password)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        if (charSequence.toString().length() > 6
                                && et_confirm_password.getText().toString().equals(et_confirm_password.getText().toString())) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .skip(1)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isValid) {
                        setBtn_doneEnable();
                        if (!isValid) {
                            showErrorInput(et_confirm_password);
                        } else {
                            hideErrorInput(et_confirm_password);
                        }
                    }
                }));
    }

    private void sendRequest() {
        //Save to Preferences
        sharedPreferences.edit().putString(GlobalConstant.PASSWORD_KEY,
                GlobalConstant.encryptPassword(et_new_password.getText().toString())).apply();
        sharedPreferences.edit().putInt(GlobalConstant.PASSWORD_LENGTH_KEY,
                et_new_password.getText().toString().length()).apply();

        //Set data to globalConstant thus we can reuse it later
        GlobalConstant.PASSWORD = GlobalConstant.encryptPassword(et_new_password.getText().toString());
        GlobalConstant.PASSWORD_LENGTH = et_new_password.getText().toString().length();
    }

    private void showError() {
        if (et_confirm_password.getText().toString().length() <= 6) {
            showErrorInput(et_confirm_password);
        }
        if (et_new_password.getText().toString().length() <= 6) {
            showErrorInput(et_new_password);
        }
        if (et_old_password.getText().toString().length() <= 6) {
            showErrorInput(et_old_password);
        }
        if (!et_confirm_password.getText().toString().equals(et_new_password.getText().toString())) {
            showErrorInput(et_confirm_password);
        }
        if (!et_old_password.getText().toString()
                .equals(GlobalConstant.decryptPassword(GlobalConstant.PASSWORD, GlobalConstant.PASSWORD_LENGTH))) {
            showErrorInput(et_old_password);
        }
    }

    private boolean checkCondition() {
        if (!et_old_password.getText().toString()
                .equals(GlobalConstant.decryptPassword(GlobalConstant.PASSWORD, GlobalConstant.PASSWORD_LENGTH))) {
            return false;
        }
        if (et_old_password.getText().toString().length() <= 6) return false;
        if (et_new_password.getText().toString().length() <= 6) return false;
        if (et_confirm_password.getText().toString().length() <= 6) return false;
        if (!et_confirm_password.getText().toString().equals(et_new_password.getText().toString())) {
            return false;
        }

        return true;
    }

    private void showErrorInput(EditText et) {
        et.setBackgroundResource(R.drawable.et_red_border);
        et.setTextColor(this.getContext().getResources().getColor(R.color.red_color));
    }

    private void hideErrorInput(EditText et) {
        et.setBackgroundResource(R.drawable.et_highlight_border);
        et.setTextColor(this.getContext().getResources().getColor(R.color.black_color));
    }

    private void setBtn_doneEnable() {
        if (checkCondition()) {
            btn_change_password_ok.setBackgroundResource(R.drawable.btn_orange_border_radius);
        } else {
            btn_change_password_ok.setBackgroundResource(R.drawable.btn_disable_border_radius);
        }
    }
}
