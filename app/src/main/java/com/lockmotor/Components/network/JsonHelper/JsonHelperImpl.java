package com.lockmotor.Components.network.JsonHelper;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lockmotor.Components.network.base.CallbackListener;

import java.lang.reflect.Type;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by VietHoa on 06/10/15.
 */
public class JsonHelperImpl implements JsonHelper {
    private CallbackListener myCallback;
    private Type resultType;
    private Context context;

    @Inject
    Gson mGsonParser;

    @Override
    public void setThreadBeRunOn(Context activityOrApplicationContext) {
        this.context = activityOrApplicationContext;
    }

    @Override
    public void setTypeAndCallback(Type resultType, CallbackListener callbackListener) {
        this.resultType = resultType;
        this.myCallback = callbackListener;
    }

    @Override
    public void success(JsonElement jsonElement, Response response) {
        if (jsonElement == null) {
            performResponse(null, new Exception("No data response"));
            return;
        }

        //No type of data
        if (resultType == null) {
            performResponse(jsonElement, null);
            return;
        }

        //Parse json to the data type
        Object object = null;
        try {

            object = mGsonParser.fromJson(jsonElement, resultType);
        } catch (Exception ex) {
            performResponse(null, ex);
            return;
        }
        if (object == null) {
            performResponse(null, new Exception("Receive empty data"));
            return;
        }

        Class objectClass = object.getClass();
        performResponse(objectClass.cast(object), null);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (retrofitError.isNetworkError()) {
            performResponse(null, new Exception("No network connection"));
            return;
        }

        try {
            String error = retrofitError.getCause().getMessage();
            performResponse(null, new Exception(error));
        } catch (Exception e) {
            performResponse(null, e);
        }
    }

    protected void performResponse(final Object response, final Exception e) {
        if (myCallback == null)
            return;

        if (!(context instanceof Activity)) {
            myCallback.onDone(response, e);
            return;
        }

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myCallback.onDone(response, e);
            }
        });
    }
}
