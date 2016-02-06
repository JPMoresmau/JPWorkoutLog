package com.github.jpmoresmau.jpworkoutlog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.jpmoresmau.jpworkoutlog.db.DataHelper;
import com.github.jpmoresmau.jpworkoutlog.model.ExSet;
import com.github.jpmoresmau.jpworkoutlog.model.Exercise;
import com.github.jpmoresmau.jpworkoutlog.model.Workout;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Past workouts: show sets
 * @author jpmoresmau
 */
public class PastActivity extends Activity {
    public static String WORKOUT_LIST="WORKOUT_LIST";
    /**
     * full list of workouts, this is a reasonable length and small objects (no set information)
     */
    private ArrayList<Workout> workouts;
    /**
     * current workout index
     */
    private int idx=0;
    private DateFormat df=DateFormat.getDateInstance(DateFormat.LONG);
    private DataHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past);

        helper=new DataHelper(this);
        workouts=helper.listWorkouts();
        idx=workouts.size()-1;
        getActionBar().setDisplayHomeAsUpEnabled(true);
        displayWorkout();
    }

    private void displayWorkout(){
        Workout w=workouts.get(idx);
        TextView dt=(TextView)findViewById(R.id.workout_date);
        String s=String.format(getResources().getString(R.string.past_date),df.format(w.getDate()));
        dt.setText(s);

        Button next=(Button)findViewById(R.id.next);
        next.setVisibility(idx < workouts.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        Button previous=(Button)findViewById(R.id.previous);
        previous.setVisibility(idx > 0 ? View.VISIBLE : View.INVISIBLE);


        ListView lv=(ListView)findViewById(R.id.listView);
        final List<ExSet> sets=helper.listSets(w);
        if (sets.isEmpty()){
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{getResources().getString(R.string.no_past_set)});
            lv.setAdapter(aa);
        } else {
            ArrayAdapter<ExSet> aa = new ArrayAdapter<ExSet>(this, android.R.layout.simple_list_item_1, sets) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = null;
                    if (convertView instanceof TextView) {
                        tv = (TextView) convertView;
                    } else {
                        tv = new TextView(PastActivity.this);
                    }
                    ExSet set = this.getItem(position);
                    String s = getResources().getString(R.string.past_set);
                    String fs = String.format(s, set.getExercise().getName(), set.getReps(), SettingsActivity.getWeigthInUserUnits(PastActivity.this, set.getWeight()), SettingsActivity.getUnitName(PastActivity.this));
                    tv.setText(fs);
                    return tv;
                }
            };
            lv.setAdapter(aa);
        }
    }

    public void onPrevious(View v){
        idx--;
        displayWorkout();
    }

    public void onNext(View v){
        idx++;
        displayWorkout();
    }

}
