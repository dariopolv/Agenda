package com.example.agenda.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda.Activities.InsertWork;
import com.example.agenda.Activities.ModifyWork;
import com.example.agenda.Helper.MainHelper;
import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TomorrowActivity extends Fragment {
    private TextView tomorrow;
    private Button next;
    private MainHelper mainHelper;
    private String nextDay;
    private ListView workListView, timeListView;
    private Switch modifyMode;
    private TextView modifyModeStatus;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tomorrow_activity, container, false);

        tomorrow = view.findViewById(R.id.tomorrowTextView);
        workListView = view.findViewById(R.id.tomorrowWorkListView);
        timeListView = view.findViewById(R.id.tomorrowTimeListView);

        modifyMode = view.findViewById(R.id.switchToModifyMode);
        modifyModeStatus = view.findViewById(R.id.modifyModeStatus);

        mainHelper = new MainHelper();
        mainHelper.setContext(getContext());
        mainHelper.setWorkListView(workListView);
        mainHelper.setTimeListView(timeListView);

        mainHelper.currentDaySetup();
        nextDay = mainHelper.nextDaySetup();
        mainHelper.setVarDate(nextDay);
        tomorrow.setText(nextDay);



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

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("agenda");

        next =  view.findViewById(R.id.tomorrowNextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext();
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


        databaseReference.child(nextDay).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainHelper.listLoading(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void goToModifyWork(int index) {
        Intent intent = new Intent(getContext(), ModifyWork.class);
        mainHelper.goToModifyWork(index,nextDay, intent);
        startActivity(intent);
    }

    private void goNext() {
        Intent intent = new Intent(getContext(), InsertWork.class);
        startActivity(intent);
    }
}
