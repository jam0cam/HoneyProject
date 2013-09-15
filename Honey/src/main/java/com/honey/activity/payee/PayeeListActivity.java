package com.honey.activity.payee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.finance.model.Payee;
import com.honey.R;
import com.honey.activity.BaseActivity;

import java.util.ArrayList;

public class PayeeListActivity extends BaseActivity implements PayeeListFragment.OnItemSelectedListener, ISelectedPayee{

    private ArrayList<Payee> payees = null;
    private Payee selectedPayee = null;

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
    public void error() {
        showToastError();
        if (pd != null && pd.isShowing()){pd.dismiss();}
    }

    @Override
    public void onFillingList(ArrayList<Payee> payees) {
        this.payees = payees;
        if (this.selectedPayee == null) {
            this.selectedPayee = payees.get(0);
        }

        if (pd != null && pd.isShowing()){pd.dismiss();}
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

        Intent intent = new Intent(this, PayeeDetailActivity.class);
        intent.putExtra("payee", payee);
        intent.putExtra("payees", payees);

        startActivityForResult(intent,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_payee:
                Intent addPayeeIntent = new Intent(this, AddPayeeActivity.class);
                addPayeeIntent.putExtra("payees", payees);
                startActivityForResult(addPayeeIntent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            restartActivity();
        } else if (resultCode == PayeeDetailActivity.PAYEE_REMOVED){
            softRestartActivity();
        } else if (resultCode == PayeeDetailActivity.PAYEE_CHANGED){
            softRestartActivity();
        }
    }

    /**
     * This is used so that the activity doesn't *appear* to be reloaded
     */
    public void softRestartActivity() {
        payees = null;
        selectedPayee = null;

        //reload the list fragment
        PayeeListFragment listFragment = (PayeeListFragment)getFragmentManager().findFragmentById(R.id.frgPayeeList);
        listFragment.fetchPayees();
    }
}
