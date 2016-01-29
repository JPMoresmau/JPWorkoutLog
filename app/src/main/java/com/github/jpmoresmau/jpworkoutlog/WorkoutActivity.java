package com.github.jpmoresmau.jpworkoutlog;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jpmoresmau.jpworkoutlog.model.ExSet;
import com.github.jpmoresmau.jpworkoutlog.model.RuntimeInfo;

import java.util.Date;

public class WorkoutActivity extends FragmentActivity implements AddSetExerciseFragment.NewSetListener{
    public static final String WORKOUT_DATE = "WORKOUT_DATE";

    private TextView summaryText;
    private RuntimeInfo runtimeInfo;

    private LastSetFragment lastSetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        setupActionBar();

        long d=getIntent().getLongExtra(WORKOUT_DATE,System.currentTimeMillis());
        runtimeInfo=new RuntimeInfo(this,new Date(d));

        summaryText=(TextView)findViewById(R.id.summaryText);

        lastSetFragment=new LastSetFragment();
        lastSetFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lastSetContainer, lastSetFragment).commit();

        setSummary();

    }

    private void setSummary(){
       if (runtimeInfo!=null){
           summaryText.setText(runtimeInfo.getSummary());
           lastSetFragment.setLastSet(runtimeInfo.getLastSet());
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

    public void onDone(View view){
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onNewSet(View view){
        DialogFragment newFragment = new AddSetExerciseFragment();
        Bundle b=new Bundle();
        b.putStringArray(AddSetExerciseFragment.EXERCISES, runtimeInfo.listPossibleExerciseNames());
        b.putString(AddSetExerciseFragment.LAST_EXERCISE, runtimeInfo.getLastExercise());
        b.putSerializable(AddSetExerciseFragment.LAST_EXERCISES,runtimeInfo.getExercisesLatest());
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "addset");
    }

    @Override
    public void newSet(String ex, int reps, double weight) {
        runtimeInfo.addSet(ex, reps, weight);
        setSummary();
        String s=getResources().getString(R.string.set_added);
        String notif= String.format(s, ex);
        Toast toast = Toast.makeText(this, notif, Toast.LENGTH_SHORT);
        toast.show();

    }

    /**
     * remove last set
     * @param v
     */
    public void removeLastSet(View v){
        runtimeInfo.removeLastSet();
        setSummary();

    }
}
