package com.github.jpmoresmau.jpworkoutlog;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.github.jpmoresmau.jpworkoutlog.model.StatsHelper;

public class StatsActivity extends Activity {
    private StatsHelper statsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setupActionBar();

        statsHelper=new StatsHelper(this);

        TextView last=(TextView)findViewById(R.id.stats_global_last);
        if (statsHelper.hasWorkout()){
            last.setText(statsHelper.getLastWorkoutDate());
        } else {
            last.setText(getResources().getText(R.string.global_stats_none));
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
