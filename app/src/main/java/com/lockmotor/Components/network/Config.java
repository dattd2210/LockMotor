package com.lockmotor.Components.network;

import com.lockmotor.BuildConfig;

/**
 * Created by Tran Dinh Dat on 3/8/2016.
 */
public class Config {
    public static final String DEVELOPMENT_SERVER = "http://api.tankfind.ssf.vn";
    public static final String PRODUCTION_SERVER = "http://api.tankfind.ssf.vn";
    public static final String AUTHORIZATION_KEY = "token";

    public static final int DEVELOPMENT = 1;
    public static final int PRODUCTION = 2;

    public static String getBaseUrl() {
        StringBuilder builder = new StringBuilder();
        switch (getAppEnvironment()) {
            case DEVELOPMENT:
                builder.append(DEVELOPMENT_SERVER);
                break;

            case PRODUCTION:
                builder.append(PRODUCTION_SERVER);
                break;
        }
        return builder.toString();
    }

    public static int getAppEnvironment() {
        if (BuildConfig.DEBUG)
            return DEVELOPMENT;

        return PRODUCTION;
    }
}
