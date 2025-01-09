package com.example.calendarofevents;

import static android.app.PendingIntent.getActivity;
import static android.icu.text.CaseMap.toUpper;
import static android.view.KeyEvent.KEYCODE_ENTER;

import static java.lang.Character.toUpperCase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    String curDate;
    EditText textMultiline;
    EditText editTextInput;
    private Object MotionEvent;

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
        calendarView = findViewById(R.id.calendarView10);
        textMultiline = findViewById(R.id.editTextTextMultiLine8);
        Calendar ci = Calendar.getInstance();
        //вывод даты в поле информации при запуске приложения
        String CiDateTime = ci.get(Calendar.DAY_OF_MONTH)+ "-" + (ci.get(Calendar.MONTH) + 1) + "-" + ci.get(Calendar.YEAR)+": ";
        textMultiline.setText(CiDateTime);
        editTextInput = findViewById(R.id.editTextInput);
        //поиск по слову  по нажатию на ENTER или OK
        editTextInput.setOnKeyListener(new View.OnKeyListener() {
            @SuppressLint("SetTextI18n")
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean consumed = false;
                if (keyCode == KEYCODE_ENTER) {
                    Editable word = editTextInput.getText();//Делаем то, что нам нужно...
                    StringBuilder sb = new StringBuilder();
                    try (FileInputStream fis = openFileInput("event_diary.txt");
                         InputStreamReader isr = new InputStreamReader(fis);
                         BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null){
                            boolean contains = line.contains(word);
                            if (contains){
                                sb.append(line+"\n");
                            }
                        }
                        textMultiline.setText(sb.toString());
                        if (sb.length()==0){
                            textMultiline.setText("по слову: '"+word+"' ничего не найдено. Поппробуйте ввести первые несколько букв слова.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
                curDate = String.valueOf(dayOfMonth + "-" + (month + 1) + "-" + year + ": ");
                textMultiline.setText(curDate);
            }
        });
    }
    //добавяем запись в файл "event_diary.txt"
    public AlertDialog clickAdd(View view) throws FileNotFoundException {

        try (FileOutputStream fos = openFileOutput("event_diary.txt",  MODE_APPEND);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.write("\n"+String.valueOf(textMultiline.getText()));
            //вывод диалогового окна, что запись внесена
            CustomDialogFragment dialog = new CustomDialogFragment();
            dialog.show(getSupportFragmentManager(), "custom");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    //проссмотр содержимого файла "event_diary.txt"
    public void clickReview(View view) {
        //считываем с файла всё что есть
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = openFileInput("event_diary.txt");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append('\n');
            String infile = sb.toString();
            textMultiline.setText(infile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //проссмотр по дате
    public void clickReviewData(View view) {
        String data = String.valueOf(textMultiline.getText());
        System.out.println(data.length());
        if ((data.length()>=10)&&(data.length()<=12)){
            //считываем с файла всё что есть
            StringBuilder sb = new StringBuilder();
            try (FileInputStream fis = openFileInput("event_diary.txt");
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null){
                    boolean contains = line.contains(data);
                    if (contains){
                        sb.append(line);
                        String infile = sb.toString();
                        textMultiline.setText(infile);break;
                    }else{
                        textMultiline.setText(data+"нет записей в этот день");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("кнопка не работает");
        }
    }
    //кнопка СБРОС-удаление всего из поля информации и запись текущей даты
    public void clickReset(View view) {
        Calendar ci = Calendar.getInstance();
        textMultiline.setText("");
        editTextInput.setText("");
        String CiDateTime = ci.get(Calendar.DAY_OF_MONTH)+ "-" + (ci.get(Calendar.MONTH) + 1) + "-" + ci.get(Calendar.YEAR)+": ";
        textMultiline.setText(CiDateTime);
    }
    //проссмотр по месяцу
    public void clickReviewMonth(View view) {
        String data = String.valueOf(textMultiline.getText());
        System.out.println(data.length());
        if ((data.length()>=10)&&(data.length()<=12)){
            int index_first = data.indexOf("-");
            int index_second = data.indexOf("-", index_first + 1);
            String month = data.substring(index_first,index_second+5);
            //считываем с файла всё что есть
            StringBuilder sb = new StringBuilder();
            try (FileInputStream fis = openFileInput("event_diary.txt");
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {
                String line;
                while ((line = br.readLine()) != null){
                    System.out.println(data +"мы тут");
                    System.out.println(line);
                    System.out.println(data);
                    boolean contains = line.contains(month);
                    if (contains){
                        System.out.println(data +"в файле есть");
                        sb.append(line+"\n");
                    }
                    String infile = sb.toString();
                    textMultiline.setText(infile);
                }
                if (sb.length()==0){
                    textMultiline.setText(data+"нет записей в этом месяце ");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("кнопка не работает");
        }
    }
}

