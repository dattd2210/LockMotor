package com.lockmotor.global;

import com.lockmotor.BuildConfig;

/**
 * Created by Tran Dinh Dat on 3/8/2016.
 */
public class Config {
    public static final String DEVELOPMENT_SERVER = "";
    public static final String PRODUCTION_SERVER = "";
    public static final String AUTHORIZATION_KEY = "";

    public static final int DEVELOPMENT = 1;
    public static final int PRODUCTION = 2;

    public static String getBaseUrl() {
        if (BuildConfig.DEBUG)
            return DEVELOPMENT_SERVER;

        return PRODUCTION_SERVER;
    }
}
