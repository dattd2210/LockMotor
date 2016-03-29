package com.lockmotor.Components.managers.UserSessionManager;

import com.lockmotor.Features.Models.User;

/**
 * Created by VietHoa on 17/01/16.
 */
public interface UserSessionManager {

    void clearAllSavedUserData();

    String getCurrentAccessToken();

    void saveCurrentUser(User user);

    User getCurrentUser();

    Number getCurrentUserID();
}
