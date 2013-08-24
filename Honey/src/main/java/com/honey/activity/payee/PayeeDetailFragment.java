package com.honey.activity.payee;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.DeleteObject;
import com.finance.model.Payee;
import com.honey.R;
import com.honey.common.Util;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jitse on 8/16/13.
 */
public class PayeeDetailFragment extends Fragment {

    private ArrayList<Payee> payees;

    private Payee payee;
    private TextView txtName;
    private TextView txtAccount;
    private TextView txtPhone;
    private TextView txtNotes;
    private TextView txtUrl;
    private TextView txtNotifyDay;
    private ImageView imgAlarm;

    private PayeeListActivity parentActivity;

    private ImageButton btnEdit;
    private ImageButton btnRemove;

    public PayeeDetailFragment(){}

    public PayeeDetailFragment(PayeeListActivity activity, ArrayList<Payee> payees) {
        parentActivity = activity;
        this.payees = payees;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payee_detail, container, false);

        txtName = (TextView)view.findViewById(R.id.txtName);
        txtPhone = (TextView)view.findViewById(R.id.txtPhone);
        txtAccount = (TextView)view.findViewById(R.id.txtAccount);
        txtNotes = (TextView)view.findViewById(R.id.txtNotes);
        txtUrl = (TextView)view.findViewById(R.id.txtUrl);
        imgAlarm = (ImageView) view.findViewById(R.id.imgAlarm);
        txtNotifyDay = (TextView)view.findViewById(R.id.txtNotifyDay);
        
        btnEdit = (ImageButton)view.findViewById(R.id.btnEdit);
        btnRemove = (ImageButton)view.findViewById(R.id.btnRemove);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditPayeeActivity.class);
                intent.putExtra("payee", payee);
                intent.putExtra("payees", payees);
                startActivityForResult(intent, 1);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePayee();
            }
        });

        payee = ((ISelectedPayee)getActivity()).getSelectedPayee();
        reDraw();
        return view;
    }

    private void removePayee() {
        RequestQueue queue = Volley.newRequestQueue(parentActivity);
        String url = parentActivity.getResources().getString(R.string.url_delete_payee);
        DeleteObject entry = new DeleteObject(payee.getId());
        JSONObject obj = Util.toJsonObject(entry);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                removeSuccessful(payee.getId());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                saveFailed();
            }
        });

        queue.add(request);
    }


    private void removeSuccessful(String tag) {
        Toast toast = Toast.makeText(parentActivity, "Payee deleted.", Toast.LENGTH_SHORT);
        toast.show();

        parentActivity.softRestartActivity();
    }

    private void saveFailed() {
        Toast toast = Toast.makeText(parentActivity, "Failed to delete payee.", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK){
            parentActivity.restartActivity();
        }
    }

    public void reDraw() {
        if (txtName != null) {
            txtName.setText(payee.getName());
            txtPhone.setText(payee.getPhone());
            txtAccount.setText(payee.getAccountNumber());
            txtNotes.setText(payee.getNotes());
            txtUrl.setText(payee.getUrl());
            if (payee.isNotifyOn()) {
                imgAlarm.setImageResource(R.drawable.alarm_on);
                txtNotifyDay.setText("*You will be notified on the " + payee.getNotifyDay() + " of each month.");
                txtNotifyDay.setVisibility(View.VISIBLE);
            } else {
                imgAlarm.setImageResource(R.drawable.alarm_off);
                txtNotifyDay.setVisibility(View.GONE);
            }
        }
    }
}
