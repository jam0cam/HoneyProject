package com.honey.activity.pingpong;

import android.os.Bundle;
import android.view.Menu;

import com.honey.R;
import com.honey.activity.BaseActivity;

public class PPStatsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pp_stats);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ppstats, menu);
        return true;
    }
    
}
