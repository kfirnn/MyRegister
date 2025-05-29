package com.example.myregister.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myregister.R;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GamesActivity extends AppCompatActivity {
    private ListView gamesListView;
    private GameAdapter adapter;
    private ArrayList<Game> gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        gamesListView = findViewById(R.id.gamesListView);
        gamesList = new ArrayList<>();
        initializeGames();

        adapter = new GameAdapter();
        gamesListView.setAdapter(adapter);

        // Set up the click listener
        gamesListView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                Game selectedGame = gamesList.get(position);
                openMatchStatistics(selectedGame.getHomeTeam(), selectedGame.getAwayTeam());
            } catch (Exception e) {
                android.widget.Toast.makeText(this, "Error selecting game", android.widget.Toast.LENGTH_SHORT).show();
                android.util.Log.e("GamesActivity", "Error in click listener: " + e.getMessage());
            }
        });
    }

    private void initializeGames() {
        // July 2024
        gamesList.add(new Game("15 Jul 2024", "20:00", "Maccabi Tel Aviv", "Maccabi Petah Tikva", "Bloomfield", "Super Cup", 2, 0));
        gamesList.add(new Game("23 Jul 2024", "20:30", "Steaua Bucharest", "Maccabi Tel Aviv", "Steaua Stadium", "UCL Qual. R2", 1, 1));
        gamesList.add(new Game("31 Jul 2024", "21:00", "Maccabi Tel Aviv", "Steaua Bucharest", "Buzik Arena", "UCL Qual. R2", 0, 1));

        // August 2024
        gamesList.add(new Game("06 Aug 2024", "19:30", "Panevezys", "Maccabi Tel Aviv", "LFF Stadium", "UEL Qual. R3", 1, 2));
        gamesList.add(new Game("15 Aug 2024", "21:30", "Maccabi Tel Aviv", "Panevezys", "Haladash Stadium", "UEL Qual. R3", 3, 0));
        gamesList.add(new Game("18 Aug 2024", "20:00", "Maccabi Tel Aviv", "Bnei Sakhnin", "Bloomfield", "Toto Cup SF", 2, 1));
        gamesList.add(new Game("22 Aug 2024", "21:00", "Maccabi Tel Aviv", "Backa Topola", "Haladash Stadium", "UEL Playoff", 3, 0));
        gamesList.add(new Game("29 Aug 2024", "22:00", "Backa Topola", "Maccabi Tel Aviv", "TSC Arena", "UEL Playoff", 1, 5));

        // September 2024
        gamesList.add(new Game("01 Sep 2024", "20:15", "Maccabi Petah Tikva", "Maccabi Tel Aviv", "HaMoshava", "League", 0, 3));
        gamesList.add(new Game("14 Sep 2024", "20:30", "Maccabi Tel Aviv", "Hapoel Beer Sheva", "Bloomfield", "League", 1, 0));
        gamesList.add(new Game("18 Sep 2024", "17:30", "Maccabi Tel Aviv", "Hapoel Jerusalem", "Bloomfield", "League", 2, 1));
        gamesList.add(new Game("22 Sep 2024", "20:15", "MS Ashdod", "Maccabi Tel Aviv", "Y''A", "League", 0, 2));
        gamesList.add(new Game("26 Sep 2024", "22:00", "Braga", "Maccabi Tel Aviv", "Municipal Stadium", "UEL MD1", 2, 1));
        gamesList.add(new Game("29 Sep 2024", "20:30", "Maccabi Tel Aviv", "Ironi Tiberias", "Bloomfield", "League", 1, 1));

        // October 2024
        gamesList.add(new Game("03 Oct 2024", "19:45", "Maccabi Tel Aviv", "Mitylen", "Partizan Stadium", "UEL MD2", 0, 2));
        gamesList.add(new Game("06 Oct 2024", "20:30", "Maccabi Netanya", "Maccabi Tel Aviv", "Netanya", "League", 1, 2));
        gamesList.add(new Game("19 Oct 2024", "20:30", "Maccabi Tel Aviv", "Maccabi Haifa", "Teddy", "League", 2, 0));
        gamesList.add(new Game("24 Oct 2024", "19:45", "Maccabi Tel Aviv", "Real Sociedad", "Partizan Stadium", "UEL MD3", 1, 2));
        gamesList.add(new Game("28 Oct 2024", "20:00", "Beitar Jerusalem", "Maccabi Tel Aviv", "Teddy", "League", 3, 1));

        // November 2024
        gamesList.add(new Game("02 Nov 2024", "17:30", "Hapoel Kiryat Shmona", "Maccabi Tel Aviv", "Toto Turner", "League", 1, 0));
        gamesList.add(new Game("07 Nov 2024", "22:00", "Ajax", "Maccabi Tel Aviv", "Johan Cruyff Arena", "UEL MD4", 5, 0));
        gamesList.add(new Game("10 Nov 2024", "20:15", "Bnei Sakhnin", "Maccabi Tel Aviv", "Akko", "League", 0, 4));
        gamesList.add(new Game("28 Nov 2024", "19:45", "Besiktas", "Maccabi Tel Aviv", "Nagyerdei", "UEL MD5", 1, 3));

        // December 2024
        gamesList.add(new Game("02 Dec 2024", "20:00", "Maccabi Bnei Raina", "Maccabi Tel Aviv", "Green", "League", 1, 2));
        gamesList.add(new Game("05 Dec 2024", "20:00", "Maccabi Tel Aviv", "Hapoel Hadera", "Bloomfield", "League", 2, 2));
        gamesList.add(new Game("08 Dec 2024", "20:15", "Hapoel Haifa", "Maccabi Tel Aviv", "Sammy Ofer", "League", 1, 1));
        gamesList.add(new Game("12 Dec 2024", "22:00", "Maccabi Tel Aviv", "RFS Riga", "Partizan Stadium", "UEL MD6", 2, 1));
        gamesList.add(new Game("16 Dec 2024", "20:30", "Hapoel Jerusalem", "Maccabi Tel Aviv", "Teddy", "League", 2, 3));
        gamesList.add(new Game("21 Dec 2024", "15:00", "Maccabi Tel Aviv", "Maccabi Petah Tikva", "Bloomfield", "League", 3, 2));
        gamesList.add(new Game("25 Dec 2024", "20:00", "Maccabi Tel Aviv", "Maccabi Haifa", "Netanya", "Toto Cup Final", 3, 1));
        gamesList.add(new Game("28 Dec 2024", "20:00", "Maccabi Tel Aviv", "Hapoel Jerusalem", "Bloomfield", "State Cup R16", 3, 0));

        // January 2025
        gamesList.add(new Game("01 Jan 2025", "20:30", "Hapoel Beer Sheva", "Maccabi Tel Aviv", "Turner", "League", 2, 2));
        gamesList.add(new Game("04 Jan 2025", "17:30", "Maccabi Tel Aviv", "MS Ashdod", "Bloomfield", "League", 5, 1));
        gamesList.add(new Game("11 Jan 2025", "19:30", "Ironi Tiberias", "Maccabi Tel Aviv", "Green", "League", 2, 2));
        gamesList.add(new Game("15 Jan 2025", "20:30", "Maccabi Tel Aviv", "Maccabi Bnei Raina", "Bloomfield", "State Cup R8", 1, 2));
        gamesList.add(new Game("18 Jan 2025", "20:00", "Maccabi Tel Aviv", "Maccabi Netanya", "Bloomfield", "League", 4, 1));
        gamesList.add(new Game("23 Jan 2025", "19:45", "Bodo/Glimt", "Maccabi Tel Aviv", "Aspmyra", "UEL R16", 3, 1));
        gamesList.add(new Game("27 Jan 2025", "20:30", "Maccabi Haifa", "Maccabi Tel Aviv", "Sammy Ofer", "League", 0, 3));
        gamesList.add(new Game("30 Jan 2025", "22:00", "Maccabi Tel Aviv", "Porto", "Partizan Stadium", "UEL R8", 0, 1));

        // February 2025
        gamesList.add(new Game("03 Feb 2025", "20:30", "Maccabi Tel Aviv", "Beitar Jerusalem", "Bloomfield", "League", 1, 1));
        gamesList.add(new Game("09 Feb 2025", "20:15", "Kiryat Shmona", "Maccabi Tel Aviv", "Netanya", "League", 1, 2));
        gamesList.add(new Game("16 Feb 2025", "20:15", "Maccabi Tel Aviv", "Bnei Sakhnin", "Bloomfield", "League", 3, 1));
        gamesList.add(new Game("22 Feb 2025", "17:30", "Maccabi Tel Aviv", "Maccabi Bnei Raina", "Bloomfield", "League", 0, 1));

        // March 2025
        gamesList.add(new Game("01 Mar 2025", "19:30", "Hapoel Hadera", "Maccabi Tel Aviv", "Netanya", "League", 2, 3));
        gamesList.add(new Game("08 Mar 2025", "19:30", "Maccabi Tel Aviv", "Hapoel Haifa", "Bloomfield", "League", 2, 0));
        gamesList.add(new Game("15 Mar 2025", "19:30", "Maccabi Tel Aviv", "Hapoel Haifa", "Bloomfield", "League", 3, 0));
        gamesList.add(new Game("31 Mar 2025", "20:30", "Hapoel Beer Sheva", "Maccabi Tel Aviv", "Turner", "League", 1, 3));

        // April 2025
        gamesList.add(new Game("05 Apr 2025", "20:00", "Maccabi Tel Aviv", "Maccabi Netanya", "Bloomfield", "League", 4, 1));
        gamesList.add(new Game("14 Apr 2025", "20:30", "Maccabi Tel Aviv", "Maccabi Haifa", "Bloomfield", "League", 1, 1));
        gamesList.add(new Game("21 Apr 2025", "20:30", "Beitar Jerusalem", "Maccabi Tel Aviv", "Teddy", "League", 3, 1));
        gamesList.add(new Game("26 Apr 2025", "20:30", "Hapoel Haifa", "Maccabi Tel Aviv", "Sammy Ofer", "League", 1, 3));

        // May 2025
        gamesList.add(new Game("05 May 2025", "20:30", "Maccabi Tel Aviv", "Hapoel Beer Sheva", "Bloomfield", "League", 1, 1));
        gamesList.add(new Game("12 May 2025", "20:30", "Maccabi Netanya", "Maccabi Tel Aviv", "Netanya", "League", 1, 6));
        gamesList.add(new Game("19 May 2025", "20:30", "Maccabi Haifa", "Maccabi Tel Aviv", "Sammy Ofer", "League", 0, 3));
        gamesList.add(new Game("24 May 2025", "20:30", "Maccabi Tel Aviv", "Beitar Jerusalem", "Bloomfield", "League", 5, 0));

    }

    private void openMatchStatistics(String homeTeam, String awayTeam) {
        try {
            // Format the search query
            String searchQuery = homeTeam.replace(" ", "+") + "+vs+" + awayTeam.replace(" ", "+") + "+football+match";
            String url = "https://www.google.com/search?q=" + searchQuery;
            
            // Create the intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            
            // Try to start the activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Fallback to a more basic intent if the first one fails
                Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    startActivity(fallbackIntent);
                } catch (Exception e) {
                    android.widget.Toast.makeText(this, "Could not open browser. Please make sure you have a web browser installed.", android.widget.Toast.LENGTH_LONG).show();
                    android.util.Log.e("GamesActivity", "Error opening URL: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            android.widget.Toast.makeText(this, "Error: Could not open match statistics", android.widget.Toast.LENGTH_LONG).show();
            android.util.Log.e("GamesActivity", "Error in openMatchStatistics: " + e.getMessage());
        }
    }

    private class Game {
        String date;
        String time;
        String homeTeam;
        String awayTeam;
        String venue;
        String competition;
        int homeScore;
        int awayScore;

        Game(String date, String time, String homeTeam, String awayTeam, String venue, String competition, int homeScore, int awayScore) {
            this.date = date;
            this.time = time;
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
            this.venue = venue;
            this.competition = competition;
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }

        String getHomeTeam() {
            return homeTeam;
        }

        String getAwayTeam() {
            return awayTeam;
        }
    }

    private class GameAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return gamesList.size();
        }

        @Override
        public Object getItem(int position) {
            return gamesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(GamesActivity.this).inflate(R.layout.game_list_item, parent, false);
            }

            Game game = gamesList.get(position);

            TextView dateText = convertView.findViewById(R.id.tvDate);
            TextView matchText = convertView.findViewById(R.id.tvMatch);
            TextView scoreText = convertView.findViewById(R.id.tvScore);
            TextView statusText = convertView.findViewById(R.id.tvStatus);

            dateText.setText(game.date + " " + game.time + " - " + game.venue);
            matchText.setText(game.homeTeam + " vs " + game.awayTeam);
            scoreText.setText(game.homeScore + " - " + game.awayScore);
            scoreText.setVisibility(View.VISIBLE);
            statusText.setText(game.competition);
            statusText.setTextColor(getResources().getColor(android.R.color.darker_gray));

            return convertView;
        }
    }
} 