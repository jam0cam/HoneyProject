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
        setContentView(R.layout.activity_view_payee_list);

        if (savedInstanceState != null) {
            payees = (ArrayList<Payee>)savedInstanceState.getSerializable("payees");
            selectedPayee = (Payee)savedInstanceState.getSerializable("selectedPayee");
        }

        if (findViewById(R.id.fragment_container) != null) {
            //this is portrait mode
            if (getFragmentManager().findFragmentByTag("listFragmentPort") == null) {
                //this is a new one, so just do a add
                PayeeListFragment listFragment = getNewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, listFragment, "listFragmentPort");
                transaction.commit();
            } else {
                //this is an existing one, so do a replace
                PayeeListFragment fragment = (PayeeListFragment)getFragmentManager().findFragmentByTag("listFragmentPort");
                if (payees != null && !payees.isEmpty()) {
                    fragment.setPayees(payees);
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        } else {        //landscape mode
            if (getFragmentManager().findFragmentByTag("listFragmentLand") == null) {
                PayeeListFragment listFragment = getNewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.conPayeeList, listFragment, "listFragmentLand");
                transaction.commit();
            }
            if (getFragmentManager().findFragmentByTag("detailFragmentLand") == null) {
                PayeeDetailFragment detailFragment = new PayeeDetailFragment();
                if(selectedPayee != null) {
                    detailFragment.setPayee(selectedPayee);
                }
                else if (payees != null && !payees.isEmpty()) {
                    detailFragment.setPayee(payees.get(0));
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.conPayeeDetail, detailFragment, "detailFragmentLand");
                transaction.commit();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    private PayeeListFragment getNewFragment() {
        PayeeListFragment listFragment = new PayeeListFragment();

        if (payees != null && !payees.isEmpty()) {
            listFragment.setPayees(payees);
        }
        return listFragment;
    }

    private PayeeDetailFragment getNewOrExistingFragmentPort() {
        if (getFragmentManager().findFragmentByTag("detailFragmentPort") == null) {
            return new PayeeDetailFragment();
        }  else {
            return (PayeeDetailFragment)getFragmentManager().findFragmentByTag("detailFragmentPort");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (payees != null && !payees.isEmpty()) {
            outState.putSerializable("payees", payees);
            outState.putSerializable("selectedPayee", selectedPayee);
        }
    }

    @Override
    public void onFillingList(ArrayList<Payee> payees) {
        this.payees = payees;
        if (findViewById(R.id.fragment_container) == null) {
            PayeeDetailFragment fragment = (PayeeDetailFragment)getFragmentManager().findFragmentByTag("detailFragmentLand");
            if (fragment !=  null) {
                if (selectedPayee != null) {
                    fragment.setPayeeAndRedraw(selectedPayee);
                } else {
                    fragment.setPayeeAndRedraw(payees.get(0));
                }
            }
        }
    }

    @Override
    public void onItemSelected(Payee payee) {
        if (findViewById(R.id.fragment_container) != null) {
            //portrait mode
            PayeeDetailFragment fragment = getNewOrExistingFragmentPort();
            fragment.setPayee(payee);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            this.selectedPayee = payee;
            PayeeDetailFragment fragment = (PayeeDetailFragment)getFragmentManager().findFragmentByTag("detailFragmentLand");
            fragment.setPayeeAndRedraw(payee);
        }
    }
}
