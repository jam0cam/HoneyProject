package com.honey.Fragment;

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
    private View mMainView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payee_detail, container, false);

        mMainView = view.findViewById(R.id.payee_main_layout);
        mMainView.setVisibility(View.INVISIBLE);

        txtName = (TextView)view.findViewById(R.id.txtName);
        txtPhone = (TextView)view.findViewById(R.id.txtPhone);
        txtAccount = (TextView)view.findViewById(R.id.txtAccount);

        if (savedInstanceState != null) {
            payee = (Payee)savedInstanceState.getSerializable("payee");
        }
        if (payee != null) {
            reDraw();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (payee != null) {
            outState.putSerializable("payee", payee);
        }
    }

    public void setPayeeAndRedraw(Payee payee) {
        this.payee = payee;
        reDraw();
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public void reDraw() {
        if (txtName != null) {
            mMainView.setVisibility(View.VISIBLE);
            //Util.showProgress(false, mProgressView, mMainView, animationTime);
            txtName.setText(payee.getName());
            txtPhone.setText(payee.getPhone());
            txtAccount.setText(payee.getAccountNumber());
        }
    }
}
