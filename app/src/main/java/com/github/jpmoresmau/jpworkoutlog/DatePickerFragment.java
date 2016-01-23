package com.github.jpmoresmau.jpworkoutlog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 *
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd= new DatePickerDialog(getActivity(), this, year, month, day){
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                setTitle(R.string.workout_date);
            }
        };
        dpd.setTitle(R.string.workout_date);
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

        return dpd;
    }

    public void onDateSet(DatePicker dp, int year, int month, int day) {
        Intent intent=new Intent(getActivity(),WorkoutActivity.class);
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,dp.getYear());
        c.set(Calendar.MONTH,dp.getMonth());
        c.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
        // one workout per day
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        intent.putExtra(WorkoutActivity.WORKOUT_DATE, c.getTime().getTime());
        getActivity().startActivity(intent);
    }
}
