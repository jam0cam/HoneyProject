package com.honey.activity.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.DeleteObject;
import com.finance.model.EntryCommand;
import com.honey.R;
import com.honey.common.Util;

import org.json.JSONObject;

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
    private HistoryItemFragment parentFragment;

    public HistoryListAdapter(Activity a, ArrayList<EntryCommand> d, HistoryItemFragment fragment) {
        activity = a;
        data=d;
        parentFragment = fragment;
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
        ImageView btnRemove = (ImageView)vi.findViewById(R.id.btnRemove);
        ImageView btnEdit = (ImageView)vi.findViewById(R.id.btnEdit);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeEntry((String) view.getTag());
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEntry((String) view.getTag());
            }
        });
        EntryCommand entry = data.get(position);
        btnRemove.setTag(entry.getId());
        btnEdit.setTag(entry.getId());


        // Setting all values in listview
        txtDate.setText(formatter.format(entry.getDate()));
        txtAmount.setText("$" + entry.getAmount());
        return vi;
    }

    private void editEntry(final String tag) {
        Intent intent = new Intent(activity, EditEntryActivity.class);
        for (EntryCommand entry : data) {
            if (entry.getId().equals(tag)) {
                intent.putExtra("entry", entry);
                break;
            }
        }
        activity.startActivityForResult(intent, 1);
    }

    /**
     * Makes a call to the webservice to delete this entry. On success, it will remove the
     * entry from the list. tag is the same as the entryId
     */
    private void removeEntry(final String tag) {
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = activity.getResources().getString(R.string.url_delete_entry);
        DeleteObject entry = new DeleteObject(tag);
        JSONObject obj = Util.toJsonObject(entry);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                removeSuccessful(tag);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                saveFailed();
            }
        });

        queue.add(request);
    }


    private void removeSuccessful(String tag) {
        Toast toast = Toast.makeText(activity, "Entry deleted.", Toast.LENGTH_SHORT);
        toast.show();

        int indexToRemove = 0;
        for (int i=0; i<data.size(); i++) {
            EntryCommand entry = data.get(i);
            if (entry.getId().equals(tag)) {
                indexToRemove = i;
                break;
            }
        }

        data.remove(indexToRemove);
        notifyDataSetChanged();
        parentFragment.resizeView();
    }

    private void saveFailed() {
        Toast toast = Toast.makeText(activity, "Failed to delete entry.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
