package com.lockmotor.implement.views.home;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.lockmotor.R;

import butterknife.BindView;

/**
 * Created by trandinhdat on 8/9/16.
 */
public class ConfigDeviceNumberDialog extends Dialog {

    @BindView(R.id.btn_config_phone_number_cancel)
    Button btn_quit;
    @BindView(R.id.btn_config_phone_number_next)
    Button btn_next;

    private EventHandler listener;

    public ConfigDeviceNumberDialog(Context context) {
        super(context);

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnQuitClicked(v);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.btnNextClicked(v);
            }
        });
    }


    //----------------------------------------------------------------------------------------------
    //Event listener
    //----------------------------------------------------------------------------------------------
    interface EventHandler {
        void btnQuitClicked(@NonNull View view);

        void btnNextClicked(@NonNull View view);
    }

    public void setListener(EventHandler listener) {
        this.listener = listener;
    }
}
