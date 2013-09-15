package com.honey.activity.history;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.EntryCommand;
import com.honey.R;
import com.honey.activity.BaseActivity;
import com.honey.common.Util;

import org.json.JSONObject;

import java.util.Date;

public class EditEntryActivity extends BaseActivity {

    protected EntryCommand entry;

    protected EditText txtAmount;
    protected EditText txtDate;
    protected EditText txtNotes;
    protected TextView txtPayee;

    protected String notes;
    protected Date date;
    protected String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        if (savedInstanceState != null) {
            entry = (EntryCommand)savedInstanceState.getSerializable("entry");
        } else {
            entry = (EntryCommand)getIntent().getSerializableExtra("entry");
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
        txtPayee = (TextView)findViewById(R.id.txtPayee);
        txtAmount = (EditText)findViewById(R.id.txtAmount);
        txtDate = (EditText)findViewById(R.id.txtDate);
        txtNotes = (EditText)findViewById(R.id.txtNotes);

        //when entry == null, this is potentially a call from AddActivity
        if (entry != null && entry.getId() != null) {
            txtPayee.setText(entry.getPayee().getName());
            txtNotes.setText(entry.getNotes());
            txtDate.setText(formatter.format(entry.getDate()));
            txtAmount.setText(entry.getAmount());
        }
    }

    protected void attemptSave() {
        boolean hasErrors = hasErrors();

        if (!hasErrors) {
            EntryCommand newEntry = new EntryCommand();
            newEntry.setPayee(entry.getPayee());
            newEntry.setId(entry.getId());

            //these are the editable data
            newEntry.setNotes(notes);
            newEntry.setDate(date);
            newEntry.setAmount(amount);

            saveData(newEntry);
        }
    }

    protected boolean hasErrors() {
        boolean rval = false;

        notes = txtNotes.getText().toString().trim();
        if (notes.length() > 120) {
            txtNotes.setError(getString(R.string.error_note_length_exceeded));
            rval = true;
        }

        try {
            date = new Date(txtDate.getText().toString().trim());
        } catch (Exception e) {
            txtDate.setError(getString(R.string.error_invalid_date));
            rval = true;
        }

        try {
            Double.parseDouble(txtAmount.getText().toString().trim());
            amount = txtAmount.getText().toString().trim();
        } catch (Exception e) {
            txtAmount.setError(getString(R.string.error_invalid_amount));
            rval = true;
        }


        return rval;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_entry, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_entry:
                attemptSave();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("entry", entry);
        super.onSaveInstanceState(outState);
    }

    protected void saveData(EntryCommand entry) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.url_save_entry);
        JSONObject obj = Util.toJsonObject(entry);

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
