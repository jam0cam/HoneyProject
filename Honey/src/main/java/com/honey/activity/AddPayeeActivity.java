package com.honey.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.honey.R;

import org.json.JSONException;

public class AddPayeeActivity extends Activity {

    private String mName;
    private String mAccount;
    private String mPhone;
    private String mUrl;
    private String mNotes;

    // UI references.
    private EditText txtName;
    private EditText txtAccount;
    private EditText txtPhone;
    private EditText txtUrl;
    private EditText txtNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payee);

        txtName = (EditText) findViewById(R.id.name);
        txtAccount= (EditText) findViewById(R.id.accountNumber);
        txtPhone = (EditText) findViewById(R.id.phone);
        txtUrl = (EditText) findViewById(R.id.url);
        txtNotes = (EditText) findViewById(R.id.notes);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptSave();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.payee, menu);
        return true;
    }


    private void attemptSave() throws JSONException {
        validate();

        //TODO: JIA: call save method
    }

    private void validate() {
        // Reset errors.
        txtName.setError(null);
        txtAccount.setError(null);
        txtPhone.setError(null);
        txtUrl.setError(null);
        txtNotes.setError(null);

        // Store values
        mName= txtName.getText().toString();
        mAccount= txtAccount.getText().toString();
        mPhone = txtPhone.getText().toString();
        mUrl = txtUrl.getText().toString();
        mNotes = txtNotes.getText().toString();

        //TODO: JIA: perform validation here, but for now, there won't be any
    }
}
