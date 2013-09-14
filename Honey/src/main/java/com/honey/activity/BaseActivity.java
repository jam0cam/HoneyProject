package com.honey.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.honey.R;
import com.honey.activity.history.HistoryActivity;
import com.honey.activity.history.RecentHistoryActivity;
import com.honey.activity.payee.PayeeListActivity;
import com.honey.common.MyApp;

import java.text.SimpleDateFormat;

/**
 * Created by jitse on 8/18/13.
 */
public class BaseActivity extends Activity {
    protected ProgressDialog pd;
    protected SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    protected SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");


    protected void showToastError(){
        if (pd != null && pd.isShowing()){pd.dismiss();}
        Toast toast = Toast.makeText(this, "Error.", Toast.LENGTH_SHORT);
        toast.show();
    }

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
        }
        return super.onOptionsItemSelected(item);
    }

    protected void saveFailed() {
        Toast toast = Toast.makeText(this, "Failed to save.", Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void saveSuccessful() {
        Toast toast = Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT);
        toast.show();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onResume() {
        if (!((MyApp)getApplication()).isLoggedIn() && !(this instanceof LoginActivity)){
            finish();
        }
        super.onResume();
    }

    public void restartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
