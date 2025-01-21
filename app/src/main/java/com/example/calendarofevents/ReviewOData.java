package com.example.calendarofevents;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class ReviewOData extends AppCompatActivity {

    boolean addRecord;
    EditText textMultiline;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_ondata);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.review), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textMultiline = findViewById(R.id.editTextTextMultiLine2);
        addRecord = false;
        Intent intent = getIntent();
        String   data = intent.getStringExtra("data");

        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        //String data = String.valueOf((new MainActivity().textMultiline.getText()));
        System.out.println(data);
        System.out.println(data.length());
        if (exists) {
            if ((data.length() >= 10) && (data.length() <= 12)) {
                //считываем с файла всё что есть
                StringBuilder sb = new StringBuilder();
                try (FileInputStream fis = openFileInput("event_diary.txt");
                     InputStreamReader isr = new InputStreamReader(fis);
                     BufferedReader br = new BufferedReader(isr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        boolean contains = line.contains(data);
                        if (contains) {
                            sb.append(line);
                            String infile = sb.toString();
                            textMultiline.setText(infile);
                            break;
                        } else {
                            textMultiline.setText(data + "нет событий в этот день");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "выберите дату на календаре", Toast.LENGTH_LONG).show();
                System.out.println("кнопка не работает");
            }
        }else {
            Toast.makeText(this, "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();
            System.out.println("pass");
        }

    }



}