package com.github.jpmoresmau.jpworkoutlog;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jpmoresmau.jpworkoutlog.model.FileHelper;
import com.github.jpmoresmau.jpworkoutlog.model.StatsHelper;

import java.io.File;
import java.io.IOException;

/**
 * Activity showing statistics
 * @author jpmoresmau
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
            Button as=(Button)findViewById((R.id.all_file_save));
            as.setVisibility(View.VISIBLE);


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

    /**
     * workout stats button toggled
     * @param v
     */
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

    /**
     * exercise stats button toggled
     * @param v
     */
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

    /**
     * save workout info to file
     * @param v
     */
    public void onWorkoutFileSave(View v){
        if (workoutFragment!=null){
            workoutFragment.onWorkoutFileSave(v);
        }
    }

    /**
     * save exercise info to file
     * @param v
     */
    public void onExerciseFileSave(View v){
        if (exerciseFragment!=null){
            exerciseFragment.onExerciseFileSave(v);
        }
    }

    /**
     * save all to file
     * @param v
     */
    public void onAllFileSave(View v){
        FileHelper fileHelper=new FileHelper(this);
        try {
            File f = fileHelper.writeAllStats(statsHelper.getDataHelper());
            String notif = String.format(getResources().getString(R.string.file_saved), f.getAbsolutePath());
            Toast toast = Toast.makeText(this, notif, Toast.LENGTH_SHORT);
            toast.show();
        } catch (IOException ioe) {
            Toast toast = Toast.makeText(this, ioe.getLocalizedMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
