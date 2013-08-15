package com.honey.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by jitse on 8/12/13.
 */
public class Util {


    public static <T> T fromJSON(Object object, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object o = mapper.readValue(object.toString(), clazz);
            return (T)o;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject toJsonObject(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(o);
            return new JSONObject(s);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, final View progressView, final View mainView, int animationTime) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            progressView.setVisibility(View.VISIBLE);
            progressView.animate()
                    .setDuration(animationTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mainView.setVisibility(View.VISIBLE);
            mainView.animate()
                    .setDuration(animationTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

//
//    public static <T> T fromJSONArray(JSONArray jsonArray, Class<T> clazz) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            Object o = mapper.readValue(jsonArray.toString(), clazz);
//            return (T) o;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
