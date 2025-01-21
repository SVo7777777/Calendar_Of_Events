package com.example.calendarofevents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivityTwo extends AppCompatActivity {

    boolean addRecord;
    EditText textMultiline;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.review), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textMultiline = findViewById(R.id.editTextTextMultiLine2);
        //считываем с файла всё что есть
        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        addRecord = false;
        StringBuilder sb = new StringBuilder();
        if (exists) {
            try (FileInputStream fis = openFileInput("event_diary.txt");
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null)
                    if (line.equals(""))
                        System.out.println("пусто");
                    else
                        sb.append(line).append('\n');
                String infile = sb.toString();
                textMultiline.setText(infile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            Toast.makeText(this, "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите! ", Toast.LENGTH_LONG).show();
            System.out.println("pass");
        }
    }



}