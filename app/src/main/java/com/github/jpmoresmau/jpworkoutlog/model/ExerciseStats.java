package com.github.jpmoresmau.jpworkoutlog.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jpmoresmau on 1/26/16.
 */
public class ExerciseStats {
    private List<ExerciseStat<Double>> stats=new LinkedList<>();

    private Range<Integer> setRange=new Range<>();
    private Range<Integer> repRange=new Range<>();
    private Range<Double> weightRange=new Range<>();

    public void addStat(ExerciseStat<Double> ws){
        stats.add(ws);
        setRange.addSample(ws.getSetCount());
        repRange.addSample(ws.getRepCount());
        weightRange.addSample(ws.getTotalWeight());
    }

    public List<ExerciseStat<Double>> getStats() {
        return stats;
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
