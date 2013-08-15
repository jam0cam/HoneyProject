package com.honey.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.finance.model.Payee;
import com.honey.R;

public class ViewPayeeActivity extends Activity {

    private TextView txtName;
    private TextView txtPhone;
    private TextView txtAccount;
    private EditText txtNotes;
    private EditText txtUrl;

    private Payee payee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payee);

        //payee = (Payee)getIntent().getSerializableExtra("payee");
        payee = new Payee();
        payee.setName("Chase");
        payee.setAccountNumber("UDSFHDKSJ");
        payee.setPhone("702-217-1850");

        txtName = (TextView)findViewById(R.id.txtName);
        txtAccount = (TextView)findViewById(R.id.txtAccount);
        txtPhone = (TextView)findViewById(R.id.txtPhone);

        txtName.setText(payee.getName());
        txtAccount.setText(payee.getAccountNumber());
        txtPhone.setText(payee.getPhone());

        //TODO: finish the rest here
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_payee, menu);
        return true;
    }
    
}
