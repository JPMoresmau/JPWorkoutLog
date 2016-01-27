package com.github.jpmoresmau.jpworkoutlog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jpmoresmau.jpworkoutlog.model.FileHelper;
import com.github.jpmoresmau.jpworkoutlog.model.Range;
import com.github.jpmoresmau.jpworkoutlog.model.StatsHelper;
import com.github.jpmoresmau.jpworkoutlog.model.WorkoutStats;

import java.io.File;
import java.io.IOException;

import static com.github.jpmoresmau.jpworkoutlog.ExerciseStatFragment.fillText;


/**
 */
public class WorkoutStatFragment extends Fragment {
    private WorkoutStats wss;
    private FileHelper fileHelper;

    public WorkoutStatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StatsHelper statsHelper=new StatsHelper(getActivity());

        View v= inflater.inflate(R.layout.fragment_workout_stat, container, false);

        wss=statsHelper.getWorkoutStats();
        fillText(v, R.id.exercises_min, R.id.exercises_max, wss.getExerciseRange());
        fillText(v,R.id.sets_min,R.id.sets_max,wss.getSetRange());
        fillText(v,R.id.reps_min,R.id.reps_max,wss.getRepRange());
        fillText(v, R.id.weight_min, R.id.weight_max, wss.getWeightRange());

        fileHelper=new FileHelper(getActivity());

        Button b=(Button)v.findViewById(R.id.workout_file_save);
        b.setVisibility(fileHelper.isExternalStorageWritable() ? View.VISIBLE : View.INVISIBLE);

        return v;
    }



    public void onWorkoutFileSave(View v){
        try {
            File f = fileHelper.writeWorkoutStats(wss);
            String notif=String.format(getResources().getString(R.string.file_saved),f.getAbsolutePath());
            Toast toast = Toast.makeText(getActivity(), notif, Toast.LENGTH_SHORT);
            toast.show();
        } catch (IOException ioe){
            Log.e("onWorkoutFileSave",ioe.getLocalizedMessage(),ioe);
            Toast toast = Toast.makeText(getActivity(), ioe.getLocalizedMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
