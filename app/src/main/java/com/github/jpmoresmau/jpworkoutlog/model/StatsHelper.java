package com.github.jpmoresmau.jpworkoutlog.model;

import android.content.Context;

import com.github.jpmoresmau.jpworkoutlog.R;
import com.github.jpmoresmau.jpworkoutlog.SettingsActivity;
import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;

import java.text.DateFormat;
import java.util.List;

/**
 * Helper for statistics
 * @author jpmoresmau
 */
public class StatsHelper {
    private Context ctx;
    private DataHelper dataHelper;

    private GlobalStats globalStats;

    public StatsHelper(Context ctx) {
        this.ctx = ctx;
        this.dataHelper = new DataHelper(ctx);

    }

    public DataHelper getDataHelper() {
        return dataHelper;
    }

    public GlobalStats getGlobalStats() {
        if (globalStats==null){
            this.globalStats=dataHelper.getGlobalStats();
        }
        return globalStats;
    }

    /**
     * do we have any workout done?
     * @return
     */
    public boolean hasWorkout(){
        return getGlobalStats().getWorkoutCount()>0;
    }

    /**
     * get a string of the last workout date
     * @return
     */
    public String getLastWorkoutDate(){
        DateFormat df=DateFormat.getDateInstance(DateFormat.LONG);
        String d=df.format(getGlobalStats().getLastWorkoutDate());
        return String.format(ctx.getResources().getString(R.string.global_stats_last),d);
    }

    /**
     * get a text describing the workout average since initial workout date
     * @return
     */
    public String getWorkoutAverage(){
        DateFormat df=DateFormat.getDateInstance(DateFormat.LONG);
        String d=df.format(getGlobalStats().getFirstWorkoutDate());
        return String.format(ctx.getResources().getString(R.string.global_stats_avg),getGlobalStats().getWorkoutCount(),d,getGlobalStats().getAverage());
    }

    /**
     * get all workout statistics
     * @return
     */
    public WorkoutStats getWorkoutStats(){
        List<WorkoutStat<Long>> wss=dataHelper.getWorkoutStats();
        WorkoutStats ret=new WorkoutStats();
        for (WorkoutStat<Long> ws:wss){
            double d= SettingsActivity.getWeigthInUserUnits(ctx,ws.getTotalWeight());
            WorkoutStat<Double> wsd=new WorkoutStat<>(ws.getWorkoutDate(),ws.getExerciseCount(),ws.getSetCount(),ws.getRepCount(),d);
            ret.addStat(wsd);
        }
        return ret;
    }

    public List<Exercise> listExercises(){
        return dataHelper.listExercises();
    }

    /**
     * get stats for a given exercise
     * @param e
     * @return
     */
    public ExerciseStats getExerciseStat(Exercise e){
        List<ExerciseStat<Long>> ess=dataHelper.getExerciseStats(e.getId());
        ExerciseStats ret=new ExerciseStats();
        for (ExerciseStat<Long> es:ess){
            double d= SettingsActivity.getWeigthInUserUnits(ctx,es.getTotalWeight());
            double m= SettingsActivity.getWeigthInUserUnits(ctx,es.getMaxWeight());
            ExerciseStat<Double> esd=new ExerciseStat<>(es.getWorkoutDate(),es.getSetCount(),es.getRepCount(),d,m);
            ret.addStat(esd);
        }
        return ret;
    }
}
