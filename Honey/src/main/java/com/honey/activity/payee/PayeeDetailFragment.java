package com.honey.activity.payee;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finance.model.Payee;
import com.honey.R;

/**
 * Created by jitse on 8/16/13.
 */
public class PayeeDetailFragment extends Fragment {

    private Payee payee;
    private TextView txtName;
    private TextView txtAccount;
    private TextView txtPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payee_detail, container, false);

        txtName = (TextView)view.findViewById(R.id.txtName);
        txtPhone = (TextView)view.findViewById(R.id.txtPhone);
        txtAccount = (TextView)view.findViewById(R.id.txtAccount);

        payee = ((ISelectedPayee)getActivity()).getSelectedPayee();
        reDraw();
        return view;
    }

    public void reDraw() {
        if (txtName != null) {
            txtName.setText(payee.getName());
            txtPhone.setText(payee.getPhone());
            txtAccount.setText(payee.getAccountNumber());
        }
    }
}
