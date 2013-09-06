package com.honey.common;

import com.pingpong.Game;
import com.pingpong.Player;
import com.pingpong.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jitse on 9/6/13.
 */
public class MockUtil {

    private static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    public static Series getSeries() {
        Series series = new Series();
        series.setPlayer1TotalWin(55);
        series.setPlayer1SeriesWin(23);
        series.setPlayer2SeriesWin(13);
        series.setPlayer2TotalWin(40);
        series.setPlayerOne("Jia");
        series.setPlayerTwo("Matt");

        return series;
    }

    public static ArrayList<Game> getGames() {
        ArrayList<Game> games = new ArrayList<Game>();

        Player p1 = new Player();
        p1.setName("Jia");

        Player p2 = new Player();
        p2.setName("Matt");

        for (int i=0; i<5; i++) {
            Game g = new Game();
            g.setDateString("11/12/2013");
            g.setPlayer1Score(2);
            g.setPlayer2Score(3);
            g.setPlayer1(p1);
            g.setPlayer2(p2);

            if (g.getPlayer1Score() > g.getPlayer2Score()) {
                g.setWinner(g.getPlayer1().getName());
            } else if (g.getPlayer1Score() < g.getPlayer2Score()) {
                g.setWinner(g.getPlayer2().getName());
            } else {
                g.setWinner("TIE");
            }
            games.add(g);
        }

        return games;
    }
}
