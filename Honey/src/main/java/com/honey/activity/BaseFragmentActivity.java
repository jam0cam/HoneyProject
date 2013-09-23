package com.honey.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.honey.R;
import com.honey.activity.history.HistoryActivity;
import com.honey.activity.history.RecentHistoryActivity;
import com.honey.activity.payee.PayeeListActivity;
import com.honey.activity.stats.StatsActivity;
import com.honey.common.MyApp;

public class BaseFragmentActivity extends FragmentActivity {

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_payee_list:
                Intent intent = new Intent(this, PayeeListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                Intent historyIntent = new Intent(this, HistoryActivity.class);
                startActivity(historyIntent);
                return true;
            case R.id.action_recent_history:
                Intent recentHistoryIntent = new Intent(this, RecentHistoryActivity.class);
                startActivity(recentHistoryIntent);
                return true;
            case R.id.action_logout:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                ((MyApp)getApplication()).logout();
                startActivity(loginIntent);
                return true;
            case R.id.action_more_apps:
                Intent moreAppsIntent = new Intent(this, MoreAppsActivity.class);
                startActivity(moreAppsIntent);
                return true;
            case R.id.action_stats:
                Intent statsIntent = new Intent(this, StatsActivity.class);
                startActivity(statsIntent);
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
