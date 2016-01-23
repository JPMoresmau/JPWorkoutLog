package com.github.jpmoresmau.jpworkoutlog.model;

import java.util.Date;

/**
 * Created by jpmoresmau on 1/19/16.
 */
public class Workout {
    private long id;
    private Date date;

    public Workout(long id, Date date) {
        if (date==null){
            throw new IllegalArgumentException("date");
        }
        this.id = id;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workout workout = (Workout) o;

        return id == workout.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }

    public Date getDate() {
        return date;

    }


}
