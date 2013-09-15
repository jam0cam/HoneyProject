package com.honey.activity.history;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.finance.model.EntryCommand;
import com.honey.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by jitse on 8/29/13.
 */
public class RecentHistoryListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<EntryCommand>> entryCollections;
    private List<String> months;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");

    public RecentHistoryListAdapter(Activity context, List<String> laptops,
                                 Map<String, List<EntryCommand>> entryCollections) {
        this.context = context;
        this.entryCollections = entryCollections;
        this.months = laptops;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return entryCollections.get(months.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        EntryCommand entry = (EntryCommand)getChild(groupPosition, childPosition);


        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recent_history_child, null);
        }

        ((TextView) convertView.findViewById(R.id.txtName)).setText(entry.getPayee().getName());
        ((TextView) convertView.findViewById(R.id.txtAmount)).setText("$" + entry.getAmount());
        ((TextView) convertView.findViewById(R.id.txtDate)).setText(formatter.format(entry.getDate()));

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return entryCollections.get(months.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return months.get(groupPosition);
    }

    public int getGroupCount() {
        return months.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.recent_history_group, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
