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

    private HashMap<String,SetInfo<Double>> exercisesLatest=new HashMap<>();

    private String lastExercise="";

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

        Map<Long,String> exById=new HashMap<>();
        for (Exercise ex:dataHelper.listExercises()){
            exById.put(ex.getId(),ex.getName());
        }

        Map<Long,SetInfo<Long>> m=dataHelper.getLatestInfoByExercise();
        for (Long l:m.keySet()){
           String n=exById.get(l);
            if (n!=null){
                SetInfo<Long> si1=m.get(l);
                exercisesLatest.put(n,new SetInfo<Double>(si1.getReps(),SettingsActivity.getWeigthInUserUnits(ctx, si1.getWeight())));
            }
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
            lastExercise=ex.getName();
            totalWeight+=weight*reps;
            exercisesLatest.put(ex.getName(),new SetInfo(reps,weight));
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


    public HashMap<String, SetInfo<Double>> getExercisesLatest() {
        return exercisesLatest;
    }

    public String getLastExercise() {
        return lastExercise;
    }
}
