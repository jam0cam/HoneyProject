package com.honey.activity.history;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.finance.model.EntryCommand;
import com.honey.R;
import com.honey.common.Util;

import java.util.ArrayList;

/**
 * Created by jitse on 8/19/13.
 */
public class HistoryItemFragment extends Fragment {

    private ArrayList<EntryCommand> entries;
    private TextView txtTitle;
    private ListView listView;

    private EventListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_item, container, false);

        txtTitle = (TextView)view.findViewById(R.id.txtTitle);
        listView = (ListView)view.findViewById(R.id.lst_history_item);

        if (entries != null && !entries.isEmpty()) {
            txtTitle.setText(entries.get(0).getPayee().getName());
            listView.setAdapter(new HistoryListAdapter(this.getActivity(), entries));
            Util.getListViewSize(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), EditEntryActivity.class);
                    intent.putExtra("entry", entries.get(i));
                    startActivityForResult(intent, 1);
                }
            });
        }
        return view;
    }

    public interface EventListener {
        public void dataChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof EventListener) {
            listener = (EventListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet HistoryItemFragment.EventListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK){
            listener.dataChanged();
        }
    }

    public void setEntries(ArrayList<EntryCommand> entries) {
        this.entries = entries;
    }
}
