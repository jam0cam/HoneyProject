package com.honey.activity.payee;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.Payee;
import com.honey.R;
import com.honey.activity.BaseActivity;
import com.honey.common.MyApp;
import com.honey.common.Util;

import org.json.JSONObject;

import java.util.ArrayList;

public class AddPayeeActivity extends BaseActivity {

    protected ArrayList<Payee> existingPayees;

    // UI references.
    protected EditText txtName;
    protected EditText txtAccount;
    protected EditText txtPhone;
    protected EditText txtUrl;
    protected EditText txtNotes;
    protected NumberPicker np;
    protected Switch sw;

    protected String name;
    protected String account;
    protected String phone;
    protected String url;
    protected String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            existingPayees = (ArrayList<Payee>)savedInstanceState.getSerializable("payees");
        } else {
            existingPayees = (ArrayList<Payee>)getIntent().getSerializableExtra("payees");
        }
        setContentView(R.layout.activity_add_payee);

        txtName = (EditText) findViewById(R.id.name);
        txtAccount= (EditText) findViewById(R.id.accountNumber);
        txtPhone = (EditText) findViewById(R.id.phone);
        txtUrl = (EditText) findViewById(R.id.url);
        txtNotes = (EditText) findViewById(R.id.notes);
        np = (NumberPicker) findViewById(R.id.np);
        np.setMaxValue(28);
        np.setMinValue(1);

        sw = (Switch)findViewById(R.id.sw);
        sw.setChecked(false);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("payees", existingPayees);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.payee, menu);
        return true;
    }

    protected void attemptSave() {
        boolean hasErrors = hasErrors();

        if (!hasErrors) {
            Payee payee = new Payee();
            payee.setNotes(notes);
            payee.setName(name);
            payee.setPhone(phone);
            payee.setUrl(url);
            payee.setAccountNumber(account);
            payee.setUserId(((MyApp) getApplication()).getUserId());
            if (sw.isChecked()) {
                payee.setNotifyOn(true);
                payee.setNotifyDay(Integer.toString(np.getValue()));
            }
            saveData(payee);
        }
    }

    protected boolean hasErrors() {
        boolean rval = false;

        notes = txtNotes.getText().toString().trim();
        name = txtName.getText().toString().trim();
        phone = txtPhone.getText().toString().trim();
        url = txtUrl.getText().toString().trim();
        account = txtAccount.getText().toString().trim();

        if (notes.length() > 120) {
            txtNotes.setError(getString(R.string.error_note_length_exceeded));
            rval = true;
        }

        if (TextUtils.isEmpty(name)) {
            txtName.setError(getString(R.string.error_field_required));
            rval = true;
        } else {
            //check that the payee doesn't already exists
            for (Payee p: existingPayees) {
                if (p.getName().toLowerCase().equals(name.toLowerCase())) {
                    txtName.setError(getString(R.string.error_payee_exists));
                    rval = true;
                    break;
                }
            }
        }

        return rval;
    }

    protected void saveData(Payee payee) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.url_save_payee);
        JSONObject obj = Util.toJsonObject(payee);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                saveSuccessful();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                saveFailed();
            }
        });
        queue.add(request);
    }

}
