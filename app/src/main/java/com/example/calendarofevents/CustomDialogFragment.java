package com.example.calendarofevents;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("ВНИМАНИЕ!")
                .setMessage("ЗАПИСЬ УСПЕШНО ВНЕСЕНА!")
                .setPositiveButton("OK", null)
                .setNegativeButton("",null)



                .create();
    }
}

