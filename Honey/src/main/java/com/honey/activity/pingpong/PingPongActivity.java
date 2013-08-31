package com.honey.activity.pingpong;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.honey.R;
import com.honey.activity.BaseActivity;
import com.honey.common.Util;
import com.pingpong.Game;
import com.pingpong.Series;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class PingPongActivity extends BaseActivity implements SeriesFragment.SeriesEventListener, GamesFragment.SeriesEventListener {


    private Game game;
    private ArrayList<Game> games;
    private Series series;
    private Button btnSave;

    private EditText txtMattScore;
    private EditText txtJiaScore;

    private String mattScore;
    private String jiaScore;

    private SeriesFragment frgPPSeries;
    private GamesFragment frgPPGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_pong);

        txtMattScore = (EditText)findViewById(R.id.txtMattScore);
        txtJiaScore = (EditText)findViewById(R.id.txtJiaScore);

        frgPPSeries = (SeriesFragment)getFragmentManager().findFragmentById(R.id.frgPPSeries);
        frgPPSeries.setRetainInstance(true);

        frgPPGames = (GamesFragment)getFragmentManager().findFragmentById(R.id.frgPPGames);
        frgPPGames.setRetainInstance(true);

        if (savedInstanceState != null) {
            games = (ArrayList<Game>)savedInstanceState.getSerializable("games");
            series = (Series)savedInstanceState.getSerializable("series");
        }

        if (games == null) {
            //just have to show this while the fragments load
            pd = ProgressDialog.show(this,"Fetching Games","Loading");
        }

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });
    }

    protected void attemptSave() {
        boolean hasErrors = hasErrors();

        if (!hasErrors) {
            //clear previous error fields
            txtMattScore.setError(null);
            txtJiaScore.setError(null);

            game = new Game();
            mattScore = txtMattScore.getText().toString().trim();
            jiaScore = txtJiaScore.getText().toString().trim();

            if (!TextUtils.isEmpty(mattScore)) {
                game.setPlayer2Score(Integer.parseInt(mattScore));
            }
            if (!TextUtils.isEmpty(jiaScore)) {
                game.setPlayer1Score(Integer.parseInt(jiaScore));
            }

            game.setPlayer1(games.get(0).getPlayer1());
            game.setPlayer2(games.get(0).getPlayer2());
            game.setDate(new Date());
            saveData();
        }
    }

    protected boolean hasErrors() {
        boolean rval = false;

        mattScore = txtMattScore.getText().toString().trim();
        jiaScore = txtJiaScore.getText().toString().trim();

        if (TextUtils.isEmpty(mattScore)) {
            txtMattScore.setError(getString(R.string.error_field_required));
            rval = true;
        }

        if (TextUtils.isEmpty(jiaScore)) {
            txtJiaScore.setError(getString(R.string.error_field_required));
            rval = true;
        }

        //both scores must add up to 5
        if (!rval) {
            if (Integer.parseInt(jiaScore) + Integer.parseInt(mattScore) != 5){
                txtJiaScore.setError(getString(R.string.error_invalid_sum));
                rval = true;
            }
        }


        return rval;
    }

    protected void saveData() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.url_save_game);

        game.setDateString(formatter.format(new Date()));
        if (game.getPlayer1Score() > game.getPlayer2Score()){
            game.setWinner(game.getPlayer1().getName());
        } else {
            game.setWinner(game.getPlayer2().getName());
        }

        JSONObject obj = Util.toJsonObject(game);

        pd = ProgressDialog.show(this,"Please Wait...","Saving");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (pd != null && pd.isShowing()){pd.dismiss();}
                saveSuccessful();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pd != null && pd.isShowing()){pd.dismiss();}
                saveFailed();
            }
        });
        queue.add(request);
    }

    @Override
    protected void saveSuccessful() {
        Toast toast = Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT);
        toast.show();

        frgPPGames.updateGames(game);
        frgPPSeries.updateSeries(game);

        //reset the actual game itself
        game = new Game();
        txtJiaScore.setText("");
        txtMattScore.setText("");

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("games", games);
        outState.putSerializable("series", series);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ping_pong, menu);
        return true;
    }

    /**
     * This is called by the child fragments when it is done loading data.
     * @param series
     */
    @Override
    public void onDataLoaded(Series series) {
        this.series = series;
        if (series == null || games == null) {
            //don't reload until both series and games are available
            return;
        }
        if (pd != null && pd.isShowing()){pd.dismiss();}
    }

    @Override
    public void error() {
        showToastError();
    }

    @Override
    public void onDataLoaded(ArrayList<Game> games) {
        this.games = games;
        if (series == null || games == null) {
            //don't reload until both series and games are available
            return;
        }
        if (pd != null && pd.isShowing()){pd.dismiss();}
    }
}