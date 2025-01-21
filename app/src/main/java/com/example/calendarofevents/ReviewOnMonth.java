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

public class ReviewOnMonth extends AppCompatActivity {

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
        //проссмотр за месяц
        addRecord = false;
        Intent intent = getIntent();
        String   data = intent.getStringExtra("data");;

        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");

        System.out.println(data.length());
        if (exists) {
            if ((data.length() >= 10) && (data.length() <= 12)) {
                int index_first = data.indexOf("-");
                int index_second = data.indexOf("-", index_first + 1);
                String month = data.substring(index_first, index_second + 5);
                //считываем с файла всё что есть
                StringBuilder sb = new StringBuilder();
                try (FileInputStream fis = openFileInput("event_diary.txt");
                     InputStreamReader isr = new InputStreamReader(fis);
                     BufferedReader br = new BufferedReader(isr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(data + "мы тут");
                        System.out.println(line);
                        System.out.println(data);
                        boolean contains = line.contains(month);
                        if (contains) {
                            System.out.println(data + "в файле есть");
                            sb.append(line + "\n");
                        }
                        String infile = sb.toString();
                        textMultiline.setText(infile);
                    }
                    if (sb.length() == 0) {
                        textMultiline.setText(data + "нет событий в этом месяце!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(this, "Выберите дату на календаре!", Toast.LENGTH_LONG).show();
                System.out.println("кнопка не работает");
            }
        }else {
            Toast.makeText(this, "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();

        }
    }


}