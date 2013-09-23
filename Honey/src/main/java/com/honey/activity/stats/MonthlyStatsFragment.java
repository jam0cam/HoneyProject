package com.honey.activity.stats;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.MobileMonthlyExpense;
import com.honey.R;
import com.honey.common.MyApp;
import com.honey.common.Util;

import org.json.JSONObject;

public class MonthlyStatsFragment extends Fragment {
    private String userId;
    private MobileMonthlyExpense monthlyExpense;

    private ListView lstMonthlyStats;
    protected ProgressDialog pd;

    private View rootView;
    private StatsType statsType;

    public enum StatsType{
        MONTHLY, PAYEE
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_monthly_stats, container, false);
        } else {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }

        lstMonthlyStats = (ListView)rootView.findViewById(R.id.lstMonthlyStats);
        userId = ((MyApp)getActivity().getApplication()).getUserId();


        if (savedInstanceState != null) {
            monthlyExpense = (MobileMonthlyExpense)savedInstanceState.get("monthlyExpense");
        }
        if (monthlyExpense == null) {
//            if (pd == null || !pd.isShowing()){
//                pd = ProgressDialog.show(getActivity(), "Stats", "Calculating stats.");
//            }
            fetchMonthlyExpense();
        } else {
            fetchStatsSucccess();
        }

        return rootView;
    }

    private void fetchStatsSucccess(){
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.monthly_expense_grand_total_row, null, false);
        ((TextView)footerView.findViewById(R.id.txtGrandTotal)).setText(monthlyExpense.getFormattedGrandTotal());
        lstMonthlyStats.addFooterView(footerView);

        lstMonthlyStats.setAdapter(new MonthlyExpenseListAdapter(getActivity(), monthlyExpense.getMonthlyExpense()));
    }

    private void fetchMonthlyExpense(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "";
        if (statsType == StatsType.MONTHLY) {
            url = getResources().getString(R.string.url_get_monthly_expense);
        } else if (statsType == StatsType.PAYEE) {
            url = getResources().getString(R.string.url_get_payee_expense);
        }

        url = url.replace("{userId}", userId);
        monthlyExpense = null;

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //if it comes back here, that means this is a valid user
                monthlyExpense = Util.fromJSON(response, MobileMonthlyExpense.class);
                fetchStatsSucccess();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                showToastError();
            }
        });

        queue.add(request);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("monthlyExpense", monthlyExpense);
        super.onSaveInstanceState(outState);
    }


    protected void showToastError(){
        if (pd != null && pd.isShowing()){pd.dismiss();}
        Toast toast = Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setStatsType(StatsType statsType) {
        this.statsType = statsType;
    }
}
