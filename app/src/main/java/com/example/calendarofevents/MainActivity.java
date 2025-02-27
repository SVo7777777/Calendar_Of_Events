package com.example.calendarofevents;

import static android.app.PendingIntent.getActivity;
import static android.graphics.Color.BLACK;
import static android.view.KeyEvent.KEYCODE_ENTER;

import static org.xmlpull.v1.XmlPullParser.TEXT;
import static java.lang.Character.toUpperCase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;


public class MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    String curDate;
    public EditText textMultiline;
    EditText editTextInput;
    SearchView simpleSearchView;
    private Object MotionEvent;
    boolean addRecord;
    public String chosesData;
    String[] data = {"ПРОССМОТРЕТЬ", "ПРОССМОТРЕТЬ ЗА ДЕНЬ", "ПРОССМОТРЕТЬ ЗА МЕСЯЦ", "ПРОССМОТРЕТЬ ЗА НЕДЕЛЮ"};


    //boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
    @SuppressLint("SetTextI18n")
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
        //верхняя полоса с названием и 3мя точками
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Use the toolbar as the app bar
        //цвет для 3ёх точек и для названия
        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
        //виджет поиска и его цвет
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        SearchView simpleSearchView = findViewById(R.id.simpleSearchView);
        //ImageView icon = simpleSearchView.findViewById(android.support.v4.app.R.id.search_button);
        //icon.setColorFilter(Color.BLACK);
        //ImageView searchIcon = simpleSearchView.findViewById(R.id.search_button);
        //simpleSearchView.setIconified(Color.BLACK);
        //SearchView searchView = findViewById(R.id.search_view);
