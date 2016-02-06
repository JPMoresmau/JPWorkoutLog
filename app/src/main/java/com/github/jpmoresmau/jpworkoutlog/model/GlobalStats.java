package com.github.jpmoresmau.jpworkoutlog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Global statistics
 * @author jpmoresmau
 */
public class GlobalStats implements Serializable{
    private Date firstWorkoutDate;
    private Date lastWorkoutDate;
    private int workoutCount;

    public GlobalStats(Date firstWorkoutDate, Date lastWorkoutDate, int workoutCount) {
        this.firstWorkoutDate = firstWorkoutDate;
        this.lastWorkoutDate = lastWorkoutDate;
        this.workoutCount = workoutCount;
    }

    public Date getFirstWorkoutDate() {
        return firstWorkoutDate;
    }

    public Date getLastWorkoutDate() {
        return lastWorkoutDate;
    }

    public int getWorkoutCount() {
        return workoutCount;
    }

    public double getAverage(){
        int nb=getWorkoutCount();
        Date st=getFirstWorkoutDate();
        long diff=System.currentTimeMillis()-st.getTime();
        long d= TimeUnit.MILLISECONDS.toDays(diff);
        return ((double)nb)/((double)d/7);
    }
}
