package com.lockmotor.implement.views.home;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.lockmotor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 8/13/16.
 */
public class ConfigDialog extends Dialog {

    @BindView(R.id.btn_config_cancel)
    Button btn_quit;
    @BindView(R.id.btn_config_done)
    Button btn_done;
    @BindView(R.id.et_config_device_number)
    EditText et_device_number;
    @BindView(R.id.et_config_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_config_password)
    EditText et_config_password;
    @BindView(R.id.et_config_password_confirm)
    EditText et_config_password_confirm;

    private EventHandler listener;
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    public ConfigDialog(Context context) {
        super(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);

        initSubscription();

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnQuitClicked(v);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCondition()) {
                    SuperToast.create(getContext(),
                            getContext().getResources().getString(R.string.error_message_input),
                            SuperToast.Duration.SHORT,
                            Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
                    showError();
                    return;
                }
                listener.btnDoneClicked(v);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        compositeSubscription.clear();
        compositeSubscription.unsubscribe();
    }

    //----------------------------------------------------------------------------------------------
    //Getter
    //----------------------------------------------------------------------------------------------
    public String getPhoneNumber() {
        return et_phone_number.getText().toString();
    }

    public String getDeviceNumber() {
        return et_device_number.getText().toString();
    }

    public String getPassword() {
        return et_config_password.getText().toString();
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private void setBtn_doneEnable() {
        if (checkCondition()) {
            btn_done.setBackgroundResource(R.drawable.btn_orange_border_radius);
        } else {
            btn_done.setBackgroundResource(R.drawable.btn_disable_border_radius);
        }
    }

    private void showError() {
        if (et_phone_number.getText().toString().length() < 10) {
            showErrorInput(et_phone_number);
        }
        if (et_device_number.getText().toString().length() < 10) {
            showErrorInput(et_device_number);
        }
        if (et_device_number.getText().toString().equals(et_phone_number.getText().toString())) {
            showErrorInput(et_device_number);
        }
        if (et_config_password.getText().toString().length() <= 6) {
            showErrorInput(et_config_password);
        }
        if (et_config_password_confirm.getText().toString().length() <= 6) {
            showErrorInput(et_config_password_confirm);
        }
        if (!et_config_password_confirm.getText().toString().equals(et_config_password.getText().toString())) {
            showErrorInput(et_config_password_confirm);
        }
    }

    private boolean checkCondition() {
        if (et_phone_number.getText().toString().length() < 10) return false;
        if (et_device_number.getText().toString().length() < 10) return false;
        if (et_device_number.getText().toString().equals(et_phone_number.getText().toString()))
            return false;
        if (et_config_password.getText().toString().length() <= 6) return false;
        if (et_config_password_confirm.getText().toString().length() <= 6) return false;
        if (!et_config_password_confirm.getText().toString().equals(et_config_password.getText().toString()))
            return false;

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

    private void initSubscription() {

        compositeSubscription.add(RxTextView.textChanges(et_phone_number)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        if (charSequence.toString().length() >= 10) {
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
                            showErrorInput(et_phone_number);
                        } else {
                            hideErrorInput(et_phone_number);
                        }
                    }
                }));

        compositeSubscription.add(RxTextView.textChanges(et_device_number)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        if (charSequence.toString().length() >= 10
                                && !et_device_number.getText().toString().equals(et_phone_number.getText().toString())) {
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
                            showErrorInput(et_device_number);
                        } else {
                            hideErrorInput(et_device_number);
                        }
                    }
                }));

        compositeSubscription.add(RxTextView.textChanges(et_config_password)
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
                            showErrorInput(et_config_password);
                        } else {
                            hideErrorInput(et_config_password);
                        }
                    }
                }));

        compositeSubscription.add(RxTextView.textChanges(et_config_password_confirm)
                .map(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        if (charSequence.toString().length() > 6
                                && et_config_password_confirm.getText().toString().equals(et_config_password.getText().toString())) {
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
                            showErrorInput(et_config_password_confirm);
                        } else {
                            hideErrorInput(et_config_password_confirm);
                        }
                    }
                }));
    }

    //----------------------------------------------------------------------------------------------
    //Event listener
    //----------------------------------------------------------------------------------------------
    interface EventHandler {
        void btnQuitClicked(@NonNull View view);

        void btnDoneClicked(@NonNull View view);
    }

    public void setListener(EventHandler listener) {
        this.listener = listener;
    }
}
