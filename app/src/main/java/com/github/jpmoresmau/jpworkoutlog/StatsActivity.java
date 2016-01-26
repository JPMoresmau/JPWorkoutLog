package com.github.jpmoresmau.jpworkoutlog;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.jpmoresmau.jpworkoutlog.model.StatsHelper;

/**
 * Activity showing statistics
 */
public class StatsActivity extends FragmentActivity {
    private StatsHelper statsHelper;

    private WorkoutStatFragment workoutFragment=null;
    private ExerciseStatFragment exerciseFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setupActionBar();

        statsHelper=new StatsHelper(this);

        TextView last=(TextView)findViewById(R.id.stats_global_last);
        if (statsHelper.hasWorkout()){
            last.setText(statsHelper.getLastWorkoutDate());
            TextView avg=(TextView)findViewById(R.id.stats_global_avg);
            avg.setText(statsHelper.getWorkoutAverage());

            Button ws=(Button)findViewById((R.id.stats_workout));
            ws.setVisibility(View.VISIBLE);
            Button es=(Button)findViewById((R.id.stats_exercise));
            es.setVisibility(View.VISIBLE);


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

    public void onWorkoutStats(View v){
        if (workoutFragment==null) {
            workoutFragment = new WorkoutStatFragment();
            workoutFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.workoutContainer, workoutFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().remove(workoutFragment).commit();
            workoutFragment=null;
        }
    }

    public void onExerciseStats(View v){
        if (exerciseFragment==null) {
            exerciseFragment = new ExerciseStatFragment();
            exerciseFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.exerciseContainer, exerciseFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().remove(exerciseFragment).commit();
            exerciseFragment=null;
        }
    }

    public void onWorkoutFileSave(View v){
        if (workoutFragment!=null){
            workoutFragment.onWorkoutFileSave(v);
        }
    }

    public void onExerciseFileSave(View v){
        if (exerciseFragment!=null){
            exerciseFragment.onExerciseFileSave(v);
        }
    }
}
