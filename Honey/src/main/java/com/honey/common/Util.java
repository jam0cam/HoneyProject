package com.honey.common;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by jitse on 8/12/13.
 */
public class Util {

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }


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

    public static String getMonthName(int i) {
        if (i == 1 || i == 13) {
            return "January";
        } else if (i == 2) {
            return "February";
        } else if (i == 3) {
            return "March";
        } else if (i == 4) {
            return "April";
        } else if (i == 5) {
            return "May";
        } else if (i == 6) {
            return "June";
        } else if (i == 7) {
            return "July";
        } else if (i == 8) {
            return "August";
        } else if (i == 9) {
            return "September";
        } else if (i == 10) {
            return "October";
        } else if (i == 11) {
            return "November";
        } else if (i == 12 || i == 0) {
            return "December";
        }

        return "???";
    }
}
