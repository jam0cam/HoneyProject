package com.honey.common;

import android.app.Application;

/**
 * Created by jitse on 8/13/13.
 */
public class MyApp extends Application {
    private String userId = "9";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
