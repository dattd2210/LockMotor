package com.lockmotor.implement.views.home;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.lockmotor.R;
import com.lockmotor.global.PhoneUtils;

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
    @BindView(R.id.et_config_password)
    EditText et_config_password;
    @BindView(R.id.et_config_password_confirm)
    EditText et_config_password_confirm;
    @BindView(R.id.et_config_phone_number)
    EditText et_config_phone_number;
    @BindView(R.id.btn_skip)
    Button btn_skip;
    @BindView(R.id.tv_config_title)
    TextView tv_config_title;

    @BindView(R.id.sn_config_network_provider_1)
    Spinner sn_config_network_provider_1;
    @BindView(R.id.sn_config_network_provider_2)
    Spinner sn_config_network_provider_2;

    private EventHandler listener;
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private static boolean canSkip = true;

    public ConfigDialog(Context context) {
        super(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);

        initSubscription();

        sn_config_network_provider_1.setEnabled(false);
        sn_config_network_provider_2.setEnabled(false);

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

    public String getDeviceNumber() {
        return et_device_number.getText().toString();
    }

    public String getPassword() {
        return et_config_password.getText().toString();
    }

    public String getPhoneNumber() {
        return et_config_phone_number.getText().toString();
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

    public void showSkipButton()
    {
//        if(canSkip || !GlobalConstant.HAS_OPEN_SETTING) {
            btn_skip.setVisibility(View.VISIBLE);
//        }
//        else {
//            btn_skip.setVisibility(View.GONE);
//            tv_config_title.setGravity(Gravity.CENTER);
//        }
    }

    public void setCanSkip(boolean canSkip) {
        this.canSkip = canSkip;
    }

    public static boolean isCanSkip() {
        return canSkip;
    }

    private void showError() {
        if (et_device_number.getText().toString().length() < 10) {
            showErrorInput(et_device_number);
        }
        if (et_config_phone_number.getText().toString().length() < 10) {
            showErrorInput(et_config_phone_number);
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
        if (et_device_number.getText().toString().length() < 10) return false;
        if (et_config_phone_number.getText().toString().length() < 10) return false;
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

    private void hideNetProvider(Spinner sn) {
        sn.setEnabled(false);
        sn.setSelection(0, true);
    }

    private void showNetProvider(Spinner sn, String phoneNumber) {
        sn.setEnabled(true);
        sn.setSelection(PhoneUtils.getNetProviderFromPhoneNumber(phoneNumber), true);
    }

    private void initSubscription() {

        compositeSubscription.add(RxTextView.textChanges(et_device_number)
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
                            hideNetProvider(sn_config_network_provider_1);
                            showErrorInput(et_device_number);
                        } else {
                            showNetProvider(sn_config_network_provider_1, et_device_number.getText().toString());
                            hideErrorInput(et_device_number);
                        }
                    }
                }));

        compositeSubscription.add(RxTextView.textChanges(et_config_phone_number)
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
                            hideNetProvider(sn_config_network_provider_2);
                            showErrorInput(et_config_phone_number);
                        } else {
                            showNetProvider(sn_config_network_provider_2, et_config_phone_number.getText().toString());
                            hideErrorInput(et_config_phone_number);
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

        compositeSubscription.add(RxView.clicks(btn_quit)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.btnQuitClicked();
                    }
                }));

        compositeSubscription.add(RxView.clicks(btn_done)
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
                        listener.btnDoneClicked();
                    }
                }));

        compositeSubscription.add(RxView.clicks(btn_skip)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.btnSkipClicked();
                    }
                }));
    }

    //----------------------------------------------------------------------------------------------
    //Event listener
    //----------------------------------------------------------------------------------------------
    interface EventHandler {
        void btnQuitClicked();

        void btnDoneClicked();

        void btnSkipClicked();
    }

    public void setListener(EventHandler listener) {
        this.listener = listener;
    }
}
