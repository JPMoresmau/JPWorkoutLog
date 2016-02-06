package com.github.jpmoresmau.jpworkoutlog.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Keep all workout statistics with range for workout values
 * @author jpmoresmau
 */
public class WorkoutStats implements Serializable {
    private List<WorkoutStat<Double>> stats=new LinkedList<>();

    private Range<Integer> exerciseRange=new Range<>();
    private Range<Integer> setRange=new Range<>();
    private Range<Integer> repRange=new Range<>();
    private Range<Double> weightRange=new Range<>();

    public void addStat(WorkoutStat<Double> ws){
        stats.add(ws);
        exerciseRange.addSample(ws.getExerciseCount());
        setRange.addSample(ws.getSetCount());
        repRange.addSample(ws.getRepCount());
        weightRange.addSample(ws.getTotalWeight());
    }

    public List<WorkoutStat<Double>> getStats() {
        return stats;
    }

    public Range<Integer> getExerciseRange() {
        return exerciseRange;
    }

    public Range<Integer> getSetRange() {
        return setRange;
    }

    public Range<Integer> getRepRange() {
        return repRange;
    }

    public Range<Double> getWeightRange() {
        return weightRange;
    }
}
