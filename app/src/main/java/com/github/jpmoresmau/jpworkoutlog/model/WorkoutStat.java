package com.github.jpmoresmau.jpworkoutlog.model;

import java.util.Date;

/**
 * Statistics for one workout (aggregate all set information)
 * @author jpmoresmau
 */
public class WorkoutStat<T> {
    private Date workoutDate;
    private int exerciseCount;
    private int setCount;
    private int repCount;
    private T totalWeight;


    public WorkoutStat(Date workoutDate, int exerciseCount, int setCount, int repCount,T totalWeight) {
        this.workoutDate = workoutDate;
        this.exerciseCount = exerciseCount;
        this.setCount = setCount;

        this.repCount = repCount;
        this.totalWeight = totalWeight;
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }

    public int getExerciseCount() {
        return exerciseCount;
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
