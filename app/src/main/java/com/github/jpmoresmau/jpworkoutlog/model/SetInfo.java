package com.github.jpmoresmau.jpworkoutlog.model;

import java.io.Serializable;

/**
 * Created by jpmoresmau on 1/23/16.
 */
public class SetInfo<T> implements Serializable{
    private int reps;
    private T weight;

    public SetInfo(int reps, T weight) {
        this.reps = reps;
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public T getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "SetInfo{" +
                "reps=" + reps +
                ", weight=" + weight +
                '}';
    }
}
