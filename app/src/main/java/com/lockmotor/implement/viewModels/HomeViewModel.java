package com.lockmotor.implement.viewModels;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.lockmotor.R;

/**
 * Created by trandinhdat on 8/9/16.
 */
public class HomeViewModel {

    private SmsManager smsManager;
    private Activity context;

    public HomeViewModel(Activity context) {
        this.context = context;
    }

    public void setSmsManager(SmsManager smsManager)
    {
        this.smsManager = smsManager;
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------

    /**
     * Update view when click on button
     * @param imageView button lozenge
     * @param event user event
     */
    public void updateViewAfterClick(ImageView imageView, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                imageView.setImageResource(R.mipmap.ic_btn_turn_off);
                break;
            case MotionEvent.ACTION_UP:
                imageView.setImageResource(R.mipmap.ic_btn_normal);
                break;
        }
    }

    /**
     * Update view when click on button anti thief
     * @param iv_main button lozenge
     * @param iv_icon icon on button
     * @param textView textview on button
     * @param event user event
     * @param isTurnOnAntiThief check if anti thief turn on
     * @return change state of anti thief
     */
    public boolean updateAntiThiefView(ImageView iv_main, ImageView iv_icon, TextView textView, MotionEvent event, boolean isTurnOnAntiThief) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTurnOnAntiThief) {
                isTurnOnAntiThief = false;
                iv_main.setImageResource(R.mipmap.ic_btn_normal);
                iv_icon.setImageResource(R.mipmap.ic_turn_on_anti_thief);
                textView.setText(R.string.tv_turn_on_anti_thief);
            } else {
                isTurnOnAntiThief = true;
                iv_main.setImageResource(R.mipmap.ic_btn_turn_off);
                iv_icon.setImageResource(R.mipmap.ic_turn_off_anti_thief);
                textView.setText(R.string.tv_turn_off_anti_thief);
            }
        }

        return isTurnOnAntiThief;
    }


    /**
     * Make a call to device to turn off engine
     * @param event user event
     */
    public void turnOffEngine(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //Listen call state: roll back to activity when call is end
            PhoneCallListener phoneListener = new PhoneCallListener();
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

            //start a call
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:0377778888"));

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("CALL_CANCEL","Dont have permission");
                return;
            }
            context.startActivity(callIntent);
        }
    }

    /** TODO not done
     * Send message to device and listen server to receive location of device and show it to map
     * @param event user event
     */
    public void findLocation(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            try {
                smsManager.sendTextMessage("phone number", null, "content", null, null);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /** TODO change phone number and content
     * Send message to device to make an sound on device
     * @param event user event
     */
    public void findMyBike(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            try {
                smsManager.sendTextMessage("phone number", null, "content", null, null);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /** TODO change phone number and content, receive data from server
     * Send message to device to turn off anti thief, show noti to user after sent
     * @param event user event
     */
    public void turnOffAntiThief(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            try {
                smsManager.sendTextMessage("phone number", null, "content", null, null);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /** TODO change phone number and content, receive data from server
     * Send message to device to turn on anti thief, show noti to user after sent
     * @param event user event
     */
    public void turnOnAntiThief(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            try {
                smsManager.sendTextMessage("phone number", null, "content", null, null);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //Listen phone state
    //----------------------------------------------------------------------------------------------
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "PHONE_STATE";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");
                    // restart app
                    Intent i = context.getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    context.getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }


}
