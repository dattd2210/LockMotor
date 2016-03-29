package com.lockmotor.Components.network.base;

/**
 * Created by VietHoa on 05/10/15.
 */
public interface CallbackListener<T> {

    void onDone(T response, Exception exception);
}
