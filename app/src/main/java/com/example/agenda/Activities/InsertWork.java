package com.example.agenda.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.Class.CustomData;
import com.example.agenda.Helper.InsertWorkHelper;
import com.example.agenda.R;
import com.example.agenda.Class.Work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class InsertWork extends AppCompatActivity {
    private final Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePick;
    private TextView date;
    private EditText work;
    private TextView time;
    private EditText note;
    private CustomData oCustomData;
    private Work myWork;
    private Button add;
    InsertWorkHelper insertWorkHelper = new InsertWorkHelper();


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_work);
        work = (EditText) findViewById(R.id.editTextWork);
        time = findViewById(R.id.editTextTime);
        date = findViewById(R.id.editTextDate);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setDate();
            datePick.show();
            }
        });
        note = findViewById(R.id.editTextNote);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });


        add = (Button) findViewById(R.id.addButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWork = new Work(work.getText().toString(), time.getText().toString());
                myWork.setWork(work.getText().toString());
                myWork.setTime(time.getText().toString());
                insertWorkHelper.add(myWork, date.getText().toString(),note.getText().toString());
                goback();
            }
        });

    }

    private void goback() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void setDate(){

        datePick = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                oCustomData = new CustomData(dayOfMonth, month, year);
                date.setText(formatDate(dayOfMonth, month, year));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setTime() {
       Calendar cal = Calendar.getInstance();
       int hours = cal.get(Calendar.HOUR_OF_DAY);
       int minutes = cal.get(Calendar.MINUTE);

       TimePickerDialog timePickerDialog = new TimePickerDialog(
               InsertWork.this,
               R.style.Theme_AppCompat_Dialog,
               new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker timePicker, int i, int i1) {
                       Calendar c = Calendar.getInstance();
                       c.set(Calendar.HOUR_OF_DAY, i);
                       c.set(Calendar.MINUTE, i1);
                       c.setTimeZone(TimeZone.getDefault());
                       SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                       String getTime = format.format(c.getTime());
                       time.setText(getTime);

                   }
               }, hours, minutes, true);
       timePickerDialog.show();
    }


    private String formatDate(int day, int month, int year){
        String dayString = "" + day;
        String monthString = "" + (month+1);
        String stringDate;
        if(day < 10) {
         dayString = "0" + day;
        }
        if (month+1 < 10) {
            monthString = "0" + (month+1);
        }
        stringDate = dayString + "-" + monthString + "-" + year;
        return stringDate;
    }
}
