package com.honey.activity;

import android.os.Bundle;

import com.finance.model.Payee;
import com.honey.R;

public class PayeeDetailActivity extends BaseActivity implements ISelectedPayee{

    private Payee payee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payee = (Payee)getIntent().getSerializableExtra("payee");
        setContentView(R.layout.activity_payee_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public Payee getSelectedPayee() {
        return payee;
    }
}
