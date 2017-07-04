package br.com.senac.cademeulivro.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.activity.container.CadastroPagerActivity;

public class DatePickerFragment extends DialogFragment {
    private DatePicker mDatePicker;
    private Calendar c;

    public static DatePickerFragment newInstance(Date d) {
        Bundle args = new Bundle();
        args.putSerializable("data", d);
        DatePickerFragment frag = new DatePickerFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable("data");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.h_custom_dialog_picker, null);

        c = Calendar.getInstance();
        if(date != null) {
            c.setTime(date);
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        mDatePicker = (DatePicker) v.findViewById(R.id.pickerData);
        mDatePicker.init(year,month,day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Escolha uma data")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c.set(Calendar.YEAR,mDatePicker.getYear());
                        c.set(Calendar.MONTH, mDatePicker.getMonth());
                        c.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
                        Date dtSelecionada = c.getTime();
                        ((CadastroPagerActivity) getActivity()).setDataResult(dtSelecionada);
                    }
                })
                .create();
    }
}
