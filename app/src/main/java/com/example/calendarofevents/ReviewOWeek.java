package com.example.calendarofevents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ReviewOWeek extends AppCompatActivity {

    boolean addRecord;
    TextView textMultiline;
    TextView textView;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_ondata);

        //верхняя полоса с названием и 3мя точками
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Use the toolbar as the app bar
        //цвет для 3ёх точек и для названия
        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);

        //toolbar.setTitle("hello");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.review), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textMultiline = findViewById(R.id.editTextTextMultiLine2);
        textView = findViewById(R.id.textView);
        addRecord = false;
        Intent intent = getIntent();
        int week = intent.getIntExtra("week", 0);
        System.out.println("week "+ week);

        Bundle args = intent.getBundleExtra("BUNDLE");
        assert args != null;
        String[] week_days = (String[]) args.getSerializable("ARRAYLIST");
        System.out.println((Arrays.toString(week_days))+" week_days");
        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        assert week_days != null;
        String weekDays = Arrays.toString(week_days);
        System.out.println("weekDays "+weekDays);
        String year = week_days[0].substring(6);;

        //System.out.println("week_days[0]"+result);
        //String data = String.valueOf((new MainActivity().textMultiline.getText()));

        //System.out.println(data.length());
        if (exists) {
            if ( week_days.equals("")) {
                Toast.makeText(this, "выберите дату на календаре", Toast.LENGTH_LONG).show();
                System.out.println("кнопка не работает");

            } else {
                textView.setText("  за " + week + "ую неделю " +year +"г.:");
                //считываем с файла всё что есть
                StringBuilder sb = new StringBuilder();
                try (FileInputStream fis = openFileInput("event_diary.txt");
                     InputStreamReader isr = new InputStreamReader(fis);
                     BufferedReader br = new BufferedReader(isr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        //есть ли в строке дата из массива дней недели
                        boolean contains = Arrays.stream(week_days).anyMatch(line::contains);
                        //boolean contains = line.contains(week_days);
                        if (contains)  {
                            String day = line.substring(0, 11);
                            String event = line.substring(11);
                            String str =  "<span style=\"background-color:#f3f402;\">" + day + "</span>" + event+ " <br>";
                            sb.append(str);
                        }


                    }
                    //
                    //textMultiline.setText(sb.toString());
                    textMultiline.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));

                    if (sb.length() == 0) {
                        textMultiline.setText("   НЕТ СОБЫТИЙ ЗА ЭТУ НЕДЕЛЮ!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }else {
            Toast.makeText(this, "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();
            System.out.println("pass");
        }

    }
    //меню три точки вверху справа
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); // Replace 'menu_main' with your menu resource name
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_settings == id) {
            // Handle settings action
            Intent intent = new Intent(ReviewOWeek.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (R.id.action_about == id) {
            // Handle about action
            Intent intent = new Intent(ReviewOWeek.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}