package com.github.jpmoresmau.jpworkoutlog.model;

import android.content.Context;

import com.github.jpmoresmau.jpworkoutlog.R;
import com.github.jpmoresmau.jpworkoutlog.SettingsActivity;
import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class encapsulating the current workout
 * @author jpmoresmau
 */
public class RuntimeInfo {
    private Context ctx;
    private DataHelper dataHelper;
    /**
     * current workout
     */
    private Workout workout;

    /**
     * exercises done
     */
    private Set<Exercise> exercises=new HashSet<Exercise>();

    /**
     * sets done
     */
    private List<ExSet> sets;

    /**
     * total weight, in user units
     */
    private double totalWeight=0;

    /**
     * exercise by name cache
     */
    private Map<String,Exercise> exercisesByName=new HashMap<>();

    /**
     * latest info by exercise name
     */
    private HashMap<String,SetInfo<Double>> exercisesLatest=new HashMap<>();

    /**
     * last exercise name
     */
    private String lastExercise="";

    public RuntimeInfo(Context ctx, Date d) {
        this.ctx=ctx;
        this.dataHelper = new DataHelper(ctx);
        this.workout = this.dataHelper.addWorkout(d);
        // workout may have been started already today
        sets=this.dataHelper.listSets(workout);
        for (ExSet s:sets){
            exercises.add(s.getExercise());
            double uw=SettingsActivity.getWeigthInUserUnits(ctx,s.getWeight());
            totalWeight+=uw*s.getReps();
        }



        Map<Long,SetInfo<Long>> m=dataHelper.getLatestInfoByExercise();
        for (Long l:m.keySet()){
            Exercise ex=dataHelper.getExercise(l); // this uses a cache
            if (ex!=null){
                SetInfo<Long> si1=m.get(l);
                exercisesLatest.put(ex.getName(),new SetInfo<Double>(si1.getReps(),SettingsActivity.getWeigthInUserUnits(ctx, si1.getWeight())));
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

    /**
     * get the summary as a localized string
     * @return
     */
    public String getSummary(){
        String s=ctx.getResources().getString(R.string.summary);
        String summary= String.format(s, exercises.size(),sets.size(),totalWeight, SettingsActivity.getUnitName(ctx));
        return summary;
    }

    /**
     * add a set to the current information and the database
     * @param exName exercise name
     * @param reps number of repetitions
     * @param weight weight in user units
     */
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

    /**
     * ensure exercise name cache is populated
     */
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

    /**
     * list all possible exercise names
     * @return
     */
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
