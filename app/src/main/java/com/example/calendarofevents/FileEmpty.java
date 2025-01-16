package com.example.calendarofevents;

import android.annotation.SuppressLint;
public class FileEmpty {
    @SuppressLint("SdCardPath")
    private static final String APP_SD_PATH = "/data/data/com.example.calendarofevents";
    public static boolean fileExistsInSD(String sFileName){
        System.out.println("проверка файла!");
        //Environment.getExternalStorageDirectory().toString()
        String sFolder =  APP_SD_PATH + "/files";
        String sFile=sFolder+"/"+sFileName;
        java.io.File file = new java.io.File(sFile);
        System.out.println(sFile);
        return file.exists();
    }
}
