package com.github.jpmoresmau.jpworkoutlog.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by jpmoresmau on 1/25/16.
 */
public class RangeTest {

    @Test
    public void testInteger(){
        Range<Integer> r=new Range<>();
        assertNull(r.getMin());
        assertNull(r.getMax());
        r.addSample(3);
        assertEquals(3, r.getMin().intValue());
        assertEquals(3, r.getMax().intValue());
        r.addSample(35);
        r.addSample(-2);
        assertEquals(-2, r.getMin().intValue());
        assertEquals(35, r.getMax().intValue());


    }
}
