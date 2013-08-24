package com.honey.activity.history;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditEntryActivity extends BaseActivity {

    protected EntryCommand entry;

    protected EditText txtAmount;
    protected EditText txtDate;
    protected EditText txtNotes;
    protected TextView txtPayee;
    protected Button btnSave;
    protected SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

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
        btnSave = (Button)findViewById(R.id.btnSave);

        //when entry == null, this is potentially a call from AddEditActivity
        if (entry != null) {
            txtPayee.setText(entry.getPayee().getName());
            txtNotes.setText(entry.getNotes());
            txtDate.setText(formatter.format(entry.getDate()));
            txtAmount.setText(entry.getAmount());

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EntryCommand newEntry = new EntryCommand();
                    newEntry.setPayee(entry.getPayee());
                    newEntry.setId(entry.getId());

                    //these are the editable data
                    newEntry.setNotes(txtNotes.getText().toString());
                    newEntry.setDate(new Date(txtDate.getText().toString()));
                    newEntry.setAmount(txtAmount.getText().toString());
                    saveData(newEntry);
                }
            });
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_entry, menu);
        return true;
    }
    
}
