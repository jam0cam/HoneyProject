package com.honey.activity.history;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.finance.model.EntryCommand;
import com.finance.model.Payee;
import com.honey.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Add entry is just a special case of edit entry
 *
 * Created by jitse on 8/21/13.
 */
public class AddEntryActivity  extends EditEntryActivity {

    private Spinner spinner;
    private ArrayList<Payee> payees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (payees == null) {
            payees = (ArrayList<Payee>)getIntent().getSerializableExtra("payees");
        }

        //instead of showing this, we show a drop down selected
        txtPayee.setVisibility(View.GONE);
        txtDate.setText(formatter.format(new Date()));

        spinner = (Spinner)findViewById(R.id.spinnerPayee);
        spinner.setAdapter(new PayeeSpinnerAdapter(this, payees));
        spinner.setVisibility(View.VISIBLE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Payee p = payees.get(i);
                entry.setSelectedPayeeId(p.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //since it's inheriting EditEntry, this could possibly be created.
        entry = new EntryCommand();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               attemptSave();
            }
        });
    }

    protected void attemptSave() {
        boolean hasErrors = hasErrors();

        if (!hasErrors) {
            EntryCommand newEntry = new EntryCommand();
            newEntry.setSelectedPayeeId(entry.getSelectedPayeeId());

            //these are the editable data
            newEntry.setNotes(notes);
            newEntry.setDate(date);
            newEntry.setAmount(amount);

            saveData(newEntry);
        }
    }

}
