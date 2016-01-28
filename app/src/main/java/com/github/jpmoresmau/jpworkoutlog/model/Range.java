package com.github.jpmoresmau.jpworkoutlog.model;

/**
 * Range (min/max) of values
 * @author jpmoresmau
 */
public class Range<T extends Comparable<T>> {
    private T min;
    private T max;

    public Range(){

    }

    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }


    /**
     * minimum value encountered, possibly null
     * @return
     */
    public T getMin() {
        return min;
    }

    /**
     * maximum value encountered, possibly null
     * @return
     */
    public T getMax() {
        return max;
    }

    /**
     * add a sample, adjusting min and max accordingly
     * @param s
     */
    public void addSample(T s){
        if (min==null || min.compareTo(s)>0){
            min=s;
        }
        if (max==null || max.compareTo(s)<0){
            max=s;
        }
    }

    @Override
    public String toString() {
        return "Range{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }


}
