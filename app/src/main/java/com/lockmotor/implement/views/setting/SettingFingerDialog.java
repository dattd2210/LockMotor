package com.lockmotor.implement.views.setting;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import static com.lockmotor.global.GlobalConstant.getNextFingerID;

/**
 * Created by trandinhdat on 8/28/16.
 */
public class SettingFingerDialog extends Dialog {
    @BindView(R.id.btn_finger_printer_ok)
    Button btn_finger_printer_ok;
    @BindView(R.id.btn_add_finger)
    TextView btn_add_finger;
    @BindView(R.id.iv_add_icon)
    ImageView iv_add_finger;
    @BindView(R.id.btn_delete_finger_1)
    ImageView btn_delete_finger_1;
    @BindView(R.id.et_finger_id_1)
    EditText et_finger_id_1;
    @BindView(R.id.btn_delete_finger_2)
    ImageView btn_delete_finger_2;
    @BindView(R.id.et_finger_id_2)
    EditText et_finger_id_2;
    @BindView(R.id.btn_delete_finger_3)
    ImageView btn_delete_finger_3;
    @BindView(R.id.et_finger_id_3)
    EditText et_finger_id_3;
    @BindView(R.id.btn_delete_finger_4)
    ImageView btn_delete_finger_4;
    @BindView(R.id.et_finger_id_4)
    EditText et_finger_id_4;

    public static final CompositeSubscription subscriptions = new CompositeSubscription();
    private SettingFingerListener listener;
    private int count1,count2,count3,count4 = 0;

    public SettingFingerDialog(Context context, SettingFingerListener listener) {
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
        updateUI();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        subscriptions.clear();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateUI();
        }
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private void initSubscription() {
        subscriptions.add(RxView.clicks(btn_add_finger)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        sendRequest();
                    }
                }));

        subscriptions.add(RxView.clicks(iv_add_finger)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        sendRequest();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_finger_printer_ok)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dismiss();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_delete_finger_1)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        count1 ++;
                        count2 = count3 = count4 = 0;
                        if(count1 >= 2){
                            deleteRequest(1);
                            return;
                        }
                        SuperToast.create(getContext(),
                                getContext().getResources().getString(R.string.hint_message_double_click),
                                SuperToast.Duration.SHORT,
                                Style.getStyle(Style.GRAY, SuperToast.Animations.FLYIN)).show();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_delete_finger_4)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        count4 ++;
                        count2 = count3 = count1 = 0;
                        if(count4 >= 2){
                            deleteRequest(4);
                            return;
                        }
                        SuperToast.create(getContext(),
                                getContext().getResources().getString(R.string.hint_message_double_click),
                                SuperToast.Duration.SHORT,
                                Style.getStyle(Style.GRAY, SuperToast.Animations.FLYIN)).show();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_delete_finger_2)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        count2 ++;
                        count1 = count3 = count4 = 0;
                        if(count2 >= 2){
                            deleteRequest(2);
                            return;
                        }
                        SuperToast.create(getContext(),
                                getContext().getResources().getString(R.string.hint_message_double_click),
                                SuperToast.Duration.SHORT,
                                Style.getStyle(Style.GRAY, SuperToast.Animations.FLYIN)).show();
                    }
                }));

        subscriptions.add(RxView.clicks(btn_delete_finger_3)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        count3 ++;
                        count2 = count1 = count4 = 0;
                        if(count3 >= 2){
                            deleteRequest(3);
                            return;
                        }
                        SuperToast.create(getContext(),
                                getContext().getResources().getString(R.string.hint_message_double_click),
                                SuperToast.Duration.SHORT,
                                Style.getStyle(Style.GRAY, SuperToast.Animations.FLYIN)).show();
                    }
                }));
    }

    private void sendRequest() {
        GlobalConstant.ADDED_ID = getNextFingerID();
        DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER, GlobalConstant.CONTENT_UPDATE_FINGER + " " + GlobalConstant.ADDED_ID);
        listener.settingFingerBtnAddClick();
    }

    private void deleteRequest(int id){
        count1 = count2 = count3 = count4 = 0;
        GlobalConstant.DELETE_ID = id+"";
        DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER, GlobalConstant.CONTENT_DELETE_FINGER + " " + id);
        listener.settingFingerBtnDeleteClick();
    }

    public void updateUI() {
        if (!GlobalConstant.getNextFingerID().equals("")) {
            iv_add_finger.setVisibility(View.VISIBLE);
            btn_add_finger.setVisibility(View.VISIBLE);
        } else {
            iv_add_finger.setVisibility(View.GONE);
            btn_add_finger.setVisibility(View.GONE);
        }

        //finger 1
        if(GlobalConstant.FINGER_ID_LIST.contains("1")){
            et_finger_id_1.setVisibility(View.VISIBLE);
            et_finger_id_1.setText(getContext().getResources().getString(R.string.et_finger_default)+" 1");
            et_finger_id_1.clearFocus();
            btn_delete_finger_1.setVisibility(View.VISIBLE);
        }else{
            et_finger_id_1.setVisibility(View.GONE);
            btn_delete_finger_1.setVisibility(View.GONE);
        }

        //finger 2
        if(GlobalConstant.FINGER_ID_LIST.contains("2")){
            et_finger_id_2.setVisibility(View.VISIBLE);
            et_finger_id_2.setText(getContext().getResources().getString(R.string.et_finger_default)+" 2");
            et_finger_id_2.clearFocus();
            btn_delete_finger_2.setVisibility(View.VISIBLE);
        }else{
            et_finger_id_2.setVisibility(View.GONE);
            btn_delete_finger_2.setVisibility(View.GONE);
        }

        //finger 3
        if(GlobalConstant.FINGER_ID_LIST.contains("3")){
            et_finger_id_3.setVisibility(View.VISIBLE);
            et_finger_id_3.setText(getContext().getResources().getString(R.string.et_finger_default)+" 3");
            et_finger_id_3.clearFocus();
            btn_delete_finger_3.setVisibility(View.VISIBLE);
        }else{
            et_finger_id_3.setVisibility(View.GONE);
            btn_delete_finger_3.setVisibility(View.GONE);
        }

        //finger 4
        if(GlobalConstant.FINGER_ID_LIST.contains("4")){
            et_finger_id_4.setVisibility(View.VISIBLE);
            et_finger_id_4.setText(getContext().getResources().getString(R.string.et_finger_default)+" 4");
            et_finger_id_4.clearFocus();
            btn_delete_finger_4.setVisibility(View.VISIBLE);
        }else{
            et_finger_id_4.setVisibility(View.GONE);
            btn_delete_finger_4.setVisibility(View.GONE);
        }
    }

    public interface SettingFingerListener {
        void settingFingerBtnAddClick();
        void settingFingerBtnDeleteClick();
    }
}
