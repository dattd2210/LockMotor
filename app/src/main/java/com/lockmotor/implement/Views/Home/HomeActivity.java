package com.lockmotor.implement.Views.Home;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lockmotor.R;
import com.lockmotor.implement.LockMotorActivity;

/**
 * Created by Tran Dinh Dat on 3/26/2016.
 */
public class HomeActivity extends LockMotorActivity {

    RelativeLayout layout;

    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_home);
        layout = (RelativeLayout)findViewById(R.id.root_layout);

        final ImageView imageView = (ImageView) findViewById(R.id.iv_turn_off_engine);
        Button btn_1 = (Button)findViewById(R.id.btn_turn_off_engine1);
        btn_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });

        Button btn_2 = (Button)findViewById(R.id.btn_turn_off_engine2);
        btn_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });


        final ImageView imageView1 = (ImageView) findViewById(R.id.iv_anti_thief);
        Button btn_11 = (Button)findViewById(R.id.btn_anti_thief1);
        btn_11.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView1.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView1.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });

        Button btn_21 = (Button)findViewById(R.id.btn_anti_thief2);
        btn_21.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView1.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView1.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });


        final ImageView imageView2 = (ImageView) findViewById(R.id.iv_find_location);
        Button btn_12 = (Button)findViewById(R.id.btn_find_location1);
        btn_12.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView2.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView2.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });

        Button btn_22 = (Button)findViewById(R.id.btn_find_location2);
        btn_22.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView2.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView2.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });


        final ImageView imageView3 = (ImageView) findViewById(R.id.iv_find_my_bike);
        Button btn_13 = (Button)findViewById(R.id.btn_find_my_bike1);
        btn_13.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView3.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView3.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });

        Button btn_23 = (Button)findViewById(R.id.btn_find_my_bike2);
        btn_23.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView3.setImageResource(R.mipmap.ic_btn_turn_off);
                        break;
                    case MotionEvent.ACTION_UP:
                        imageView3.setImageResource(R.mipmap.ic_btn_normal);
                        break;
                }
                return true;
            }
        });
    }
}
