package com.honey.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.common.User;
import com.honey.R;
import com.honey.activity.history.RecentHistoryActivity;
import com.honey.common.MyApp;
import com.honey.common.Util;

import org.json.JSONObject;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {

    // Values for email and password at the time of the login attempt.
    private String mEmail = "";
    private String mPassword = "";

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    private TextView txtWebVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //check for login, if logged in, then redirect to recent history page
        if (((MyApp)getApplication()).getUserId() != null){
            goToRecentHistory();
        }

        txtWebVersion = (TextView)findViewById(R.id.txtWebVersion);
        txtWebVersion.setText(Html.fromHtml(getResources().getString(R.string.url_web_version)));
        txtWebVersion.setMovementMethod(LinkMovementMethod.getInstance());

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mEmail);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            pd = ProgressDialog.show(this,"Please Wait...","Logging In");

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getResources().getString(R.string.url_login);

            User user = new User();
            user.setEmail(mEmail);
            user.setPassword(mPassword);

            JSONObject requestObject = Util.toJsonObject(user);

            JsonObjectRequest request = new JsonObjectRequest(url, requestObject , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //if it comes back here, that means this is a valid user
                    if (pd.isShowing()){pd.dismiss();}
                    User user = Util.fromJSON(response, User.class);
                    succeedLogin(user);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pd.isShowing()){pd.dismiss();}
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();

                }
            });

            queue.add(request);
        }
    }



    private void succeedLogin(User user) {
        ((MyApp)this.getApplication()).saveUser(user.getId());
        //succeeds login
        goToRecentHistory();
    }

    private void goToRecentHistory() {
        Intent intent = new Intent(this, RecentHistoryActivity.class);
        startActivity(intent);
        finish();
    }
}
