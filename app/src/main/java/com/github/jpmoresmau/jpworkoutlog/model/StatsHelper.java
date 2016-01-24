package com.github.jpmoresmau.jpworkoutlog.model;

import android.content.Context;

import com.github.jpmoresmau.jpworkoutlog.R;
import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by jpmoresmau on 1/24/16.
 */
public class StatsHelper {
    private Context ctx;
    private DataHelper dataHelper;

    private GlobalStats globalStats;

    public StatsHelper(Context ctx) {
        this.ctx = ctx;
        this.dataHelper = new DataHelper(ctx);
        this.globalStats=dataHelper.getGlobalStats();
    }

    public boolean hasWorkout(){
        return globalStats.getWorkoutCount()>0;
    }

    public String getLastWorkoutDate(){
        DateFormat df=DateFormat.getDateInstance(DateFormat.LONG);
        String d=df.format(globalStats.getLastWorkoutDate());
        return String.format(ctx.getResources().getString(R.string.global_stats_last),d);
    }
}
