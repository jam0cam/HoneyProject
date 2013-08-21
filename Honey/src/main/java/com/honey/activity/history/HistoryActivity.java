package com.honey.activity.history;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.EntryCommand;
import com.honey.R;
import com.honey.activity.BaseActivity;
import com.honey.common.Util;

import org.json.JSONArray;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity {

    private String userId = "1";
    private ArrayList<EntryCommand> entries;
    private ArrayList<String> uniquePayees;

    private int animationTime;
    private View mProgressView;
    private View mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (savedInstanceState != null) {
            entries = (ArrayList<EntryCommand>)savedInstanceState.getSerializable("entries");
            uniquePayees = (ArrayList<String>)savedInstanceState.getSerializable("uniquePayees");
        }


        animationTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mMainView = findViewById(R.id.lvEntries);
        mProgressView = findViewById(R.id.layoutProgress);


        if (entries == null) {
            Util.showProgress(true, mProgressView, mMainView, animationTime);
            fetchEntries();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("entries", entries);
        outState.putSerializable("uniquePayees", uniquePayees);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void fetchEntries() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.url_get_entries) + userId;

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //if it comes back here, that means this is a valid user
                EntryCommand[] entriesArray = Util.fromJSON(response, EntryCommand[].class);

                if (entriesArray.length <= 0) {
                    entries = new ArrayList<EntryCommand>();
                    uniquePayees = new ArrayList<String>();
                    return;
                }

                entries = new ArrayList<EntryCommand>();
                uniquePayees = new ArrayList<String>();
                String currentPayee = entriesArray[0].getPayee().getName();
                for (EntryCommand e : entriesArray) {
                    entries.add(e);
                    if(!currentPayee.equals(e.getPayee().getName())) {
                        uniquePayees.add(currentPayee);
                        currentPayee = e.getPayee().getName();
                    }
                }
                uniquePayees.add(currentPayee);
                initiateFragments();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(request);
    }

    private void initiateFragments() {
        if (uniquePayees == null || uniquePayees.isEmpty())
            return;

        if (mProgressView != null) {
            Util.showProgress(false, mProgressView, mMainView, animationTime);
        }

        ArrayList<EntryCommand> entryGroup = new ArrayList<EntryCommand>();
        String currentPayee = entries.get(0).getPayee().getName();
        for(EntryCommand e : entries) {
            if(!currentPayee.equals(e.getPayee().getName())) {
                currentPayee = e.getPayee().getName();
                addFragment(entryGroup);
                entryGroup = new ArrayList<EntryCommand>();
            }

            entryGroup.add(e);
        }

        //add the final group
        addFragment(entryGroup);
    }

    private void addFragment(ArrayList<EntryCommand> entryList) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        HistoryItemFragment fragment = new HistoryItemFragment();
        fragment.setRetainInstance(true);
        fragment.setEntries(entryList);
        transaction.add(R.id.lvEntries, fragment, entryList.get(0).getPayee().getName());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

}
