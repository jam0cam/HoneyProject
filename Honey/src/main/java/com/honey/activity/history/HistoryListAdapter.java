package com.honey.activity.history;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finance.model.EntryCommand;
import com.honey.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jitse on 8/21/13.
 */
public class HistoryListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<EntryCommand> data;
    private static LayoutInflater inflater=null;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public HistoryListAdapter(Activity a, ArrayList<EntryCommand> d) {
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
        if(convertView==null)
            vi = inflater.inflate(R.layout.history_list_row, null);

        TextView txtDate = (TextView)vi.findViewById(R.id.txtDate); // title
        TextView txtAmount = (TextView)vi.findViewById(R.id.txtAmount); // artist name

        EntryCommand entry = data.get(position);

        // Setting all values in listview
        txtDate.setText(formatter.format(entry.getDate()));
        txtAmount.setText("$" + entry.getAmount());
        return vi;
    }
}
