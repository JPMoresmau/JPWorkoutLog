package com.github.jpmoresmau.jpworkoutlog.model;

import android.test.AndroidTestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by jpmoresmau on 1/25/16.
 */
public class StatsTest {

    @Test
    public void testAverage(){
        long d1=System.currentTimeMillis();
        long d0=d1- TimeUnit.DAYS.toMillis(7);
        GlobalStats gs=new GlobalStats(new Date(d0),new Date(d1),1);
        assertEquals(1.0, gs.getAverage(), 0.0000001);
        gs=new GlobalStats(new Date(d0),new Date(d1),3);
        assertEquals(3.0, gs.getAverage(), 0.0000001);

        long d2=d1- TimeUnit.DAYS.toMillis(21);
        gs=new GlobalStats(new Date(d2),new Date(d1),3);
        assertEquals(1.0, gs.getAverage(), 0.0000001);

        long d3=d1- TimeUnit.DAYS.toMillis(2);
        gs=new GlobalStats(new Date(d3),new Date(d1),2);
        assertEquals(7.0, gs.getAverage(), 0.0000001);
        long d4=d1- TimeUnit.DAYS.toMillis(4);
        gs=new GlobalStats(new Date(d4),new Date(d1),2);
        assertEquals(3.5, gs.getAverage(), 0.0000001);

    }
}
