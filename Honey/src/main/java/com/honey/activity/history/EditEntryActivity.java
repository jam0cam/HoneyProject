package com.honey.activity.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private EntryCommand entry;

    private EditText txtAmount;
    private EditText txtDate;
    private EditText txtNotes;
    private TextView txtPayee;
    private Button btnSave;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        if (savedInstanceState != null) {
            entry = (EntryCommand)savedInstanceState.getSerializable("entry");
        } else {
            entry = (EntryCommand)getIntent().getSerializableExtra("entry");
        }

        txtPayee = (TextView)findViewById(R.id.txtPayee);
        txtAmount = (EditText)findViewById(R.id.txtAmount);
        txtDate = (EditText)findViewById(R.id.txtDate);
        txtNotes = (EditText)findViewById(R.id.txtNotes);
        btnSave = (Button)findViewById(R.id.btnSave);

        txtPayee.setText(entry.getPayee().getName());
        txtNotes.setText(entry.getNotes());
        txtDate.setText(formatter.format(entry.getDate()));
        txtAmount.setText(entry.getAmount());

        getActionBar().setDisplayHomeAsUpEnabled(true);

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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("entry", entry);

        super.onSaveInstanceState(outState);
    }


    private void saveData(EntryCommand entry) {
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

    private void saveFailed() {
        Toast toast = Toast.makeText(this, "Failed to save.", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void saveSuccessful() {
        Toast toast = Toast.makeText(this, "Entry Saved.", Toast.LENGTH_SHORT);
        toast.show();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_entry, menu);
        return true;
    }
    
}
