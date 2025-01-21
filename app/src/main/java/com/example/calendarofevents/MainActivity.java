package com.example.calendarofevents;

import static android.app.PendingIntent.getActivity;
import static android.view.KeyEvent.KEYCODE_ENTER;

import static java.lang.Character.toUpperCase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;


public class MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    String curDate;
    public EditText textMultiline;
    EditText editTextInput;
    private Object MotionEvent;
    boolean addRecord;
    String[] data = {"проссмотреть", "проссмотреть за день", "проссмотреть за месяц", "проссмотреть за год"};
    //boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Use the toolbar as the app bar

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.menu_review);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент
        //spinner.setSelection(0);
        // устанавливаем обработчик нажатия spinner menu_review
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(30);


                String data = String.valueOf(textMultiline.getText());
                boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
                if (exists) {
                    if ((data.length() >= 10) && (data.length() <= 12)) {
                        Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                        switch (position) {
                            case 0:
                                // Whatever you want to happen when the first item gets selected
                                break;
                            case 1:

                                Intent intent = new Intent(MainActivity.this, ReviewOData.class);
                                intent.putExtra("data", data);
                                spinner.setSelection(0);
                                startActivity(intent);
                                break;
                            case 2:
                                Intent intent2 = new Intent(MainActivity.this, ReviewOnMonth.class);
                                intent2.putExtra("data", data);
                                spinner.setSelection(0);
                                startActivity(intent2);
                                break;

                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Выберите дату на календаре!", Toast.LENGTH_LONG).show();
                        System.out.println("кнопка не работает");
                    }
                } else {
                    Toast.makeText(getBaseContext(), "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        calendarView = findViewById(R.id.calendarView10);
        textMultiline = findViewById(R.id.editTextTextMultiLine8);
        Calendar ci = Calendar.getInstance();
        //вывод текущей даты в поле информации при запуске приложения
        String CiDateTime = ci.get(Calendar.DAY_OF_MONTH) + "-" + (ci.get(Calendar.MONTH) + 1) + "-" + ci.get(Calendar.YEAR) + ": ";
        textMultiline.setText(CiDateTime);
        addRecord = false;
        editTextInput = findViewById(R.id.editTextInput);
        //поиск по слову  по нажатию на ENTER или OK
        editTextInput.setOnKeyListener(new View.OnKeyListener() {
            @SuppressLint("SetTextI18n")
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean consumed = false;
                boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
                if (keyCode == KEYCODE_ENTER) {
                    Editable word = editTextInput.getText();//Делаем то, что нам нужно...
                    StringBuilder sb = new StringBuilder();
                    if (exists) {
                        try (FileInputStream fis = openFileInput("event_diary.txt");
                             InputStreamReader isr = new InputStreamReader(fis);
                             BufferedReader br = new BufferedReader(isr)) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                boolean contains = line.contains(word);
                                if (contains) {
                                    sb.append(line + "\n");
                                }
                            }
                            textMultiline.setText(sb.toString());
                            if (sb.length() == 0) {
                                textMultiline.setText("По слову '" + word + "' ничего не найдено. Попробуйте ввести первые несколько букв слова.");
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        textMultiline.setText("В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!");
                    }
                    consumed = true; //это если не хотим, чтобы нажатая кнопка обрабатывалась дальше видом, иначе нужно оставить false
                }
                return consumed;
            }
        });
        //вывод даты  в поле информации при нажатии на календаре
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                addRecord = true;
                curDate = String.valueOf(dayOfMonth + "-" + (month + 1) + "-" + year + ": ");
                textMultiline.setText(curDate);
            }
        });
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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (R.id.action_about == id) {
            // Handle about action
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //добавяем запись в файл "event_diary.txt"
    public AlertDialog clickAdd(View view) throws FileNotFoundException {
        String data = String.valueOf(textMultiline.getText());
        if (!addRecord) {
            Toast.makeText(this, "Выберите дату, запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
        } else {
                if  (data.length() >= 14){
                    try (FileOutputStream fos = openFileOutput("event_diary.txt", MODE_APPEND);
                         OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                        //String data = String.valueOf(textMultiline.getText());
                        osw.write(data+"\n");
                        //вывод диалогового окна, что запись внесена
                        CustomDialogFragment dialog2 = new CustomDialogFragment();
                        dialog2.show(getSupportFragmentManager(), "custom");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    addRecord = false;
                } else {
                    Toast.makeText(this, "Запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
                }
        }
        return null;
    }

    //проссмотр по дате
    public void clickReviewData(View view) {
        addRecord = false;
        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        String data = String.valueOf(textMultiline.getText());
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
    //кнопка СБРОС-удаление всего из поля информации и запись текущей даты
    public void clickReset(View view) {
        addRecord = false;
        Calendar ci = Calendar.getInstance();
        textMultiline.setText("");
        editTextInput.setText("");
        String CiDateTime = ci.get(Calendar.DAY_OF_MONTH) + "-" + (ci.get(Calendar.MONTH) + 1) + "-" + ci.get(Calendar.YEAR) + ": ";
        textMultiline.setText(CiDateTime);
    }
    //проссмотр по месяцу
    public void clickReviewMonth(View view) {
        addRecord = false;
        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
        String data = String.valueOf(textMultiline.getText());
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
    //проссмотр содержимого файла "event_diary.txt"
    @SuppressLint("NonConstantResourceId")
    public void clickReview(View view) {
        Intent intent = new Intent(this, ActivityTwo.class);
        startActivity(intent);
    }




    }