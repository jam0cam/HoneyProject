package com.honey.activity.history;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

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

public class HistoryActivity extends BaseActivity implements HistoryItemFragment.EventListener{

    private String userId = "1";
    private ArrayList<EntryCommand> entries;
    private ArrayList<String> uniquePayees;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //if it is specified that history should reload, then this should reload because data could've changed.
        if (savedInstanceState != null) {
            entries = (ArrayList<EntryCommand>)savedInstanceState.getSerializable("entries");
            uniquePayees = (ArrayList<String>)savedInstanceState.getSerializable("uniquePayees");
        }

        if (entries == null) {
            pd = ProgressDialog.show(this,"Fetching history","Loading...");
            fetchEntries();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("entries", entries);
        outState.putSerializable("uniquePayees", uniquePayees);

        super.onSaveInstanceState(outState);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                fetchEntries();
            }
        }
    }

    private void initiateFragments() {
        if (uniquePayees == null || uniquePayees.isEmpty())
            return;

        if (pd.isShowing()) {pd.dismiss();}

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

    @Override
    public void dataChanged() {
        //restart the current activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
