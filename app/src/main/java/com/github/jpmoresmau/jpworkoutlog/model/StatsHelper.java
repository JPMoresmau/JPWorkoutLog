package com.github.jpmoresmau.jpworkoutlog.model;

import android.content.Context;

import com.github.jpmoresmau.jpworkoutlog.R;
import com.github.jpmoresmau.jpworkoutlog.SettingsActivity;
import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Helper for statistics
 * Created by jpmoresmau on 1/24/16.
 */
public class StatsHelper {
    private Context ctx;
    private DataHelper dataHelper;

    private GlobalStats globalStats;

    public StatsHelper(Context ctx) {
        this.ctx = ctx;
        this.dataHelper = new DataHelper(ctx);

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

}
