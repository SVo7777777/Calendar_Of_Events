package com.example.calendarofevents;

import static android.graphics.Color.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarView extends AppCompatActivity{
    Button button2;
    EditText editText;
    Button previous_month;
    public Button month;
    public Button year;
    LinearLayout view;
    //String[][] buttons = new String[3][3];
    LinearLayout[][] buttons= new LinearLayout[7][8];
    TextView[][] days= new TextView[7][8];
    TextView[][] events= new TextView[7][8];
    TextView[] number_of_week= new TextView[7];

    String[] monthNames = new String[]{"ЯНВАРЬ", "ФЕВРАЛЬ", "МАРТ", "АПРЕЛЬ", "МАЙ", "ИЮНЬ", "ИЮЛЬ", "АВГУСТ", "СЕНТЯБРЬ", "ОКТЯБРЬ", "НОЯБРЬ", "ДЕКАБРЬ"};
    String[] day_of_weeks = new String[]{"","ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА", "ВОСКРЕСЕНЬЕ"};

    public TextView event_11;
    public TextView day_11;
    Calendar calendar = Calendar.getInstance();
    public int current_year = calendar.get(Calendar.YEAR);
    public int current_month = calendar.get(Calendar.MONTH);
    public int current_day = calendar.get(Calendar.DATE);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.view_menu);
        button2 = findViewById(R.id.button2);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        previous_month = findViewById(R.id.previous_month);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        {
            int i = 0;
            while (i < 7) {
                String weekId = "numweek_" + i;
                int weID = getResources().getIdentifier(weekId, "id", getPackageName());
                number_of_week[i] = findViewById(weID);
                i++;
            }
        }

        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 8; j++) {
                //buttons[i][j] = "calendar_"+i+j;
                String llButtonId = "calendar_" + i + j;
                String dayId = "day_" + i + j;
                String eventId = "event_" + i + j;
                int bID = getResources().getIdentifier(llButtonId, "id", getPackageName());
                int dID = getResources().getIdentifier(dayId, "id", getPackageName());
                int eID = getResources().getIdentifier(eventId, "id", getPackageName());
                //buttons[i][j] = String.valueOf(findViewById(gameID));
                buttons[i][j] = findViewById(bID);
                days[i][j] = findViewById(dID);
                events[i][j] = findViewById(eID);

            }

        String month3 = monthNames[current_month];
        System.out.println(month3);
        Calendar c = Calendar.getInstance();
        c.set(current_year, current_month, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int dayOfWeekOfFirstDayOfMonth = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeekOfFirstDayOfMonth);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        //int day_of_week = calendar.getFirstDayOfWeek();
        System.out.println(day_of_week);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        String month2 = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ru"));
        System.out.println(month2);

        calendar.add(Calendar.MONTH, -1);
        int max_pred = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("max_pred="+max_pred);

        showCalendar(month3, current_year, week_of_year, max_pred, day_of_week, dateEnd);

        for (int i = 1; i < 7; i++)
            for (int j = 1; j < 8; j++) {
                try {
                    setOnClick(buttons[i][j], days[i][j], events[i][j], day_of_weeks[j]);
                    //ne: NullPointerException
                } catch (Exception ignored) {

                }

            }

        for (int i = 1; i < 7; i++){
            onWeekMonthClick(number_of_week[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showCalendar(String mon, int yea, int wee, int mpred, int dayOfWeekOfFirstDayOfMonth, int dateEnd) {
        month.setText(mon);
        year.setText(Integer.toString(yea));
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

        calendar.set(current_year, current_month, current_day);
        String sDate_now = sdf1.format(calendar.getTime());
        System.out.println("sDate_now="+sDate_now);
        if (dayOfWeekOfFirstDayOfMonth == 1){
            dayOfWeekOfFirstDayOfMonth = 8;
        }
        int m = mpred - dayOfWeekOfFirstDayOfMonth+3;
        int day_of_week = dayOfWeekOfFirstDayOfMonth;
        int ind = Arrays.asList(monthNames).indexOf(mon);
        if (ind == 0){
            ind = 11;
        }
        int d = 1;
        int d2 = 1;

        for (int i = 1; i < 7; i++) {
            number_of_week[i].setText(Integer.toString(wee));
            wee += 1;
            for (int j = 1; j < 8; j++) {
//                try {
//                    setOnClick(buttons[i][j], days[i][j], events[i][j], (String) year.getText(), (String) month.getText(), day_of_weeks[j]);
//                    //ne: NullPointerException
//                } catch (Exception ignored) {
//
//                }

                if (i == 1 && j < dayOfWeekOfFirstDayOfMonth-1) {
                    buttons[i][j].setBackgroundColor(LTGRAY);
                    days[i][j].setText(Integer.toString(m));
                    buttons[i][j].setEnabled(false);
                    days[i][j].setTextColor(GRAY);
                    events[i][j].setText("");
                    m += 1;
                } else {
                    if (d < dateEnd + 1) {
                        days[i][j].setText(Integer.toString(d));
                        events[i][j].setText("");
                        buttons[i][j].setEnabled(true);
                        @SuppressLint("SimpleDateFormat")
                        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), d);
                        String sDate = sdf.format(c.getTime());
                        boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");

                        if (exists) {
                            try (FileInputStream fis = openFileInput("event_diary.txt");
                                 InputStreamReader isr = new InputStreamReader(fis);
                                 BufferedReader br = new BufferedReader(isr)) {
                                String line;

                                while ((line = br.readLine()) != null) {
                                    boolean contains = line.contains(sDate);
                                    if (contains) {
                                        events[i][j].setText("event");
                                        System.out.println("sDate="+sDate);
                                        break;
                                    }
                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            Toast.makeText(this, "В Вашем календаре пока нет событий! Выберите дату, запишите событие  и внесите!", Toast.LENGTH_LONG).show();

                        }

                        if (j==6 || j == 7){
                            buttons[i][j].setBackgroundColor(buttons[i][j].getContext().getResources().getColor(R.color.weekend_day));
                            days[i][j].setTextColor(days[i][j].getContext().getResources().getColor(R.color.white));
                        }else {
                            buttons[i][j].setBackgroundColor(buttons[i][j].getContext().getResources().getColor(R.color.work_day));
                            days[i][j].setTextColor(days[i][j].getContext().getResources().getColor(R.color.Purple2));

                        }
                        if (sDate.equals(sDate_now)){
                            buttons[i][j].setBackgroundColor(buttons[i][j].getContext().getResources().getColor(R.color.DeepSkyBlue));
                        }
                        d += 1;
                    } else {
                        buttons[i][j].setBackgroundColor(LTGRAY);
                        days[i][j].setText(Integer.toString(d2));
                        days[i][j].setTextColor(GRAY);
                        buttons[i][j].setEnabled(false);
                        events[i][j].setText("");
                        d2 += 1;

                    }

                }

            }
        }

    }
    public void onWeekMonthClick(TextView btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View v) {
                System.out.println(btn.getText());
                String[] week_days;
                String result = "";
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                int week = Integer.parseInt((String) btn.getText());
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.WEEK_OF_YEAR, week);
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                int week_start_day = cal.getFirstDayOfWeek();
                System.out.println(format.format(cal.getTime()));
                for (int i = week_start_day; i < week_start_day + 7; i++) {
                    cal.set(Calendar.DAY_OF_WEEK, i);
                    result += new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()) + " ";
                }
                System.out.println(result);
                week_days = result.split(" ");
                System.out.println(Arrays.toString(week_days));
                Intent intent = new Intent(CalendarView.this, ReviewOWeek.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)week_days);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("week", week);
                //intent.putExtra("week_days", week_days);

                startActivity(intent);

            }
        });
    }
    public void onReviewMonthClick(View view) {
        Intent intent2 = new Intent(CalendarView.this, ReviewOnMonth.class);
        @SuppressLint("SimpleDateFormat")
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), 1);
        String data = sdf.format(c.getTime());
        intent2.putExtra("data", data);
        startActivity(intent2);
    }
    public void onPreviousMonthClick(View view)
    {
        String mon = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(mon);
        if (ind == 0){
            ind = 12;
        }
        String new_month = monthNames[ind-1];
        System.out.println(new_month);
        month.setText(new_month);
        int _year =Integer.parseInt( year.getText().toString());
        System.out.println(_year);
        System.out.println(new_month);
        System.out.println(ind-1);
        Calendar c = Calendar.getInstance();
        c.set(_year, ind-1, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("day_of_week="+day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        showCalendar(new_month, _year, week_of_year, max_pred, day_of_week, dateEnd);
    }
    public void onNextMonthClick(View view)
    {
        String mon = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(mon);
        if (ind == 11){
            ind = -1;
        }
        String new_month = monthNames[ind+1];
        System.out.println(new_month);
        month.setText(new_month);
        int _year =Integer.parseInt( year.getText().toString());
        System.out.println(_year);
        System.out.println(new_month);
        System.out.println(ind+1);
        Calendar c = Calendar.getInstance();
        c.set(_year, ind+1, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        showCalendar(new_month, _year, week_of_year, max_pred, day_of_week, dateEnd);
    }
    public void onPreviousYearClick(View view)
    {
        String old_month = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(old_month);
        int _year =Integer.parseInt( year.getText().toString());
        int new_year = _year-1;
        System.out.println(_year);
        System.out.println(ind-1);
        Calendar c = Calendar.getInstance();
        c.set(new_year, ind, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        showCalendar(old_month, new_year, week_of_year, max_pred, day_of_week, dateEnd);
    }
    public void onNextYearClick(View view)
    {
        String old_month = (String) month.getText();
        int ind = Arrays.asList(monthNames).indexOf(old_month);
        int _year =Integer.parseInt( year.getText().toString());
        int new_year = _year+1;
        System.out.println(_year);
        System.out.println(ind-1);
        Calendar c = Calendar.getInstance();
        c.set(new_year, ind, 1);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        System.out.println(day_of_week);
        int dateEnd = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(dateEnd);
        int  week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        System.out.println(week_of_year);
        c.add(Calendar.MONTH, -1);
        int max_pred = c.getActualMaximum(Calendar.DAY_OF_MONTH);


        showCalendar(old_month, new_year, week_of_year, max_pred, day_of_week, dateEnd);
    }
    private void setOnClick(LinearLayout btn, TextView day1, TextView event1,  String day_week) {
            btn.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalendarView.this);
                    view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_review, null);
                    EditText event = view.findViewById(R.id.editTextTextMultiLine);
                    Button add = view.findViewById(R.id.button);
                    Button close = view.findViewById(R.id.close);
                    TextView number = view.findViewById(R.id.number);
                    number.setText(day1.getText());
                    TextView year1 = view.findViewById(R.id.year);

                    year1.setText(year.getText().toString());
                    TextView month1 = view.findViewById(R.id.month);
                    month1.setText((String) month.getText());
                    TextView day_of_weeks = view.findViewById(R.id.day_of_weeks);
                    day_of_weeks.setText(day_week);

                    @SuppressLint("SimpleDateFormat")
                    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    c.set(Integer.parseInt(year.getText().toString()), Arrays.asList(monthNames).indexOf((String) month.getText()), Integer.parseInt((String) number.getText()));
                    String sDate = sdf.format(c.getTime());
                    event.setText(sDate+": ");
                    boolean exists = FileEmpty.fileExistsInSD("event_diary.txt");
                    if (exists) {
                        StringBuilder sb = new StringBuilder();
                        try (FileInputStream fis = openFileInput("event_diary.txt");
                             InputStreamReader isr = new InputStreamReader(fis);
                             BufferedReader br = new BufferedReader(isr)) {
                            String line;

                            while ((line = br.readLine()) != null) {
                                boolean contains = line.contains(sDate);
                                if (contains) {
                                    String day = line.substring(0, 11);
                                    String event1 = line.substring(11);
                                    String str =  "<font color=\"#0000FF\">"  + day + "</font>" + event1+ " <br>";
                                    sb.append(str);
                                }
                            }
                            event.setText(Html.fromHtml(String.valueOf(sb), Html.FROM_HTML_MODE_LEGACY));
                            if (sb.length() == 0) {
                                event.setHint(sDate + " нет событий за этот день!");
                                //дата синяя
//                                String str = "<font color=\"#0000FF\">" + sDate+": " + "</font>" + " нет событий за этот день!";
//                                event.setHint(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
//
                                //textMultiline.setText(Html.fromHtml("<font color=\"#0000FF\">" + data  + "</font>"+ " нет событий за этот день!"));


                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        event.setHint(R.string.file_exist);

                    }

                    add.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View view) {
                            String data = String.valueOf(event.getText());
                            System.out.println("data="+data);
                            event1.setText("event");
                            //сохранение события в (базу данных) пока в текстовый файл
                            try (FileOutputStream fos = openFileOutput("event_diary.txt", MODE_APPEND);
                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                                //String data = String.valueOf(textMultiline.getText());
                                osw.write(sDate+": "+data+"\n");
                                //вывод диалогового окна, что запись внесена
                                CustomDialogFragment dialog2 = new CustomDialogFragment();
                                dialog2.show(getSupportFragmentManager(), "custom");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();//display the text of button1
                        }
                    });
                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    close.setOnClickListener(new  View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                }



                // Do whatever you want(str can be used here)

            });
        }

    }
