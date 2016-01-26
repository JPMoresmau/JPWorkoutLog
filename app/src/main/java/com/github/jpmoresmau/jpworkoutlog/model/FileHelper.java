package com.github.jpmoresmau.jpworkoutlog.model;

import android.content.Context;
import android.os.Environment;

import com.github.jpmoresmau.jpworkoutlog.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by jpmoresmau on 1/26/16.
 */
public class FileHelper {
    private Context ctx;

    public FileHelper(Context ctx) {
        this.ctx = ctx;
    }

    /** Checks if external storage is available for read and write **/
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getExportFile(String rootName) {
        String d= DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
        d=d.replace('/','-');
        String fileName=rootName+"-"+d+".csv";

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), fileName);
        file.getParentFile().mkdirs();
        return file;
    }

    public File writeWorkoutStats(WorkoutStats wss) throws IOException{
        File f=getExportFile("workouts");
        DateFormat df=DateFormat.getDateInstance(DateFormat.SHORT);
        BufferedWriter bw=new BufferedWriter(new FileWriter(f));
        try {
            writeLine(bw, ctx.getResources().getString(R.string.date)
                    , ctx.getResources().getString(R.string.exercises)
                    , ctx.getResources().getString(R.string.sets)
                    , ctx.getResources().getString(R.string.reps)
                    , ctx.getResources().getString(R.string.weight));
            for (WorkoutStat<Double> ws : wss.getStats()) {
                writeLine(bw, df.format(ws.getWorkoutDate())
                        , String.valueOf(ws.getExerciseCount())
                        , String.valueOf(ws.getSetCount())
                        , String.valueOf(ws.getRepCount())
                        , String.valueOf(ws.getTotalWeight()));
            }
        } finally {
            bw.close();
        }
        return f;
    }

    public File writeExerciseStats(Exercise e,ExerciseStats ess) throws IOException{
        File f=getExportFile(e.getName());
        DateFormat df=DateFormat.getDateInstance(DateFormat.SHORT);
        BufferedWriter bw=new BufferedWriter(new FileWriter(f));
        try {
            writeLine(bw, ctx.getResources().getString(R.string.date)
                    , ctx.getResources().getString(R.string.sets)
                    , ctx.getResources().getString(R.string.reps)
                    , ctx.getResources().getString(R.string.weight));
            for (ExerciseStat<Double> es : ess.getStats()) {
                writeLine(bw, df.format(es.getWorkoutDate())
                        , String.valueOf(es.getSetCount())
                        , String.valueOf(es.getRepCount())
                        , String.valueOf(es.getTotalWeight()));
            }
        } finally {
            bw.close();
        }
        return f;
    }

    private void writeLine(BufferedWriter w,String... vs) throws IOException{
        String sep="";
        for (String v:vs){
            w.write(sep);
            sep=",";
            writeCell(w,v);
        }
        w.newLine();
    }

    private void writeCell(Writer w,String v) throws IOException{
        if (v.contains(",")){
            v="\""+v+"\"";
        }
        w.write(v);
    }
}