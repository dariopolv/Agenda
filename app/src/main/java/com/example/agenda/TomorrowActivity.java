package com.example.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        mainHelper = new MainHelper();
        mainHelper.setContext(getContext());
        mainHelper.setWorkListView(workListView);
        mainHelper.setTimeListView(timeListView);

        mainHelper.currentDaySetup();
        nextDay = mainHelper.nextDaySetup();
        tomorrow.setText(nextDay);

        databaseReference = FirebaseDatabase.getInstance().getReference();

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
               mainHelper.clickToRemoveJob(i);
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

    private void goNext() {
        Intent intent = new Intent(getContext(), Test.class);
        startActivity(intent);
    }
}
