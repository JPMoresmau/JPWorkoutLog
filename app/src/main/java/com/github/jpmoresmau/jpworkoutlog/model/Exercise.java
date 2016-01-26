package com.github.jpmoresmau.jpworkoutlog.model;

/**
 * Created by jpmoresmau on 1/18/16.
 */
public class Exercise {
    public Exercise(long id, String name) {
        if (name==null || name.trim().length()==0){
            throw new IllegalArgumentException("name");
        }
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercise exercise = (Exercise) o;

        return id == exercise.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return name;
    }
}
