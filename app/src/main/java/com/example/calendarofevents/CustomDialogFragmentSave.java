package com.example.calendarofevents;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class CustomDialogFragmentSave extends CustomDialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String stroka = getArguments().getString("data");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Сохранение!")
                .setMessage("Будет сохранено содержимое информационного поля этого экрана !")
                .setPositiveButton("Сохранить",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(stroka);

                    }
                })
                .setNegativeButton("Отменить",null)



                .create();
    }
}
