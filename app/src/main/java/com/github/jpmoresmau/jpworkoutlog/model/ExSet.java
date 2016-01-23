package com.github.jpmoresmau.jpworkoutlog.model;

/**
 * Created by jpmoresmau on 1/19/16.
 */
public class ExSet {
    private long id;
    private Workout workout;
    private Exercise exercise;
    private int reps;
    private long weight;

    public ExSet(long id, Workout workout, Exercise exercise, int reps, long weight) {
        this.id = id;
        this.workout = workout;
        this.exercise = exercise;
        this.reps = reps;
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getReps() {
        return reps;
    }

    public long getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExSet exSet = (ExSet) o;

        return id == exSet.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ExSet{" +
                "id=" + id +
                ", workout=" + workout +
                ", exercise=" + exercise +
                ", reps=" + reps +
                ", weight=" + weight +
                '}';
    }
}
