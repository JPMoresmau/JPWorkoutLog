package com.github.jpmoresmau.jpworkoutlog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.github.jpmoresmau.jpworkoutlog.model.RuntimeInfo;

/**
 * Created by jpmoresmau on 1/23/16.
 */
public class AddSetExerciseFragment extends DialogFragment {
    public static final String EXERCISES="exercises";


    private NewSetListener listener;

    public AddSetExerciseFragment() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.new_set);
        View view=inflater.inflate(R.layout.dialog_addsetexercise, null);

        final AutoCompleteTextView exText=(AutoCompleteTextView)view.findViewById(R.id.set_exercise);
        String[] execs=getArguments().getStringArray(EXERCISES);
        exText.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, execs));

        final EditText repsText=(EditText)view.findViewById(R.id.set_reps);
        final EditText weightText=(EditText)view.findViewById(R.id.set_weight);



        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.set_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String ex = exText.getText().toString();
                        try {
                            int reps = Integer.parseInt(repsText.getText().toString());
                            if (reps < 1) {
                                repsText.setError(getActivity().getResources().getString(R.string.error_reps));
                            } else {
                                try {
                                    double weight = Double.parseDouble(weightText.getText().toString());
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
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddSetExerciseFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof NewSetListener){
            listener=(NewSetListener)activity;
        }
    }

    public interface NewSetListener {
        void newSet(String ex,int reps,double weight);
    }

}
