package com.github.jpmoresmau.jpworkoutlog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jpmoresmau.jpworkoutlog.model.SetInfo;
import com.github.jpmoresmau.jpworkoutlog.model.Utils;

import java.util.Map;

/**
 * UI to add a set to the workout
 * @author jpmoresmau
 */
public class AddSetExerciseFragment extends DialogFragment {
    public static final String EXERCISES="exercises";
    public static final String LAST_EXERCISE="last_exercise";
    public static final String LAST_EXERCISES="last_exercises";


    private NewSetListener listener;

    private AutoCompleteTextView exText;
    private EditText repsText;
    private EditText weightText;

    public AddSetExerciseFragment() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.new_set);
        View view=inflater.inflate(R.layout.dialog_addsetexercise, null);

        exText=(AutoCompleteTextView)view.findViewById(R.id.set_exercise);
        String[] execs=getArguments().getStringArray(EXERCISES);
        exText.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, execs));


        repsText=(EditText)view.findViewById(R.id.set_reps);

        weightText=(EditText)view.findViewById(R.id.set_weight);

        // latest info by exercise
        final Map<String, SetInfo<Double>> exerciseLatest=(Map<String, SetInfo<Double>>)getArguments().getSerializable(LAST_EXERCISES);
        // prepopulate if we know the last exercise
        String l=getArguments().getString(LAST_EXERCISE);
        if (l!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                exText.setText(l,false);
            } else {
                exText.setText(l);
            }
            exText.setSelection(0,l.length());
            SetInfo<Double> si=exerciseLatest.get(l);
            if (si!=null){
                String sr=String.valueOf(si.getReps());
                repsText.setText(sr);
                repsText.setSelection(0, sr.length());
                String sw= Utils.formatWeight(si.getWeight());
                weightText.setText(sw);
                weightText.setSelection(0, sw.length());
            }
        }

        // go from exercise to reps, populating with latest info for exercise
        exText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    SetInfo<Double> si = exerciseLatest.get(exText.getText().toString());
                    if (si != null) {
                        String sr=String.valueOf(si.getReps());
                        repsText.setText(sr);
                        repsText.setSelection(0, sr.length());
                        String sw=Utils.formatWeight(si.getWeight());
                        weightText.setText(sw);
                        weightText.setSelection(0,sw.length());
                    } else {
                        repsText.setText("");
                        weightText.setText("");
                    }
                }
                return handled;
            }
        });

        // weight is the last text view, triggers the addition
        weightText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addSet();
                    handled = true;
                }
                return handled;
            }
        });


        // wire buttons
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.set_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addSet();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddSetExerciseFragment.this.getDialog().cancel();
                    }
                })
                .setNeutralButton(R.string.clear, null)
        ;



        final AlertDialog d=builder.create();
        // show keyboard
        exText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });


        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                exText.requestFocus();
                // clear info
                Button b = d.getButton(AlertDialog.BUTTON_NEUTRAL);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        exText.setText("");
                        repsText.setText("");
                        weightText.setText("");
                        exText.requestFocus();
                    }
                });
            }
        });



        return d;

    }

    /**
     * add a set from the entered information if correct
     */
    private void addSet(){
        String ex = exText.getText().toString();
        try {
            int reps = Integer.parseInt(repsText.getText().toString());
            if (reps < 1) {
                repsText.setError(getActivity().getResources().getString(R.string.error_reps));
            } else {
                try {
                    double weight = Utils.parseWeight(weightText.getText().toString());
                    if (listener != null) {
                        listener.newSet(ex, reps, weight);
                    }
                    AddSetExerciseFragment.this.getDialog().dismiss();
                } catch (NumberFormatException nfe) {
                    weightText.setError(getActivity().getResources().getString(R.string.error_weight));
                }
            }
        } catch (NumberFormatException nfe) {
            repsText.setError(getActivity().getResources().getString(R.string.error_reps));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof NewSetListener){
            listener=(NewSetListener)activity;
        }
    }

    /**
     * listener for set creation
     */
    public interface NewSetListener {
        void newSet(String ex,int reps,double weight);
    }

}
