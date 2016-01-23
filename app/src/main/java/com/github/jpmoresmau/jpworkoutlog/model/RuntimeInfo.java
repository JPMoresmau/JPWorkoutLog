package com.github.jpmoresmau.jpworkoutlog.model;

import android.app.Activity;
import android.content.Context;

import com.github.jpmoresmau.jpworkoutlog.R;
import com.github.jpmoresmau.jpworkoutlog.SettingsActivity;
import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jpmoresmau on 1/19/16.
 */
public class RuntimeInfo {
    private Context ctx;
    private DataHelper dataHelper;
    private Workout workout;

    private Set<Exercise> exercises=new HashSet<Exercise>();

    private List<ExSet> sets;

    private double totalWeight=0;

    private Map<String,Exercise> exercisesByName=new HashMap<>();

    public RuntimeInfo(Context ctx, Date d) {
        this.ctx=ctx;
        this.dataHelper = new DataHelper(ctx);
        this.workout = this.dataHelper.addWorkout(d);
        sets=this.dataHelper.listSets(workout);
        for (ExSet s:sets){
            exercises.add(s.getExercise());
            double uw=SettingsActivity.getWeigthInUserUnits(ctx,s.getWeight());
            totalWeight+=uw*s.getReps();
        }
    }

    public Workout getWorkout() {
        return workout;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public String getSummary(){
        String s=ctx.getResources().getString(R.string.summary);
        String summary= String.format(s, exercises.size(),sets.size(),totalWeight, SettingsActivity.getUnitName(ctx));
        return summary;
    }

    public void addSet(String exName,int reps,double weight){
        checkExercisesNames();
        Exercise ex=exercisesByName.get(exName);
        if (ex==null){
            ex=dataHelper.addExercise(exName);
            exercisesByName.put(exName,ex);
        }
        ExSet s=dataHelper.addSet(workout, ex, reps, SettingsActivity.getWeightInGrams(ctx, weight));
        if (s.getId()>-1){
            sets.add(s);
            exercises.add(ex);
            totalWeight+=weight*reps;
        }
    }

    private void checkExercisesNames(){
        if (exercisesByName.isEmpty()) {
            for (Exercise e : dataHelper.listExercises()) {
                exercisesByName.put(e.getName(), e);
            }
            for (String s : ctx.getResources().getStringArray(R.array.default_exercises)) {
                exercisesByName.put(s, null);
            }
        }
    }

    public String[] listPossibleExerciseNames(){
        checkExercisesNames();
        return exercisesByName.keySet().toArray(new String[exercisesByName.size()]);
    }
}
