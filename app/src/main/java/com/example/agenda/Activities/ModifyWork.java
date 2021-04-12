package com.example.agenda.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.Helper.ModifyHelper;
import com.example.agenda.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ModifyWork extends AppCompatActivity {
 private EditText workToModify, noteToModify;
 private TextView timeToModify;
 private ModifyHelper modifyHelper;
 private Button applyChanges;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int index = Integer.parseInt(getIntent().getExtras().get("index").toString());

        modifyHelper = new ModifyHelper();
        modifyHelper.setAllLists(getIntent().getStringArrayListExtra("worklist"), getIntent().getStringArrayListExtra("timelist"),
                getIntent().getStringArrayListExtra("idlist"), getIntent().getStringArrayListExtra("notelist"));
        modifyHelper.setDate(getIntent().getStringExtra("date"));

        setContentView(R.layout.modify_work);
        workToModify = findViewById(R.id.workToModify);
        timeToModify = findViewById(R.id.timeToModify);
        noteToModify = findViewById(R.id.noteToModify);

        timeToModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });

        applyChanges = findViewById(R.id.applyChanges);
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyHelper.setModifiedActivity(workToModify.getText().toString(), timeToModify.getText().toString(), noteToModify.getText().toString());
                modifyHelper.applyChanges(index);
                goback();
            }
        });

        workToModify.setText(modifyHelper.getWorkList().get(index));
        timeToModify.setText(modifyHelper.getTimeList().get(index));
        noteToModify.setText(modifyHelper.getNoteList().get(index));



    }

    private void setTime() {
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                ModifyWork.this,
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
                        timeToModify.setText(getTime);

                    }
                }, hours, minutes, true);
        timePickerDialog.show();
    }

    private void goback() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


}
