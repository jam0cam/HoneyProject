package com.honey.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by jitse on 8/13/13.
 */
public class MyApp extends Application {

    public static final String PREFS_NAME = "honeyPrefs";
    private static final String PREF_USERID = "username";

    private String userId = null;

    public String getUserId() {

        if (!TextUtils.isEmpty(userId)) {
            return userId;
        } else {
            SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            userId = pref.getString(PREF_USERID, null);
            if (TextUtils.isEmpty(userId)){
                userId = null;
            }
        }

        return userId;
    }

    public boolean isLoggedIn() {
        if (userId == null) {
            return false;
        } else {
            return true;
        }
    }

    public void logout() {
        userId = null;
        saveUser("");
    }

    public void saveUser(String userId){

        this.userId = userId;
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_USERID, userId)
                .commit();
    }
}