//        ImageView searchIcon = simpleSearchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
//        searchIcon.setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_IN);



        //simpleSearchView.setBackgroundColor(Color.YELLOW);




        //меню для кнопки "проссмотреть"
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
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //изменяем щрифт и цвет кнопки спиннера
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
//                ((TextView) parent.getChildAt(0)).setTextSize(20);


                String data = String.valueOf(textMultiline.getText());
                boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
                if (exists) {
                    if ((data.length() >= 10) && (data.length() <= 12) ) {
                        // показываем позиция нажатого элемента
                        //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                        switch (position) {
                            case 0:
                                // Whatever you want to happen when the first item gets selected
                                break;
                            case 1:

                                System.out.println(data.length());
                                int index_first = data.indexOf("-");
                                //11-1-2025
                                //поиск даты по виду 11-01-2025
                                StringBuilder data2 = new StringBuilder(data);
                                data2.insert(index_first + 1, '0');
                                System.out.println(data2);
                                clickReviewData(data, String.valueOf(data2));
                                spinner.setSelection(0);

                                break;
                            case 2:
                                Intent intent2 = new Intent(MainActivity.this, ReviewOnMonth.class);
                                intent2.putExtra("data", data);
                                spinner.setSelection(0);
                                startActivity(intent2);
                                break;
                            case 3:

                                spinner.setSelection(0);
                                //String[] week_days = new String[7];
                                String[] week_days;

                                System.out.println("chosesData=" + chosesData);
//                                выбранную дату в новый формат переводим
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = null;
                                try {
                                    date = format.parse(chosesData);
                                    System.out.println(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String result = "";
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                                int week_start_day = calendar.getFirstDayOfWeek();
                                int index = 0;
                                for (int i = week_start_day; i < week_start_day + 7; i++) {
                                    calendar.set(Calendar.DAY_OF_WEEK, i);
                                    result += new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()) + " ";
                                    }

                                System.out.println("chosesData=" + chosesData+" "+week+" "+week_start_day);
                                System.out.println(result);
                                week_days = result.split(" ");
                                System.out.println(Arrays.toString(week_days));
                                Intent intent = new Intent(MainActivity.this, ReviewOWeek.class);
                                Bundle args = new Bundle();
                                args.putSerializable("ARRAYLIST",(Serializable)week_days);
                                intent.putExtra("BUNDLE",args);
                                intent.putExtra("week", week);
                                //intent.putExtra("week_days", week_days);

                                startActivity(intent);
                                break;
                        }
                    } else {
                        if (position!=0){
                        Toast.makeText(getBaseContext(), "Выберите дату на календаре!", Toast.LENGTH_LONG).show();
                        System.out.println("кнопка не работает");
                        spinner.setSelection(0);
                        }else{
                            System.out.println("кнопка не работает");
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();
                    spinner.setSelection(0);
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
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy EEEE");
        System.out.println(format.format(ci.getTime()));
        String today = format.format(ci.getTime());
        // цвет даты
        //textMultiline.setText(Html.fromHtml("<font color=\"#006400\">" + today  + "</font>"));
        CharSequence hint = textMultiline.getHint();
        String s = "Сегодня " + today + ". " + hint;
        textMultiline.setHint(s);

        //курсор в конце строки
        textMultiline.requestFocus();
        textMultiline.setSelection(textMultiline.getText().length());
//        Toast toast = Toast.makeText(getBaseContext(), " Выберите дату и запишите событие.", Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
//        toast.show();

        // поиск по слову Set SearchView query text listener
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StringBuilder sb = new StringBuilder();
                boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
                if (exists) {
                    try (FileInputStream fis = openFileInput("event_diary.txt");
                         InputStreamReader isr = new InputStreamReader(fis);
                         BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            boolean contains = line.contains(query);
                            if (contains) {
                                sb.append(line + "\n");
                            }
                        }
                        textMultiline.setText(sb.toString());
                        if (sb.length() == 0) {
                            textMultiline.setText("По слову '" + query + "' ничего не найдено. Попробуйте ввести первые несколько букв слова.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    textMultiline.setText("В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!");
                }
//                if (myList.contains(query)) {
//                    adapter.getFilter().filter(query);
//                }
//                else {
//                    Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //public void setGravity (int gravity, int xOffset, int yOffset);
        addRecord = true;
        //editTextInput = findViewById(R.id.editTextInput);
        //поиск по слову  по нажатию на ENTER или OK
//        editTextInput.setOnKeyListener(new View.OnKeyListener() {
//            @SuppressLint("SetTextI18n")
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                boolean consumed = false;
//                boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
//                if (keyCode == KEYCODE_ENTER) {
//                    Editable word = editTextInput.getText();//Делаем то, что нам нужно...
////                    String word = String.valueOf(editTextInput.getText());
////                    String[] words = word.split(" ");
////                    System.out.println(Arrays.toString(words));
//
//                    StringBuilder sb = new StringBuilder();
//                    if (exists) {
//                        try (FileInputStream fis = openFileInput("event_diary.txt");
//                             InputStreamReader isr = new InputStreamReader(fis);
//                             BufferedReader br = new BufferedReader(isr)) {
//                            String line;
//                            while ((line = br.readLine()) != null) {
//                                boolean contains = line.contains(word);
//                                if (contains) {
//                                    sb.append(line + "\n");
//                                }
//                            }
//                            textMultiline.setText(sb.toString());
//                            if (sb.length() == 0) {
//                                textMultiline.setText("По слову '" + word + "' ничего не найдено. Попробуйте ввести первые несколько букв слова.");
//                            }
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }else {
//                        textMultiline.setText("В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!");
//                    }
//                    consumed = true; //это если не хотим, чтобы нажатая кнопка обрабатывалась дальше видом, иначе нужно оставить false
//                }
//                return consumed;
//            }
//        });
        //вывод даты  в поле информации при нажатии на календаре
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                addRecord = true;
                @SuppressLint("SimpleDateFormat")
                final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int week = calendar.get(Calendar.WEEK_OF_YEAR);

                String sDate = sdf.format(calendar.getTime());
                chosesData = sDate;
                //цвет даты 006400-зелёный
                textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + sDate + ": " +"</font>"));
                //фокус в конце даты
                textMultiline.requestFocus();
                textMultiline.setSelection(textMultiline.getText().length());
            }
        });
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
        if (R.id.action_save == id) {
            // Handle settings action
            //Intent intent = new Intent(MainActivity.this, SaveActivity.class);
            //startActivity(intent);
            //вывод диалогового окна, что запись внесена
            String data = String.valueOf(textMultiline.getText());
            CustomDialogFragment dialog2 = new CustomDialogFragmentSave();
            Bundle args = new Bundle();
            args.putString("data", data);
            dialog2.setArguments(args);
            dialog2.show(getSupportFragmentManager(), "custom");
            return true;
        }
        if (R.id.action_settings == id) {
            // Handle settings action
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (R.id.action_about == id) {
            // Handle about action
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        if (R.id.action_view == id) {
            // Handle settings action
            Intent intent = new Intent(MainActivity.this, com.example.calendarofevents.CalendarView.class);
            startActivity(intent);
            return true;
        }
            return super.onOptionsItemSelected(item);

    }
    //Например, MODE_PRIVATE — файл доступен только этому приложению, MODE_WORLD_READABLE — файл доступен для чтения всем, MODE_WORLD_WRITEABLE — файл доступен для записи всем, MODE_APPEND — файл будет дописан, а не начат заново.
    //добавяем запись в файл "event_diary.txt"
    public AlertDialog clickAdd(View view) throws FileNotFoundException {
        String data = String.valueOf(textMultiline.getText());
        System.out.println(data);
        if (!addRecord) {
            Toast.makeText(this, "Выберите дату, запишите событие, а потом внесите! ", Toast.LENGTH_LONG).show();
        } else {
                if  (data.length() >= 20){
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
    @SuppressLint("SetTextI18n")
    public void clickReviewData(String data, String data2) {
        addRecord = false;


        //считываем с файла всё что есть
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = openFileInput("event_diary.txt");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;

            while ((line = br.readLine()) != null) {
                boolean contains = line.contains(data);
                boolean contains2 = line.contains(data2);
                if ((contains) || (contains2)) {
                    String day = line.substring(0, 11);
                    String event = line.substring(11);
                    String str =  "<font color=\"#0000FF\">"  + day + "</font>" + event+ " <br>";
                    sb.append(str);
                }



            }
            textMultiline.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
            if (sb.length() == 0) {
                //textMultiline.setText(data + " нет событий за этот день!");
                //дата синяя
                String str = "<font color=\"#0000FF\">" + data + "</font>" + " нет событий за этот день!";
                textMultiline.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));

                //textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + data  + "</font>"+ " нет событий за этот день!"));


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //кнопка СБРОС-удаление всего из поля информации и запись текущей даты

    public void clickReset(View view) {
        addRecord = false;
        Calendar ci = Calendar.getInstance();
        textMultiline.setText("");
        //simpleSearchView.setQuery("", false);
        //searchView.setQuery("", false);
        //simpleSearchView.setIconified(true);
        //simpleSearchView.setQueryHint("Поиск по слову. Введите слово.");

//        String CiDateTime = ci.get(Calendar.DAY_OF_MONTH) + "-" + (ci.get(Calendar.MONTH) + 1) + "-" + ci.get(Calendar.YEAR) + ": ";
//        textMultiline.setText(CiDateTime);
        //курсор в конце строки
        textMultiline.requestFocus();
        textMultiline.setSelection(textMultiline.getText().length());
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
                String month = data.substring(index_first, index_second + 5);//-1-2025
                //поиск месяца по виду -01-2025
                StringBuilder month2 = new StringBuilder(month);
                month2.insert(1, '0');
                System.out.println(month2);
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

    }



