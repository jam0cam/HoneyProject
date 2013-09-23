package com.honey.activity.stats;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finance.model.MonthTotal;
import com.honey.R;

import java.util.List;

public class MonthlyExpenseListAdapter extends BaseAdapter {
    private Activity activity;
    private List<MonthTotal> data;
    private static LayoutInflater inflater=null;

    public MonthlyExpenseListAdapter(Activity a, List<MonthTotal> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if (convertView==null) {
            vi = inflater.inflate(R.layout.monthly_expense_row, null);
        }

        TextView txtDate = (TextView)vi.findViewById(R.id.txtMonth); // title
        TextView txtAmount = (TextView)vi.findViewById(R.id.txtAmount); // artist name

        // Setting all values in listview
        txtDate.setText(data.get(position).getMonth());
        txtAmount.setText(data.get(position).getFormattedTotal());
        return vi;
    }
}
