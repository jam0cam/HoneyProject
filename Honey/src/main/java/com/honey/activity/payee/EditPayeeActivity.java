package com.honey.activity.payee;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.finance.model.Payee;
import com.honey.R;
import com.honey.common.MyApp;

/**
 * Created by jitse on 8/24/13.
 */
public class EditPayeeActivity extends AddPayeeActivity {

    Payee payeeToEdit;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            payeeToEdit = (Payee)savedInstanceState.getSerializable("payee");
        } else {
            payeeToEdit = (Payee)getIntent().getSerializableExtra("payee");
        }

        txtName.setText(payeeToEdit.getName());
        txtNotes.setText(payeeToEdit.getNotes());
        txtAccount.setText(payeeToEdit.getAccountNumber());
        txtUrl.setText(payeeToEdit.getUrl());
        txtPhone.setText(payeeToEdit.getPhone());

        if (payeeToEdit.isNotifyOn()) {
            sw.setChecked(true);
        } else {
            sw.setChecked(false);
        }

        np.setValue(Integer.parseInt(payeeToEdit.getNotifyDay()));

        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("payee", payeeToEdit);
    }

    @Override
    protected void attemptSave() {
        boolean hasErrors = hasErrors();

        if (!hasErrors) {
            Payee payee = new Payee();
            payee.setId(payeeToEdit.getId());
            payee.setNotes(notes);
            payee.setName(name);
            payee.setPhone(phone);
            payee.setUrl(url);
            payee.setAccountNumber(account);
            payee.setUserId(((MyApp) getApplication()).getUserId());
            if (sw.isChecked()) {
                payee.setNotifyOn(true);
                payee.setNotifyDay(Integer.toString(np.getValue()));
            }
            saveData(payee);
        }
    }

    @Override
    protected boolean hasErrors() {
        boolean rval = false;

        notes = txtNotes.getText().toString().trim();
        name = txtName.getText().toString().trim();
        phone = txtPhone.getText().toString().trim();
        url = txtUrl.getText().toString().trim();
        account = txtAccount.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            txtName.setError(getString(R.string.error_field_required));
            rval = true;
        } else {
            //Check that the name doesn't match another existing payee (with a different id)
            for (Payee p: existingPayees) {
                if (p.getId().equals(payeeToEdit.getId())) {
                    continue;
                }
                if (p.getName().toLowerCase().equals(name.toLowerCase())) {
                    txtName.setError(getString(R.string.error_payee_exists));
                    rval = true;
                    break;
                }
            }
        }

        return rval;
    }
}
