package com.github.jpmoresmau.jpworkoutlog.model;

import java.util.Date;

/**
 * Created by jpmoresmau on 1/24/16.
 */
public class ExerciseStat<T> {
    private Date workoutDate;
    private int setCount;
    private int repCount;
    private T totalWeight;


    public ExerciseStat(Date workoutDate,  int setCount, int repCount,T totalWeight) {
        this.workoutDate = workoutDate;
        this.setCount = setCount;

        this.repCount = repCount;
        this.totalWeight = totalWeight;
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }

    public int getSetCount() {
        return setCount;
    }

    public T getTotalWeight() {
        return totalWeight;
    }

    public int getRepCount() {
        return repCount;
    }
}
