package com.example.calendarofevents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView textView;
    String[] name_month = {"month", "ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ","АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
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
        textView = findViewById(R.id.textView);
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
                String month = data.substring(index_first, index_second + 5);//-1-2025
                String year = data.substring(index_second + 1, index_second + 5);//-1-2025
                String number_month = data.substring(index_first+1, index_second);
                //поиск месяца по виду -01-2025
                StringBuilder month2 = new StringBuilder(month);
                month2.insert(1, '0');
                System.out.println(month2);
                int number = Integer.parseInt(number_month);
                System.out.println("номер"+number_month+"месяца");
                System.out.println(name_month[number]);
                textView.setText("   за " +name_month[number]+" "+year + "г.:");
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
                        boolean contains2 = line.contains(month2);
                        if ((contains) || (contains2)) {
                            System.out.println(data + "в файле есть");
                            sb.append(line + "\n");
                        }


                    }
                    textMultiline.setText(sb.toString());
                    if (sb.length() == 0) {
                        textMultiline.setText( "   НЕТ СОБЫТИЙ ЗА ЭТОТ  МЕСЯЦ!");
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
    //меню три точки вверху справа
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); // Replace 'menu_main' with your menu resource name
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_settings == id) {
            // Handle settings action
            Intent intent = new Intent(ReviewOnMonth.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (R.id.action_about == id) {
            // Handle about action
            Intent intent = new Intent(ReviewOnMonth.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}