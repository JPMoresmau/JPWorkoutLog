package com.github.jpmoresmau.jpworkoutlog.db;

import android.provider.BaseColumns;

/**
 * Created by jpmoresmau on 1/18/16.
 */
public final class DataContract {

    private DataContract(){

    }

    public static abstract class ExerciseEntry implements BaseColumns {
        public static String TABLE_NAME="exercises";
        public static String COLUMN_NAME="name";

        public static String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+ "("+
                _ID +" INTEGER PRIMARY KEY,"+
                COLUMN_NAME+" TEXT NOT NULL UNIQUE)";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public static abstract class WorkoutEntry implements BaseColumns {
        public static String TABLE_NAME="workouts";
        public static String COLUMN_DATE="wdate";

        public static String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+ "("+
                _ID +" INTEGER PRIMARY KEY,"+
                COLUMN_DATE+" INTEGER NOT NULL UNIQUE)";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class SetEntry implements BaseColumns {
        public static String TABLE_NAME="sets";
        public static String COLUMN_WORKOUT="workout";
        public static String COLUMN_EXERCISE="exercise";
        public static String COLUMN_REPS="reps";
        public static String COLUMN_WEIGHT="weight";

        public static String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+ "("+
                _ID +" INTEGER PRIMARY KEY,"+
                COLUMN_WORKOUT+" INTEGER NOT NULL,"+
                COLUMN_EXERCISE+" INTEGER NOT NULL, "+
                COLUMN_REPS+" INTEGER NOT NULL, "+
                COLUMN_WEIGHT+" INTEGER NOT NULL)";

        public static String CREATE_INDEX_WORKOUT="CREATE INDEX "+TABLE_NAME+ "_woidx ON "+TABLE_NAME+ "("+
                COLUMN_WORKOUT+")";


        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class ExerciseLatestEntry implements BaseColumns {
        public static String TABLE_NAME="latest";
        public static String COLUMN_EXERCISE="exercise";
        public static String COLUMN_REPS="reps";
        public static String COLUMN_WEIGHT="weight";

        public static String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+ "("+
                COLUMN_EXERCISE+" INTEGER NOT NULL UNIQUE, "+
                COLUMN_REPS+" INTEGER NOT NULL, "+
                COLUMN_WEIGHT+" INTEGER NOT NULL)";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
