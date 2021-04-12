package com.example.agenda.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda.Class.CustomData;
import com.example.agenda.Helper.MainHelper;
import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class AllActivities extends Fragment {

    private final Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePick;
    private TextView date;
    private Switch modifyMode;
    private CustomData customData;
    private MainHelper mainHelper;
    private ListView workListView, timeListView;
    private TextView modifyModeStatus;
    DatabaseReference databaseReference;
    String currentDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_all_activities, container, false);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("agenda");

        date = view.findViewById(R.id.allActivitiesDay);
        workListView = view.findViewById(R.id.allActivitiesWorkListView);
        timeListView = view.findViewById(R.id.allActivitiesTimeListView);
        modifyMode = view.findViewById(R.id.switchToModifyMode);
        modifyModeStatus = view.findViewById(R.id.modifyModeStatus);
        modifyModeStatus.setText("OFF");


        mainHelper = new MainHelper();
        mainHelper.setContext(getContext());
        mainHelper.setWorkListView(workListView);
        mainHelper.setTimeListView(timeListView);

       modifyMode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (modifyMode.isChecked()) {
                   mainHelper.setModifyMode();
                   modifyModeStatus.setText("ON");
               } else {
                   mainHelper.exitModifyMode();
                   modifyModeStatus.setText("OFF");
               }
           }
       });

       date.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               setDate();
               datePick.show();
           }
       });


        workListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mainHelper.getSetModifyMode() == false) {
                    new AlertDialog.Builder(getContext()).setTitle("Note").setMessage(mainHelper.getNoteList().get(i)).show();
                }
                else {
                     goToModifyWork(i);
                }
            }
        });

        workListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                new AlertDialog.Builder(getContext()).setMessage("Eliminare definitivamente?").setNegativeButton("No", null)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainHelper.clickToRemoveJob(position);
                            }
                        }).show();
                return true;
            }
        });

        return view;
    }

    private void goToModifyWork(int index) {
        Intent intent = new Intent(getContext(), ModifyWork.class);
        mainHelper.goToModifyWork(index,currentDay, intent);
        startActivity(intent);
    }


    private void setDate(){

        datePick = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                customData = new CustomData(dayOfMonth, month, year);
                currentDay = formatDate(dayOfMonth, month, year);
                mainHelper.setVarDate(currentDay);
                date.setText(currentDay);
                mainHelper.refreshList();
                if (currentDay != null) {
                    databaseReference.child(currentDay).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mainHelper.listLoading(snapshot);
                            mainHelper.listLoadingRefresh(snapshot);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

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
