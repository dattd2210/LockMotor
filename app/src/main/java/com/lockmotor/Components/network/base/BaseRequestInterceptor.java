package com.lockmotor.Components.network.base;

import com.lockmotor.Components.managers.UserSessionManager.UserSessionManager;
import com.lockmotor.Components.network.Config;
import com.lockmotor.BaseAppication.utils.StringUtil.StringUtils;

import javax.inject.Inject;

import retrofit.RequestInterceptor;

/**
 * Created by VietHoa on 18/01/16.
 */
public class BaseRequestInterceptor implements RequestInterceptor {

    @Inject
    UserSessionManager mUserSessionManager;
    @Inject
    StringUtils  mStringUtils;

    @Override
    public void intercept(RequestFacade request) {
        //request.addHeader("Content-Type", "application/json");
        //request.addHeader("Content-Type","multipart/form-data");
        String accessToken = mUserSessionManager.getCurrentAccessToken();
        if (mStringUtils.isNotNull(accessToken)) {
            request.addHeader(Config.AUTHORIZATION_KEY, accessToken);
            request.addQueryParam(Config.AUTHORIZATION_KEY, accessToken);
        }
    }
}
