package com.github.jpmoresmau.jpworkoutlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.jpmoresmau.jpworkoutlog.model.ExSet;


/**
 * Show the last set and button to remove it
 * @author jpmoresmau
 */
public class LastSetFragment extends Fragment {
    private ExSet lastSet;

    public LastSetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_last_set, container, false);
        setLastSet(v,lastSet);
        return v;
    }

    /**
     * set last set info with current view
     * @param set
     */
    public void setLastSet(ExSet set){
        setLastSet(getView(),set);
    }

    /**
     * set last set info if we have a view and a set
     * @param v
     * @param set
     */
    private void setLastSet(View v,ExSet set){
        lastSet=set;
        if (v!=null) {
            TextView tv = (TextView) v.findViewById(R.id.last_set_text);
            Button br = (Button) v.findViewById(R.id.last_set_remove);
            if (tv != null && br != null) {
                if (set == null) {
                    tv.setText("");
                    br.setVisibility(View.INVISIBLE);
                } else {
                    String s = getResources().getString(R.string.last_set);
                    String fs = String.format(s, set.getExercise().getName(), set.getReps(), SettingsActivity.getWeigthInUserUnits(getActivity(), set.getWeight()), SettingsActivity.getUnitName(getActivity()));
                    tv.setText(fs);
                    br.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
