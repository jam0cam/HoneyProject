package com.honey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.Payee;
import com.honey.R;
import com.honey.common.Util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPayeeListActivity extends Activity {

    private List<Payee> payees = new ArrayList<Payee>();
    private List<String> payeeStringList = new ArrayList<String>();
    private View mProgressView;
    private View mMainView;
    private ListView mPayeeView;
    private int animationTime;
    private ArrayAdapter<String> adapter;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payee_list);
        animationTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mPayeeView = (ListView) findViewById(R.id.lvPayee);
        mProgressView = findViewById(R.id.payee_progress_layout);
        mMainView = findViewById(R.id.payee_main_layout);

        //userId = ((MyApp)this.getApplication()).getUserId();
        userId = "1";

        fetchPayees();

        mPayeeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Payee payee = payees.get(position);
                goToPayee(payee);
            }

        });
    }

    private void goToPayee(Payee payee) {
        Intent intent = new Intent(this, ViewPayeeActivity.class);
        intent.putExtra("payee", payee);
        startActivity(intent);
    }

    private void fetchPayees() {

        Util.showProgress(true, mProgressView, mMainView, animationTime);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.url_get_payee) + userId;

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //if it comes back here, that means this is a valid user
                Util.showProgress(false, mProgressView, mMainView, animationTime);
                Payee[] payeesArray = Util.fromJSON(response, Payee[].class);
                payees = Arrays.asList(payeesArray);
                fillList();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Util.showProgress(false, mProgressView, mMainView, animationTime);
            }
        });

        queue.add(request);
    }

    private void fillList() {
        payeeStringList = new ArrayList<String>();
        for(Payee p : payees) {
            payeeStringList.add(p.getName());
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, payeeStringList);
        mPayeeView.setAdapter(adapter);
    }
}
