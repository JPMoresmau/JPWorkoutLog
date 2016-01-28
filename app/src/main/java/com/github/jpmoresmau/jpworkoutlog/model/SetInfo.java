package com.github.jpmoresmau.jpworkoutlog.model;

import java.io.Serializable;

/**
 * Information about a Set
 * @author jpmoresmau
 * @param <T> the class of the weight (Long for grams, Double for user unit)
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
