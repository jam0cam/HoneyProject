package com.honey.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.honey.R;

/**
 * Created by jitse on 8/18/13.
 */
public class BaseActivity extends Activity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_payee_list:
                Intent intent = new Intent(this, PayeeListActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
