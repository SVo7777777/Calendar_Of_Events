package com.example.calendarofevents;
import static android.os.ParcelFileDescriptor.MODE_APPEND;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

public class FileEmpty {
    public static boolean check() throws FileNotFoundException {

       try (InputStream is = new FileInputStream("event_diary.txt")) {
           System.out.println("проверка файла!");

           if (is.read() == -1) {
               System.out.println("The file is empty!");
           } else {
               System.out.println("The file is  NOT empty!");
               // The file is NOT empty!
           }
       }  catch (IOException e) {
           throw new RuntimeException(e);
       }
        return false;
    }

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
