package com.lockmotor.implement.views.home;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;
import com.lockmotor.global.GlobalConstant;
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.implement.LockMotorActivity;
import com.lockmotor.implement.viewModels.HomeViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public class HomeActivity extends LockMotorActivity implements ConfigDeviceNumberDialog.EventHandler{

    //Environment variables
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SmsManager smsManager;

    //Init View
    @BindView(R.id.btn_turn_off_engine1)
    Button btn_turn_off_engine1;
    @BindView(R.id.btn_turn_off_engine2)
    Button btn_turn_off_engine2;
    @BindView(R.id.iv_turn_off_engine)
    ImageView iv_turn_off_engine;

    @BindView(R.id.btn_find_location1)
    Button btn_find_location1;
    @BindView(R.id.btn_find_location2)
    Button btn_find_location2;
    @BindView(R.id.iv_find_location)
    ImageView iv_find_location;

    @BindView(R.id.btn_find_my_bike1)
    Button btn_find_my_bike1;
    @BindView(R.id.btn_find_my_bike2)
    Button btn_find_my_bike2;
    @BindView(R.id.iv_find_my_bike)
    ImageView iv_find_my_bike;

    @BindView(R.id.btn_anti_thief1)
    Button btn_anti_thief1;
    @BindView(R.id.btn_anti_thief2)
    Button btn_anti_thief2;
    @BindView(R.id.iv_anti_thief)
    ImageView iv_anti_thief;
    @BindView(R.id.iv_anti_thief_sub)
    ImageView iv_anti_thief_sub;
    @BindView(R.id.tv_anti_thief)
    TextView tv_anti_thief;

    //Local variables
    HomeViewModel viewModel;

    final private CompositeSubscription subscriptions = new CompositeSubscription();
    boolean isTurnOnAntiThief = true;


    @Override
    protected void injectComponent(DIComponent component) {
        component.inject(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new HomeViewModel(this);
        viewModel.setSmsManager(smsManager);

        //Check for first set up
        if (sharedPreferences.getString(GlobalConstant.DEVICE_PHONE_NUMBER_KEY, "").equals("")) {
            Log.d("Home", "Bi null roi");
        }

        //Button turn off engine cheat
        subscriptions.add(RxView.touches(btn_turn_off_engine1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
            }
        }));

        subscriptions.add(RxView.touches(btn_turn_off_engine2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_turn_off_engine, motionEvent);
            }
        }));


        //Button find location cheat
        subscriptions.add(RxView.touches(btn_find_location1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_location, motionEvent);
            }
        }));

        subscriptions.add(RxView.touches(btn_find_location2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_location, motionEvent);
            }
        }));


        //Button find my bike cheat
        subscriptions.add(RxView.touches(btn_find_my_bike1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
            }
        }));

        subscriptions.add(RxView.touches(btn_find_my_bike2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                viewModel.updateViewAfterClick(iv_find_my_bike, motionEvent);
            }
        }));


        //Button anti thief cheat
        subscriptions.add(RxView.touches(btn_anti_thief1).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                isTurnOnAntiThief = viewModel.updateAntiThiefView(iv_anti_thief,iv_anti_thief_sub,
                        tv_anti_thief,motionEvent,isTurnOnAntiThief);
            }
        }));

        subscriptions.add(RxView.touches(btn_anti_thief2).subscribe(new Action1<MotionEvent>() {
            @Override
            public void call(MotionEvent motionEvent) {
                isTurnOnAntiThief = viewModel.updateAntiThiefView(iv_anti_thief,iv_anti_thief_sub,
                        tv_anti_thief,motionEvent,isTurnOnAntiThief);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
        subscriptions.unsubscribe();
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private void showConfigPhoneDialog() {
        Dialog configPhoneDialog = new Dialog(this);
        configPhoneDialog.setContentView(R.layout.dialog_config_device_number);
        configPhoneDialog.show();
    }

    //----------------------------------------------------------------------------------------------
    //Event for config device number dialog
    //----------------------------------------------------------------------------------------------
    @Override
    public void btnQuitClicked(@NonNull View view) {
        this.finish();
    }

    @Override
    public void btnNextClicked(@NonNull View view) {

    }
}
