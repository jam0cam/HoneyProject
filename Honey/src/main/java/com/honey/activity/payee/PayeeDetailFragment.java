package com.honey.activity.payee;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private TextView txtNotes;
    private TextView txtUrl;
    private TextView txtNotifyDay;
    private ImageView imgAlarm;
    private Listener listener;
    private PayeeListActivity parentActivity;

    private ImageButton btnEdit;
    private ImageButton btnRemove;

    public interface Listener{
        public void removeButtonClicked();
        public void editButtonClicked();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payee_detail, container, false);

        txtName = (TextView)view.findViewById(R.id.txtName);
        txtPhone = (TextView)view.findViewById(R.id.txtPhone);
        txtAccount = (TextView)view.findViewById(R.id.txtAccount);
        txtNotes = (TextView)view.findViewById(R.id.txtNotes);
        txtUrl = (TextView)view.findViewById(R.id.txtUrl);
        imgAlarm = (ImageView) view.findViewById(R.id.imgAlarm);
        txtNotifyDay = (TextView)view.findViewById(R.id.txtNotifyDay);
        
        btnEdit = (ImageButton)view.findViewById(R.id.btnEdit);
        btnRemove = (ImageButton)view.findViewById(R.id.btnRemove);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.editButtonClicked();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.removeButtonClicked();
            }
        });

        payee = ((ISelectedPayee)getActivity()).getSelectedPayee();
        reDraw();
        return view;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public void reDraw() {
        if (txtName != null) {
            txtName.setText(payee.getName());
            txtPhone.setText(payee.getPhone());
            txtAccount.setText(payee.getAccountNumber());
            txtNotes.setText(payee.getNotes());
            txtUrl.setText(payee.getUrl());
            if (payee.isNotifyOn()) {
                imgAlarm.setImageResource(R.drawable.alarm_on);
                txtNotifyDay.setText("*You will be notified on the " + payee.getNotifyDay() + " of each month.");
                txtNotifyDay.setVisibility(View.VISIBLE);
            } else {
                imgAlarm.setImageResource(R.drawable.alarm_off);
                txtNotifyDay.setVisibility(View.GONE);
            }
        }
    }
}
