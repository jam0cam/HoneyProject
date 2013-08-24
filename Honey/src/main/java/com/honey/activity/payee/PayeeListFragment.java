package com.honey.activity.payee;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.honey.common.MyApp;
import com.honey.common.Util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jitse on 8/15/13.
 */
public class PayeeListFragment extends Fragment {
    private ArrayList<Payee> payees;
    private List<String> payeeStringList = new ArrayList<String>();
    private ListView mPayeeView;
    private ArrayAdapter<String> adapter;
    private String userId;

    private OnItemSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payee_list, container, false);
        mPayeeView = (ListView) view.findViewById(R.id.lvPayee);

        userId = ((MyApp)this.getActivity().getApplication()).getUserId();

        //if the parent activity already have some payees, then no need to bother getting new ones
        payees = ((PayeeListActivity)getActivity()).getPayees();

        if (payees == null) {
            fetchPayees();
        } else {
            fillList();
        }

        mPayeeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Payee payee = payees.get(position);
                listener.onItemSelected(payee);
            }

        });
        return view;
    }

    public void fetchPayees() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = getResources().getString(R.string.url_get_payee) + userId;

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //if it comes back here, that means this is a valid user
                Payee[] payeesArray = Util.fromJSON(response, Payee[].class);

                payees = new ArrayList<Payee>();
                for (Payee p : payeesArray) {
                    payees.add(p);
                }
                fillList();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(request);
    }

    private void fillList() {
        listener.onFillingList(payees);

        payeeStringList = new ArrayList<String>();
        for(Payee p : payees) {
            payeeStringList.add(p.getName());
        }
        adapter = new ArrayAdapter<String>(mPayeeView.getContext(),android.R.layout.simple_list_item_1, payeeStringList);
        mPayeeView.setAdapter(adapter);
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(Payee payee);
        public void onFillingList(ArrayList<Payee> payees);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}


