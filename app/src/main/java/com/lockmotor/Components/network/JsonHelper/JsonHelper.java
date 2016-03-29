package com.lockmotor.Components.network.JsonHelper;

import android.content.Context;

import com.lockmotor.Components.network.base.CallbackListener;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

import retrofit.Callback;

/**
 * Created by VietHoa on 24/01/16.
 */
public interface JsonHelper extends Callback<JsonElement> {

    void setThreadBeRunOn(Context activityOrApplicationContext);

    void setTypeAndCallback(Type resultType, CallbackListener callbackListener);
}
