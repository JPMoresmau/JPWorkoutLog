package com.github.jpmoresmau.jpworkoutlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jpmoresmau.jpworkoutlog.R;
import com.github.jpmoresmau.jpworkoutlog.model.Exercise;
import com.github.jpmoresmau.jpworkoutlog.model.ExerciseStats;
import com.github.jpmoresmau.jpworkoutlog.model.FileHelper;
import com.github.jpmoresmau.jpworkoutlog.model.Range;
import com.github.jpmoresmau.jpworkoutlog.model.StatsHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class ExerciseStatFragment extends Fragment {
    private FileHelper fileHelper;
    private Exercise e;
    private ExerciseStats ess;

    public ExerciseStatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.fragment_exercise_stat, container, false);
        Spinner spinner = (Spinner)v.findViewById(R.id.exercise_spinner);

        fileHelper=new FileHelper(getActivity());
        final StatsHelper statsHelper=new StatsHelper(getActivity());
        final List<Exercise> exs=statsHelper.listExercises();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item,exs.toArray(new Exercise[exs.size()]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Button b=(Button)v.findViewById(R.id.exercise_file_save);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                e=exs.get(position);
                ess=statsHelper.getExerciseStat(e);
                fillText(v, R.id.sets_min, R.id.sets_max, ess.getSetRange());
                fillText(v,R.id.reps_min,R.id.reps_max,ess.getRepRange());
                fillText(v,R.id.weight_min,R.id.weight_max,ess.getWeightRange());
                b.setVisibility(fileHelper.isExternalStorageWritable() ? View.VISIBLE : View.INVISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ess=null;
                e=null;
                fillText(v, R.id.sets_min, R.id.sets_max, null);
                fillText(v,R.id.reps_min,R.id.reps_max,null);
                fillText(v,R.id.weight_min,R.id.weight_max,null);
                b.setVisibility(View.INVISIBLE);
            }
        });

        return v;
    }

    private void fillText(View v,int minid,int maxid,Range<?> r){
        TextView tv=(TextView)v.findViewById(minid);
        tv.setText(r!=null?r.getMin().toString():"");
        tv=(TextView)v.findViewById(maxid);
        tv.setText(r!=null?r.getMax().toString():null);

    }

    public void onExerciseFileSave(View v){
        if (e!=null && ess!=null) {
            try {
                File f = fileHelper.writeExerciseStats(e,ess);
                String notif = String.format(getResources().getString(R.string.file_saved), f.getAbsolutePath());
                Toast toast = Toast.makeText(getActivity(), notif, Toast.LENGTH_SHORT);
                toast.show();
            } catch (IOException ioe) {
                Log.e("onExerciseFileSave", ioe.getLocalizedMessage(), ioe);
                Toast toast = Toast.makeText(getActivity(), ioe.getLocalizedMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
