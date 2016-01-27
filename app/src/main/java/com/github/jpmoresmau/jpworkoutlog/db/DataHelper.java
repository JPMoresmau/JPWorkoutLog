package com.github.jpmoresmau.jpworkoutlog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.github.jpmoresmau.jpworkoutlog.model.ExSet;
import com.github.jpmoresmau.jpworkoutlog.model.Exercise;
import com.github.jpmoresmau.jpworkoutlog.model.ExerciseStat;
import com.github.jpmoresmau.jpworkoutlog.model.GlobalStats;
import com.github.jpmoresmau.jpworkoutlog.model.SetInfo;
import com.github.jpmoresmau.jpworkoutlog.model.Workout;
import com.github.jpmoresmau.jpworkoutlog.model.WorkoutStat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jpmoresmau on 1/18/16.
 */
public class DataHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Workout.db";

    private Map<Long,Exercise> exerciseCache=new HashMap<>();

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataContract.ExerciseEntry.CREATE_TABLE);
        db.execSQL(DataContract.WorkoutEntry.CREATE_TABLE);
        db.execSQL(DataContract.SetEntry.CREATE_TABLE);
        db.execSQL(DataContract.SetEntry.CREATE_INDEX_WORKOUT);
        db.execSQL(DataContract.ExerciseLatestEntry.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // NOOP
    }

    public List<Exercise> listExercises(){
        SQLiteDatabase db=getReadableDatabase();
        String[] proj=new String[]{DataContract.ExerciseEntry._ID,DataContract.ExerciseEntry.COLUMN_NAME};
        String sortOrder=DataContract.ExerciseEntry.COLUMN_NAME;

        Cursor c=db.query(DataContract.ExerciseEntry.TABLE_NAME,
                proj,
                null,null,
                null,
                null,
                sortOrder
                );
        try {
            List<Exercise> l = new ArrayList<>();

            while (c.moveToNext()) {
                Exercise ex=new Exercise(c.getLong(0), c.getString(1));
                l.add(ex);
                exerciseCache.put(ex.getId(),ex);
            }
            return l;
        } finally {
            c.close();
        }

    }

    public Exercise getExercise(String name){
        SQLiteDatabase db=getReadableDatabase();
        String[] proj=new String[]{DataContract.ExerciseEntry._ID,DataContract.ExerciseEntry.COLUMN_NAME};
        String sortOrder=DataContract.ExerciseEntry.COLUMN_NAME;

        Cursor c=db.query(DataContract.ExerciseEntry.TABLE_NAME,
                proj,
                DataContract.ExerciseEntry.COLUMN_NAME + " = ?",
                new String[]{name},
                null,
                null,
                sortOrder
        );
        try {
            if (c.moveToNext()) {
                return new Exercise(c.getLong(0), c.getString(1));
            }
        } finally {
            c.close();
        }
        return null;
    }

    public Exercise getExercise(long id){
        Exercise ex=exerciseCache.get(id);
        if (ex==null) {
            SQLiteDatabase db = getReadableDatabase();
            String[] proj = new String[]{DataContract.ExerciseEntry._ID, DataContract.ExerciseEntry.COLUMN_NAME};
            String sortOrder = DataContract.ExerciseEntry.COLUMN_NAME;

            Cursor c = db.query(DataContract.ExerciseEntry.TABLE_NAME,
                    proj,
                    DataContract.ExerciseEntry._ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    sortOrder
            );
            try {
                if (c.moveToNext()) {
                    ex=new Exercise(c.getLong(0), c.getString(1));
                    exerciseCache.put(ex.getId(),ex);
                }
            } finally {
                c.close();
            }

        }
        return ex;
    }

    public Exercise addExercise(String name){
        if (name==null || name.trim().length()==0){
            throw new IllegalArgumentException("name");
        }
        SQLiteDatabase db=getWritableDatabase();
        ContentValues vals=new ContentValues();
        vals.put(DataContract.ExerciseEntry.COLUMN_NAME,name);
        long newID=db.insert(DataContract.ExerciseEntry.TABLE_NAME, null, vals);
        if (newID==-1) {
            return getExercise(name);
        }
        return new Exercise(newID,name);
    }

    public List<Workout> listWorkouts(){
        SQLiteDatabase db=getReadableDatabase();
        String[] proj=new String[]{DataContract.WorkoutEntry._ID,DataContract.WorkoutEntry.COLUMN_DATE};
        String sortOrder=DataContract.WorkoutEntry.COLUMN_DATE+" ASC";

        Cursor c=db.query(DataContract.WorkoutEntry.TABLE_NAME,
                proj,
                null, null,
                null,
                null,
                sortOrder
        );
        try {
            List<Workout> l = new ArrayList<>();

            while (c.moveToNext()) {
                l.add(new Workout(c.getLong(0), new Date(c.getLong(1))));
            }
            return l;
        } finally {
            c.close();
        }

    }

    public Workout getWorkout(Date d){
        SQLiteDatabase db=getReadableDatabase();
        String[] proj=new String[]{DataContract.WorkoutEntry._ID,DataContract.WorkoutEntry.COLUMN_DATE};
        String sortOrder=DataContract.WorkoutEntry.COLUMN_DATE;

        Cursor c=db.query(DataContract.WorkoutEntry.TABLE_NAME,
                proj,
                DataContract.WorkoutEntry.COLUMN_DATE + " = ?",
                new String[]{String.valueOf(d.getTime())},
                null,
                null,
                sortOrder
        );
        try {
            if (c.moveToNext()) {
                return new Workout(c.getLong(0), new Date(c.getLong(1)));
            }
        } finally {
            c.close();
        }
        return null;
    }

    public Workout addWorkout(Date date){
        if (date==null){
            throw new IllegalArgumentException("date");
        }
        SQLiteDatabase db=getWritableDatabase();
        ContentValues vals=new ContentValues();
        vals.put(DataContract.WorkoutEntry.COLUMN_DATE,date.getTime());
        long newID=db.insert(DataContract.WorkoutEntry.TABLE_NAME, null, vals);
        if (newID==-1){
            return getWorkout(date);
        }
        return new Workout(newID,date);
    }

    public List<ExSet> listSets(Workout w){
        SQLiteDatabase db=getReadableDatabase();
        String[] proj=new String[]{DataContract.SetEntry._ID,
                DataContract.SetEntry.COLUMN_EXERCISE, DataContract.SetEntry.COLUMN_REPS,DataContract.SetEntry.COLUMN_WEIGHT};
        String sortOrder=DataContract.SetEntry._ID;


        Cursor c=db.query(DataContract.SetEntry.TABLE_NAME,
                proj,
                DataContract.SetEntry.COLUMN_WORKOUT + " = ?",
                new String[]{String.valueOf(w.getId())},
                null,
                null,
                sortOrder
        );
        try {
            List<ExSet> l = new ArrayList<>();
            while (c.moveToNext()) {
                l.add(new ExSet(c.getLong(0), w, getExercise(c.getLong(1)), c.getInt(2), c.getLong(3)));
            }
            return l;

        } finally {
            c.close();
        }

    }

    public ExSet addSet(Workout w,Exercise e,int reps, long weight){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues vals=new ContentValues();
        vals.put(DataContract.SetEntry.COLUMN_WORKOUT,w.getId());
        vals.put(DataContract.SetEntry.COLUMN_EXERCISE,e.getId());
        vals.put(DataContract.SetEntry.COLUMN_REPS,reps);
        vals.put(DataContract.SetEntry.COLUMN_WEIGHT,weight);
        long newID=db.insert(DataContract.SetEntry.TABLE_NAME, null, vals);

        vals=new ContentValues();
        vals.put(DataContract.ExerciseLatestEntry.COLUMN_REPS,reps);
        vals.put(DataContract.ExerciseLatestEntry.COLUMN_WEIGHT, weight);
        int aff=db.update(DataContract.ExerciseLatestEntry.TABLE_NAME, vals, DataContract.ExerciseLatestEntry.COLUMN_EXERCISE + "=?", new String[]{String.valueOf(e.getId())});
        if (aff==0){
            vals.put(DataContract.ExerciseLatestEntry.COLUMN_EXERCISE,e.getId());
            db.insert(DataContract.ExerciseLatestEntry.TABLE_NAME,null,vals);
        }

        return new ExSet(newID,w,e,reps,weight);

    }

    public Map<Long,SetInfo<Long>> getLatestInfoByExercise(){
        SQLiteDatabase db=getReadableDatabase();
        String[] proj=new String[]{
                DataContract.ExerciseLatestEntry.COLUMN_EXERCISE, DataContract.ExerciseLatestEntry.COLUMN_REPS,DataContract.ExerciseLatestEntry.COLUMN_WEIGHT};
        Cursor c=db.query(DataContract.ExerciseLatestEntry.TABLE_NAME,
                proj,
                null,
                null,
                null,
                null,
                null
        );
        try {
            Map<Long,SetInfo<Long>> m = new HashMap<>();
            while (c.moveToNext()) {
                m.put(c.getLong(0),new SetInfo<>(c.getInt(1), c.getLong(2)));
            }
            return m;

        } finally {
            c.close();
        }
    }

    public GlobalStats getGlobalStats(){
        SQLiteDatabase db=getReadableDatabase();

        Cursor c=db.query(DataContract.WorkoutEntry.TABLE_NAME,
                new String[]{"min("+DataContract.WorkoutEntry.COLUMN_DATE+")","max("+DataContract.WorkoutEntry.COLUMN_DATE+")","count(*)"},
                null,
                null,
                null,
                null,
                null
        );
        try {
            if (c.moveToFirst()){
                return new GlobalStats(new Date(c.getLong(0)),new Date(c.getLong(1)),c.getInt(2));
            }
        } finally {
            c.close();
        }
        return new GlobalStats(null,null,0);
    }

    public List<WorkoutStat<Long>> getWorkoutStats(){
        SQLiteDatabase db=getReadableDatabase();
        String s="select w."+DataContract.WorkoutEntry.COLUMN_DATE+","+
                "count(DISTINCT s."+DataContract.SetEntry.COLUMN_EXERCISE+"),"+
                "count(s."+DataContract.SetEntry._ID+"),"+
                "sum(s. "+DataContract.SetEntry.COLUMN_REPS+"),"+
                "sum(s."+DataContract.SetEntry.COLUMN_WEIGHT+")"+
                " from "+DataContract.WorkoutEntry.TABLE_NAME+" w LEFT OUTER JOIN  "
                + DataContract.SetEntry.TABLE_NAME + " s on w."+DataContract.WorkoutEntry._ID+" = s."+DataContract.SetEntry.COLUMN_WORKOUT
                + " GROUP BY w."+DataContract.WorkoutEntry.COLUMN_DATE
                + " ORDER BY w."+DataContract.WorkoutEntry.COLUMN_DATE;

        Cursor c=db.rawQuery(s, null);
        try {
            List<WorkoutStat<Long>> ls=new LinkedList<>();
            while (c.moveToNext()){
                Date d=new Date(c.getLong(0));
                int exCount=c.getInt(1);
                int setCount=c.getInt(2);
                int repCount=c.getInt(3);
                long weight=c.getLong(4);
                ls.add(new WorkoutStat<Long>(d,exCount,setCount,repCount,weight));
            }
            return ls;
        } finally {
            c.close();
        }
    }

    public List<ExerciseStat<Long>> getExerciseStats(long exerciseID){
        SQLiteDatabase db=getReadableDatabase();
        String s="select w."+DataContract.WorkoutEntry.COLUMN_DATE+","+
                "count(s."+DataContract.SetEntry._ID+"),"+
                "sum(s. "+DataContract.SetEntry.COLUMN_REPS+"),"+
                "sum(s."+DataContract.SetEntry.COLUMN_WEIGHT+")"+
                " from "+DataContract.WorkoutEntry.TABLE_NAME+" w LEFT OUTER JOIN  "
                + DataContract.SetEntry.TABLE_NAME + " s on w."+DataContract.WorkoutEntry._ID+" = s."+DataContract.SetEntry.COLUMN_WORKOUT
                + " WHERE "+DataContract.SetEntry.COLUMN_EXERCISE+" = ?"
                + " GROUP BY w."+DataContract.WorkoutEntry.COLUMN_DATE
                + " ORDER BY w."+DataContract.WorkoutEntry.COLUMN_DATE;

        Cursor c=db.rawQuery(s, new String[]{String.valueOf(exerciseID)});
        try {
            List<ExerciseStat<Long>> ls=new LinkedList<>();
            while (c.moveToNext()){
                Date d=new Date(c.getLong(0));
                int setCount=c.getInt(1);
                int repCount=c.getInt(2);
                long weight=c.getLong(3);
                ls.add(new ExerciseStat<Long>(d,setCount,repCount,weight));
            }
            return ls;
        } finally {
            c.close();
        }

    }


}
