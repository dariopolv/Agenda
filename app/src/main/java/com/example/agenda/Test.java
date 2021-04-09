package com.example.agenda;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Test extends AppCompatActivity {
    private final Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePick;
    private EditText date;
    private EditText work;
    private TextView time;
    private CustomData oCustomData;
    private Work myWork;
    private Button add;
    TestHelper testHelper = new TestHelper();



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_work);
        work = (EditText) findViewById(R.id.editTextWork);
        time = findViewById(R.id.editTextTime);
        date = (EditText) findViewById(R.id.editTextDate);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setDate();
            datePick.show();
            }
        });




        add = (Button) findViewById(R.id.addButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWork = new Work(work.getText().toString(), time.getText().toString());
                myWork.setWork(work.getText().toString());
                myWork.setTime(time.getText().toString());
                testHelper.add(myWork, date.getText().toString());
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
