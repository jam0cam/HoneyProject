package com.honey.activity.history;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ExpandableListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.EntryCommand;
import com.honey.R;
import com.honey.activity.BaseActivity;
import com.honey.common.MyApp;
import com.honey.common.Util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecentHistoryActivity extends BaseActivity {
    private String userId;

    ArrayList<String> groupList = new ArrayList<String>();
    ArrayList<EntryCommand> entries;
    HashMap<String, List<EntryCommand>> entryCollection = new HashMap<String, List<EntryCommand>>();
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_history);

        userId = ((MyApp)getApplication()).getUserId();
        expListView = (ExpandableListView) findViewById(R.id.lstHistory);

        if (savedInstanceState != null) {
            entries = (ArrayList<EntryCommand>)savedInstanceState.getSerializable("entries");
            groupList = (ArrayList<String>)savedInstanceState.getSerializable("groupList");
            entryCollection = (HashMap<String, List<EntryCommand>>)savedInstanceState.getSerializable("entryCollection");
            linkAdapter();
        }

        if (entries == null) {
            pd = ProgressDialog.show(this, "Fetching history", "Loading...");
            fetchEntries();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("entries", entries);
        outState.putSerializable("groupList", groupList);
        outState.putSerializable("entryCollection", entryCollection);

        super.onSaveInstanceState(outState);
    }

    private void fetchEntries() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.url_get_recent_entries);
        url = url.replace("{userId}", userId);
        url = url.replace("{month}", "3");

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //if it comes back here, that means this is a valid user
                EntryCommand[] entriesArray = Util.fromJSON(response, EntryCommand[].class);

                groupList = new ArrayList<String>();
                entries = new ArrayList<EntryCommand>();

                if (entriesArray.length <= 0) {
                    return;
                }

                int currentMonth = entriesArray[0].getDate().getMonth();
                for (EntryCommand e : entriesArray) {
                    entries.add(e);
                    if(currentMonth != e.getDate().getMonth()) {
                        groupList.add(Util.getMonthName(currentMonth));
                        currentMonth = e.getDate().getMonth();
                    }
                }

                groupList.add(Util.getMonthName(currentMonth));
                createCollection();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showToastError();
            }
        });

        queue.add(request);
    }

    private void createCollection() {
        if (pd != null && pd.isShowing()) {pd.dismiss();}
        for (String s : groupList) {
            List<EntryCommand> tempList = new ArrayList<EntryCommand>();
            for (EntryCommand e : entries){
                if (s.equals(Util.getMonthName(e.getDate().getMonth()))){
                    tempList.add(e);
                }
            }

            entryCollection.put(s, tempList);
        }
        linkAdapter();
    }

    private void linkAdapter(){
        RecentHistoryListAdapter expListAdapter = new RecentHistoryListAdapter(this, groupList, entryCollection);
        expListView.setAdapter(expListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recent_history, menu);
        return true;
    }

}
