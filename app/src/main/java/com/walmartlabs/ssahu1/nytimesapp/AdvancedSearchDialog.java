package com.walmartlabs.ssahu1.nytimesapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Switch;

import java.util.Calendar;

/**
 * Created by ssahu6 on 5/30/16.
 */
public class AdvancedSearchDialog extends  DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePicker datePicker;
    private Switch order;
    private RadioButton rbArts;
    private RadioButton rbFashion;
    private RadioButton rbSports;

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getFragmentManager(),"datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public AdvancedSearchDialog(){

    }

    public static AdvancedSearchDialog newInstance(String title) {
        AdvancedSearchDialog frag = new AdvancedSearchDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.advanced_search, container);
    }


}
