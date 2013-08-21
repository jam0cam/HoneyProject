package com.honey.activity.history;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.finance.model.EntryCommand;
import com.honey.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jitse on 8/19/13.
 */
public class HistoryItemFragment extends Fragment {

    private ArrayList<EntryCommand> entries;
    private TextView txtTitle;
    private LinearLayout mainLayout;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_item, container, false);

        txtTitle = (TextView)view.findViewById(R.id.txtTitle);
        mainLayout = (LinearLayout)view.findViewById(R.id.mainLayout);

        if (entries != null && !entries.isEmpty()) {
            refresh();
        }
        return view;
    }

    public void setEntries(ArrayList<EntryCommand> entries) {
        this.entries = entries;
    }

    /**
     * This is where most of the heavy lifting takes place. It will construct the entire layout.
     */
    private void refresh() {
        txtTitle.setText(entries.get(0).getPayee().getName());

        //for each entry, create a new horizontal LinearLayout. Put 2 TextViews in that layout, and then
        //add that layout to mainLayout
        mainLayout.removeAllViewsInLayout();
        for (EntryCommand entry : entries) {
            LinearLayout layout = new LinearLayout(this.getActivity());
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv1 = new TextView(this.getActivity());
            tv1.setText(formatter.format(entry.getDate()));
            tv1.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView tv2 = new TextView(this.getActivity());
            tv2.setText("$" + entry.getAmount());
            tv2.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            layout.addView(tv1);
            layout.addView(tv2);

            mainLayout.addView(layout);
        }
    }
}
