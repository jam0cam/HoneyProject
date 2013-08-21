package com.honey.activity.payee;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.finance.model.Payee;
import com.honey.R;
import com.honey.activity.BaseActivity;

import java.util.ArrayList;

public class PayeeListActivity extends BaseActivity implements PayeeListFragment.OnItemSelectedListener, ISelectedPayee{

    private ArrayList<Payee> payees = null;
    private Payee selectedPayee = null;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            payees = (ArrayList<Payee>)savedInstanceState.getSerializable("payees");
            selectedPayee = (Payee)savedInstanceState.getSerializable("selectedPayee");
        }

        setContentView(R.layout.activity_payee_list);

        if (payees == null) {
            pd = ProgressDialog.show(this,"Fetching Payees","Loading");
        }

        if (findViewById(R.id.fragment_container) != null && selectedPayee != null) {
            if (getFragmentManager().findFragmentByTag("detailFragment") == null) {
                //this means it is the first time loading, and the fragment doesn't exist yet
                createDetailFragment();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.view_payee_list, menu);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("payees", payees);
        outState.putSerializable("selectedPayee", selectedPayee);
    }

    @Override
    public void onFillingList(ArrayList<Payee> payees) {
        this.payees = payees;
        if (this.selectedPayee == null) {
            this.selectedPayee = payees.get(0);
        }

        if (pd.isShowing()){pd.dismiss();}

        //this should only happen once. On the first time it fills out the payee, the detail
        //fragment may not exist in landscape mode. Add it
        if (findViewById(R.id.fragment_container) != null) {
            if (getFragmentManager().findFragmentByTag("detailFragment") == null) {
                //this means it is the first time loading, and the fragment doesn't exist yet
                createDetailFragment();
            }
        }
    }

    public ArrayList<Payee> getPayees() {
        return payees;
    }

    public Payee getSelectedPayee() {
        return selectedPayee;
    }

    private void createDetailFragment() {
        PayeeDetailFragment fragment = new PayeeDetailFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, "detailFragment");
        transaction.commit();
    }

    @Override
    public void onItemSelected(Payee payee) {
        this.selectedPayee = payee;
        if (findViewById(R.id.fragment_container) != null) {
            //landscape mode
            createDetailFragment();
        } else {
            Intent intent = new Intent(this, PayeeDetailActivity.class);
            intent.putExtra("payee", payee);
            startActivity(intent);
        }
    }
}
