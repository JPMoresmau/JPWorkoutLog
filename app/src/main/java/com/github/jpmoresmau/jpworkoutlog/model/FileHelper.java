package com.github.jpmoresmau.jpworkoutlog.model;

import android.content.Context;
import android.os.Environment;

import com.github.jpmoresmau.jpworkoutlog.R;
import com.github.jpmoresmau.jpworkoutlog.SettingsActivity;
import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;

/**
 * Helper for file operations
 * @author jpmoresmau
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

    /**
     * get the file for export, given the file stub name
     * @param stubName
     * @return
     */
    public File getExportFile(String stubName) {
        String d= DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
        d=d.replace('/','-');
        String fileName=stubName+"-"+d+".csv";
        File root=Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File dir=new File(root,ctx.getResources().getString(R.string.file_prefix));
        dir.mkdirs();
        File file = new File(dir, fileName);
        return file;
    }

    /**
     * write workout statistics, the file stub name is workouts
     * @param wss
     * @return
     * @throws IOException
     */
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
                        , Utils.formatWeight(ws.getTotalWeight()));
            }
        } finally {
            bw.close();
        }
        return f;
    }

    /**
     * write exercise statistics, the file stub name is the exercise name
     * @param e
     * @param ess
     * @return
     * @throws IOException
     */
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
                        , Utils.formatWeight(es.getTotalWeight()));
            }
        } finally {
            bw.close();
        }
        return f;
    }

    /**
     * Write all statistics, the file stub name is All
     * @param dataHelper
     * @return
     * @throws IOException
     */
    public File writeAllStats(DataHelper dataHelper) throws IOException{
        File f=getExportFile(ctx.getResources().getString(R.string.all_workouts));
        DateFormat df=DateFormat.getDateInstance(DateFormat.SHORT);
        BufferedWriter bw=new BufferedWriter(new FileWriter(f));
        try {
            writeLine(bw, ctx.getResources().getString(R.string.date)
                    , ctx.getResources().getString(R.string.exercise)
                    , ctx.getResources().getString(R.string.set)
                    , ctx.getResources().getString(R.string.reps)
                    , ctx.getResources().getString(R.string.weight));
            for (Workout w:dataHelper.listWorkouts()){
                int ix=1;
                for (ExSet es : dataHelper.listSets(w)) {
                    writeLine(bw, df.format(w.getDate())
                            , es.getExercise().getName()
                            , String.valueOf(ix++)
                            , String.valueOf(es.getReps())
                            , String.valueOf(SettingsActivity.getWeigthInUserUnits(ctx,es.getWeight())));
                }
            }
        } finally {
            bw.close();
        }
        return f;

    }

    /**
     * Write a CSV line, using a comma as a separator
     * @param w
     * @param vs
     * @throws IOException
     */
    private void writeLine(BufferedWriter w,String... vs) throws IOException{
        String sep="";
        for (String v:vs){
            w.write(sep);
            sep=",";
            writeCell(w,v);
        }
        w.newLine();
    }

    /**
     * write a CSV cell
     * @param w
     * @param v
     * @throws IOException
     */
    private void writeCell(Writer w,String v) throws IOException{
        if (v.contains(",")){
            v="\""+v+"\"";
        }
        w.write(v);
    }
}
