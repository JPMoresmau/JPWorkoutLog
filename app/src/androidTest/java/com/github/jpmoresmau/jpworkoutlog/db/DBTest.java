package com.github.jpmoresmau.jpworkoutlog.db;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.github.jpmoresmau.jpworkoutlog.model.ExSet;
import com.github.jpmoresmau.jpworkoutlog.model.Exercise;
import com.github.jpmoresmau.jpworkoutlog.model.ExerciseStat;
import com.github.jpmoresmau.jpworkoutlog.model.GlobalStats;
import com.github.jpmoresmau.jpworkoutlog.model.SetInfo;
import com.github.jpmoresmau.jpworkoutlog.model.Workout;
import com.github.jpmoresmau.jpworkoutlog.model.WorkoutStat;

import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Test DB operations
 * @author jpmoresmau
 */
public class DBTest extends AndroidTestCase {
    private DataHelper dataHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context ctx = new RenamingDelegatingContext(getContext(), "TEST");
        dataHelper = new DataHelper(ctx);

    }

    @Override
    protected void tearDown() throws Exception {
        dataHelper.close();
        super.tearDown();
    }

    @Test
    public void testExercise() {
        List<Exercise> exs = dataHelper.listExercises();
        assertNotNull(exs);
        assertTrue(exs.isEmpty());

        Exercise ex1 = dataHelper.addExercise("ex1");
        assertNotNull(ex1);
        assertTrue(ex1.getId() > -1);
        assertEquals("ex1", ex1.getName());
        assertEquals(ex1, dataHelper.getExercise("ex1"));
        exs = dataHelper.listExercises();
        assertNotNull(exs);
        assertEquals(1, exs.size());
        assertTrue(exs.contains(ex1));

        assertEquals(ex1, dataHelper.getExercise(ex1.getId()));


        Exercise ex2 = dataHelper.addExercise("ex2");
        assertNotNull(ex2);
        assertTrue(ex2.getId() > -1);
        assertEquals("ex2", ex2.getName());
        assertFalse(ex1.equals(ex2));
        assertEquals(ex2, dataHelper.getExercise("ex2"));
        exs = dataHelper.listExercises();
        assertNotNull(exs);
        assertEquals(2, exs.size());
        assertTrue(exs.contains(ex1));
        assertTrue(exs.contains(ex2));

        Exercise ex3 = dataHelper.addExercise("ex1");
        assertNotNull(ex3);
        assertEquals(ex1, ex3);
        exs = dataHelper.listExercises();
        assertNotNull(exs);
        assertEquals(2, exs.size());
        assertTrue(exs.contains(ex1));
        assertTrue(exs.contains(ex2));

    }

    @Test
    public void testWorkout() {
        List<Workout> ws = dataHelper.listWorkouts();
        assertNotNull(ws);
        assertTrue(ws.isEmpty());

        Date d1 = new Date(System.currentTimeMillis());
        Workout w1 = dataHelper.addWorkout(d1);
        assertNotNull(w1);
        assertTrue(w1.getId() > -1);
        assertEquals(d1, w1.getDate());
        assertEquals(w1, dataHelper.getWorkout(d1));
        ws = dataHelper.listWorkouts();
        assertNotNull(ws);
        assertEquals(1, ws.size());
        assertTrue(ws.contains(w1));

        Date d2 = new Date(System.currentTimeMillis());
        assertTrue(d2.getTime() > d1.getTime());
        Workout w2 = dataHelper.addWorkout(d2);
        assertNotNull(w2);
        assertTrue(w2.getId() > -1);
        assertEquals(d2, w2.getDate());
        assertFalse(w1.equals(w2));
        assertEquals(w2, dataHelper.getWorkout(d2));
        ws = dataHelper.listWorkouts();
        assertNotNull(ws);
        assertEquals(2, ws.size());
        assertTrue(ws.contains(w1));
        assertTrue(ws.contains(w2));

        Workout w3 = dataHelper.addWorkout(d1);
        assertNotNull(w3);
        assertEquals(w1, w3);
        ws = dataHelper.listWorkouts();
        assertNotNull(ws);
        assertEquals(2, ws.size());
        assertTrue(ws.contains(w1));
        assertTrue(ws.contains(w2));


    }

    public void testGlobalStats() {
        Date d1 = new Date(System.currentTimeMillis() - (3600 * 1000 * 24));
        Workout w1 = dataHelper.addWorkout(d1);
        Date d2 = new Date(System.currentTimeMillis());
        assertTrue(d2.getTime() > d1.getTime());
        Workout w2 = dataHelper.addWorkout(d2);

        GlobalStats gs = dataHelper.getGlobalStats();
        assertNotNull(gs);
        assertEquals(2, gs.getWorkoutCount());
        assertEquals(d1, gs.getFirstWorkoutDate());
        assertEquals(d2, gs.getLastWorkoutDate());
    }

    @Test
    public void testSet() throws InterruptedException {
        Exercise ex1 = dataHelper.addExercise("ex1");
        Exercise ex2 = dataHelper.addExercise("ex2");
        Date d1 = new Date(System.currentTimeMillis());
        Thread.sleep(100);
        Date d2 = new Date(System.currentTimeMillis());
        assertTrue(d2.getTime() > d1.getTime());

        Workout w1 = dataHelper.addWorkout(d1);
        Workout w2 = dataHelper.addWorkout(d2);

        List<ExSet> exs1 = dataHelper.listSets(w1);
        assertNotNull(exs1);
        assertTrue(exs1.isEmpty());

        ExSet s1 = dataHelper.addSet(w1, ex1, 10, 5);
        assertNotNull(s1);
        assertEquals(w1, s1.getWorkout());
        assertEquals(ex1, s1.getExercise());
        assertEquals(10, s1.getReps());
        assertEquals(5, s1.getWeight());
        assertTrue(s1.getId() > -1);

        exs1 = dataHelper.listSets(w1);
        assertNotNull(exs1);
        assertEquals(1, exs1.size());
        assertEquals(s1, exs1.get(0));

        List<ExSet> exs2 = dataHelper.listSets(w2);
        assertNotNull(exs2);
        assertTrue(exs2.isEmpty());

        ExSet s2 = dataHelper.addSet(w1, ex1, 12, 4);
        assertNotNull(s2);
        assertEquals(w1, s2.getWorkout());
        assertEquals(ex1, s2.getExercise());
        assertEquals(12, s2.getReps());
        assertEquals(4, s2.getWeight());

        assertTrue(s2.getId() > s1.getId());

        exs1 = dataHelper.listSets(w1);
        assertNotNull(exs1);
        assertEquals(2, exs1.size());
        assertEquals(s1, exs1.get(0));
        assertEquals(s2, exs1.get(1));

        exs2 = dataHelper.listSets(w2);
        assertNotNull(exs2);
        assertTrue(exs2.isEmpty());

        dataHelper.deleteSet(s2.getId());
        exs1 = dataHelper.listSets(w1);
        assertNotNull(exs1);
        assertEquals(1, exs1.size());
        assertEquals(s1, exs1.get(0));

    }

    @Test
    public void testLatest() {
        Exercise ex1 = dataHelper.addExercise("ex1");
        Exercise ex2 = dataHelper.addExercise("ex2");
        Date d1 = new Date(System.currentTimeMillis());
        Workout w1 = dataHelper.addWorkout(d1);

        Map<Long, SetInfo<Long>> m = dataHelper.getLatestInfoByExercise();
        assertNotNull(m);
        assertTrue(m.isEmpty());

        ExSet s1 = dataHelper.addSet(w1, ex1, 10, 5);
        m = dataHelper.getLatestInfoByExercise();
        assertNotNull(m);
        assertEquals(1, m.size());
        SetInfo<Long> si = m.get(ex1.getId());
        assertNotNull(si);
        assertEquals(10, si.getReps());
        assertEquals(5, si.getWeight().longValue());

        ExSet s2 = dataHelper.addSet(w1, ex1, 11, 6);
        m = dataHelper.getLatestInfoByExercise();
        assertNotNull(m);
        assertEquals(1, m.size());
        si = m.get(ex1.getId());
        assertNotNull(si);
        assertEquals(11, si.getReps());
        assertEquals(6, si.getWeight().longValue());

        ExSet s3 = dataHelper.addSet(w1, ex2, 4, 3);
        m = dataHelper.getLatestInfoByExercise();
        assertNotNull(m);
        assertEquals(2, m.size());
        si = m.get(ex1.getId());
        assertNotNull(si);
        assertEquals(11, si.getReps());
        assertEquals(6, si.getWeight().longValue());
        si = m.get(ex2.getId());
        assertNotNull(si);
        assertEquals(4, si.getReps());
        assertEquals(3, si.getWeight().longValue());

    }

    public void testWorkoutStats() {
        Exercise ex1 = dataHelper.addExercise("ex1");
        Exercise ex2 = dataHelper.addExercise("ex2");
        Date d1 = new Date(System.currentTimeMillis() - 1000);
        Workout w1 = dataHelper.addWorkout(d1);
        dataHelper.addSet(w1, ex1, 10, 5);
        dataHelper.addSet(w1, ex1, 11, 6);
        dataHelper.addSet(w1, ex2, 4, 3);

        Date d2 = new Date(System.currentTimeMillis());
        Workout w2 = dataHelper.addWorkout(d2);
        dataHelper.addSet(w2, ex2, 4, 3);

        List<WorkoutStat<Long>> wss = dataHelper.getWorkoutStats();
        assertNotNull(wss);
        assertEquals(2, wss.size());
        WorkoutStat<Long> ws1 = wss.get(0);
        assertEquals(d1, ws1.getWorkoutDate());
        assertEquals(2, ws1.getExerciseCount());
        assertEquals(3, ws1.getSetCount());
        assertEquals(25, ws1.getRepCount());
        assertEquals(128, ws1.getTotalWeight().longValue());

        WorkoutStat<Long> ws2 = wss.get(1);
        assertEquals(d2, ws2.getWorkoutDate());
        assertEquals(1, ws2.getExerciseCount());
        assertEquals(1, ws2.getSetCount());
        assertEquals(4, ws2.getRepCount());
        assertEquals(12, ws2.getTotalWeight().longValue());

    }

    public void testExerciseStats() {
        Exercise ex1 = dataHelper.addExercise("ex1");
        Exercise ex2 = dataHelper.addExercise("ex2");
        Date d1 = new Date(System.currentTimeMillis() - 1000);
        Workout w1 = dataHelper.addWorkout(d1);
        dataHelper.addSet(w1, ex1, 10, 5);
        dataHelper.addSet(w1, ex1, 11, 6);
        dataHelper.addSet(w1, ex2, 4, 3);

        Date d2 = new Date(System.currentTimeMillis());
        Workout w2 = dataHelper.addWorkout(d2);
        dataHelper.addSet(w2, ex1, 5, 4);

        List<ExerciseStat<Long>> ess=dataHelper.getExerciseStats(ex1.getId());
        assertNotNull(ess);
        assertEquals(2, ess.size());
        ExerciseStat<Long> es1=ess.get(0);
        assertEquals(d1,es1.getWorkoutDate());
        assertEquals(2, es1.getSetCount());
        assertEquals(21, es1.getRepCount());
        assertEquals(116, es1.getTotalWeight().longValue());
        assertEquals(6, es1.getMaxWeight().longValue());

        ExerciseStat<Long> es2=ess.get(1);
        assertEquals(d2, es2.getWorkoutDate());
        assertEquals(1, es2.getSetCount());
        assertEquals(5,es2.getRepCount());
        assertEquals(20,es2.getTotalWeight().longValue());
        assertEquals(4, es2.getMaxWeight().longValue());

        ess=dataHelper.getExerciseStats(ex2.getId());
        assertNotNull(ess);
        assertEquals(1, ess.size());
        es1=ess.get(0);
        assertEquals(d1,es1.getWorkoutDate());
        assertEquals(1, es1.getSetCount());
        assertEquals(4, es1.getRepCount());
        assertEquals(12, es1.getTotalWeight().longValue());
        assertEquals(3, es1.getMaxWeight().longValue());
    }
}