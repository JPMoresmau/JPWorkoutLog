package com.github.jpmoresmau.jpworkoutlog.model;

import java.util.Date;

/**
 * Created by jpmoresmau on 1/24/16.
 */
public class GlobalStats {
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


}
