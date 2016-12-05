package com.lockmotor.global;

import com.lockmotor.BuildConfig;

/**
 * Created by Tran Dinh Dat on 3/8/2016.
 */
public class Config {
    public static final String DEVELOPMENT_SERVER = "http://27.0.15.133:8080/";
    public static final String PRODUCTION_SERVER = "http://27.0.15.133:8080/";

    public static String getBaseUrl() {
        if (BuildConfig.DEBUG)
            return DEVELOPMENT_SERVER;

        return PRODUCTION_SERVER;
    }
}
