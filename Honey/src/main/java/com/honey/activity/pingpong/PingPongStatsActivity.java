package com.honey.activity.pingpong;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;

import com.honey.R;
import com.honey.activity.BaseActivity;
import com.pingpong.Game;
import com.pingpong.Series;

import java.util.ArrayList;

public class PingPongStatsActivity extends BaseActivity implements SeriesFragment.SeriesEventListener, GamesFragment.SeriesEventListener {

    private SeriesFragment frgPPSeries;
    private GamesFragment frgPPGames;

    private ArrayList<Game> games;
    private Series series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_pong_stats);

        if (savedInstanceState != null) {
            games = (ArrayList<Game>)savedInstanceState.getSerializable("games");
            series = (Series)savedInstanceState.getSerializable("series");
        }

        frgPPSeries = (SeriesFragment)getFragmentManager().findFragmentById(R.id.frgPPSeries);
        frgPPSeries.setRetainInstance(true);

        frgPPGames = (GamesFragment)getFragmentManager().findFragmentById(R.id.frgPPGames);
        frgPPGames.setRetainInstance(true);

        if (games == null || series == null) {
            //just have to show this while the fragments load
            pd = ProgressDialog.show(this, "Fetching Games", "Loading");
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        getMenuInflater().inflate(R.menu.ping_pong_stats, menu);
        return true;
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

    @Override
    public void error() {
        showToastError();
    }

    @Override
    public void onDataLoaded(Series series) {
        this.series = series;
        if (series == null || games == null) {
            //don't reload until both series and games are available
            return;
        }
        if (pd != null && pd.isShowing()){pd.dismiss();}
    }
}
