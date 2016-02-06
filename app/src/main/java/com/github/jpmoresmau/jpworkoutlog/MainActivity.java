package com.github.jpmoresmau.jpworkoutlog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;
import com.github.jpmoresmau.jpworkoutlog.model.Workout;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity: create new workout, view stats...
 * @author jpmoresmau
 */
public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataHelper helper=new DataHelper(this);
        List<Workout> workouts=helper.listWorkouts();

        Button past=(Button)findViewById(R.id.past);
        past.setVisibility(workouts.isEmpty()?View.INVISIBLE:View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * new workout
     * @param v
     */
    public void newWorkoutClick(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * open statistics activity
     * @param v
     */
    public void openStats(View v){
        Intent intent=new Intent(this,StatsActivity.class);
        startActivity(intent);
    }

    public void openPast(View v){
        Intent intent=new Intent(this,PastActivity.class);
        startActivity(intent);
    }
}
