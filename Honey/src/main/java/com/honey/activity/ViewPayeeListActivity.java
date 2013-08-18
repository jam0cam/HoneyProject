package com.honey.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.finance.model.Payee;
import com.honey.Fragment.PayeeDetailFragment;
import com.honey.Fragment.PayeeListFragment;
import com.honey.R;

import java.util.ArrayList;

public class ViewPayeeListActivity extends Activity implements PayeeListFragment.OnItemSelectedListener{

    private ArrayList<Payee> payees = null;
    private Payee selectedPayee = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            payees = (ArrayList<Payee>)savedInstanceState.getSerializable("payees");
            selectedPayee = (Payee)savedInstanceState.getSerializable("selectedPayee");
        }

        setContentView(R.layout.activity_view_payee_list);

        if (findViewById(R.id.fragment_container) != null) {
            //this is portrait mode
            if (getFragmentManager().findFragmentByTag("listFragmentPort") == null) {
                PayeeListFragment listFragment = new PayeeListFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, listFragment, "listFragmentPort");
                transaction.commit();
            } else {
                //this is an existing one, so do a replace
                PayeeListFragment fragment = (PayeeListFragment)getFragmentManager().findFragmentByTag("listFragmentPort");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }
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
    }

    public ArrayList<Payee> getPayees() {
        return payees;
    }

    public Payee getSelectedPayee() {
        return selectedPayee;
    }

    @Override
    public void onItemSelected(Payee payee) {
        this.selectedPayee = payee;
        if (findViewById(R.id.fragment_container) != null) {
            //portrait mode
            PayeeDetailFragment fragment = new PayeeDetailFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            PayeeDetailFragment fragment = (PayeeDetailFragment)getFragmentManager().findFragmentById(R.id.frgPayeeDetail);
            fragment.setPayeeAndRedraw(payee);
        }
    }
}
