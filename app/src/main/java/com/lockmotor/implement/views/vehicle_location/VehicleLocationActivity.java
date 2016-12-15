package com.lockmotor.implement.views.vehicle_location;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.lockmotor.R;
import com.lockmotor.base.utils.DeviceUtils;
import com.lockmotor.global.GlobalConstant;
import com.lockmotor.global.LockMotorAPI;
import com.lockmotor.global.dagger.DIComponent;
import com.lockmotor.implement.LockMotorActivity;
import com.lockmotor.implement.models.InfoRequest;
import com.lockmotor.implement.models.InfoResponse;

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by trandinhdat on 12/7/16.
 */

public class VehicleLocationActivity extends LockMotorActivity implements OnMapReadyCallback {
    @Inject
    LockMotorAPI service;

    @BindView(R.id.iv_vl_back_press)
    ImageView btn_back;

    private GoogleMap googleMap;
    private MarkerOptions markerOptions;

    final private CompositeSubscription subscriptions = new CompositeSubscription();
    final Handler handler = new Handler();

    @Override
    protected void injectComponent(DIComponent component) {
        component.inject(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_vehicle_location);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LatLng lastPosition = new LatLng(0, 0);
        markerOptions = new MarkerOptions()
                .position(lastPosition)
                .title("Your Location!")
                .snippet("Lat: " + lastPosition.latitude + " - Long: " + lastPosition.longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set up rx java event
        initSubscription();
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscriptions.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
        subscriptions.unsubscribe();
    }

    private void initSubscription() {
        subscriptions.clear();
        //Button back press
        subscriptions.add(RxView.clicks(btn_back).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
                overridePendingTransition(R.anim.acitivity_in_from_left_to_right, R.anim.hold);
                sendEndRequest();
                handler.removeCallbacksAndMessages(null);
            }
        }));
    }
    //----------------------------------------------------------------------------------------------
    //Map handler
    //----------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.addMarker(markerOptions);

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);

        startUpdateLocation();
        showLoadingDialog();
    }

    public void moveMap(LatLng latLng) {
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //----------------------------------------------------------------------------------------------
    //Server listener
    //----------------------------------------------------------------------------------------------
    private void startUpdateLocation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InfoRequest param = new InfoRequest(GlobalConstant.decryptPhoneNumber());
                Call<InfoResponse> call = service.getInfo(param);
                call.enqueue(new Callback<InfoResponse>() {
                    @Override
                    public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                        if (!response.body().getStatus().equals("OK")) {
                            startUpdateLocation();
                            return;
                        }

                        if (!response.body().getMessage().equals("")
                                && !response.body().getMessage().equals("null_dattd2210")) {
                            startUpdateLocation();
                            moveMap(getLocation(response.body().getMessage()));
                            return;
                        }

                        startUpdateLocation();
                    }

                    @Override
                    public void onFailure(Call<InfoResponse> call, Throwable t) {
                        startUpdateLocation();
                    }
                });
//                handler.postDelayed(this, GlobalConstant.AUTO_CALL_API_TIME);

            }
        }, GlobalConstant.AUTO_CALL_API_TIME);
    }

    //----------------------------------------------------------------------------------------------
    //Function
    //----------------------------------------------------------------------------------------------
    private LatLng getLocation(String latlng) {
//        10.754948, 106.675332

        dismissLoadingDialog();
        double lat = Double.parseDouble(latlng.substring(0, latlng.indexOf(",")));
        double lng =Double.parseDouble(latlng.substring(latlng.indexOf(",") + 1));

        LatLng result = new LatLng(lat, lng);

        markerOptions.position(result);
        googleMap.clear();
        googleMap.addMarker(markerOptions);

        return result;
    }

    private void sendEndRequest(){
        DeviceUtils.sendSms(GlobalConstant.DEVICE_PHONE_NUMBER,GlobalConstant.CONTENT_END_FIND_LOCATION);
    }
}
