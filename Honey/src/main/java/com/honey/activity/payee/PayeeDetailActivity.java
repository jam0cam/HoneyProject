package com.honey.activity.payee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.finance.model.DeleteObject;
import com.finance.model.Payee;
import com.honey.R;
import com.honey.activity.BaseActivity;
import com.honey.common.Util;

import org.json.JSONObject;

import java.util.ArrayList;

public class PayeeDetailActivity extends BaseActivity implements ISelectedPayee {

    private Payee payee;
    private ArrayList<Payee> payees;

    private boolean payeeChanged = false;

    private PayeeDetailFragment fragment;
    public static final int PAYEE_REMOVED = -5;
    public static final int PAYEE_CHANGED = -6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payee = (Payee)getIntent().getSerializableExtra("payee");
        payees = (ArrayList<Payee>)getIntent().getSerializableExtra("payees");

        setContentView(R.layout.activity_payee_detail);

        fragment = (PayeeDetailFragment)getFragmentManager().findFragmentById(R.id.frgPayeeDetail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }


    /**
     * Removes the payee via webservice call
     */
    public void removeButtonClicked() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.url_delete_payee);
        DeleteObject entry = new DeleteObject(payee.getId());
        JSONObject obj = Util.toJsonObject(entry);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                removeSuccessful(payee.getId());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                saveFailed();
            }
        });

        queue.add(request);
    }

    public void editButtonClicked() {
        Intent intent = new Intent(this, EditPayeeActivity.class);
        intent.putExtra("payee", payee);
        intent.putExtra("payees", payees);
        startActivityForResult(intent, 1);
    }

    /**
     * Notify the caller of this activity (payee list activity) that this payee is removed, so
     * it can remove it out of the list.
     * @param tag
     */
    private void removeSuccessful(String tag) {
        Toast toast = Toast.makeText(this, "Payee deleted.", Toast.LENGTH_SHORT);
        toast.show();

        Intent returnIntent = new Intent();
        setResult(PAYEE_REMOVED,returnIntent);
        finish();
    }

    /**
     * When this happens, that means the EditPayeeActivity changed the payee, we update our local
     * data here
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            payeeChanged = true;
            Bundle res = data.getExtras();
            payee = (Payee)res.getSerializable("payee");
            fragment.setPayee(payee);
            fragment.reDraw();
        }
    }

    @Override
    public void onBackPressed() {
        if (payeeChanged){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("payee", payee);
            setResult(PAYEE_CHANGED,returnIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Payee getSelectedPayee() {
        return payee;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.payee_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit_payee:
                editButtonClicked();
                return true;
            case R.id.action_remove_payee:
                removeButtonClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
