package com.example.agenda;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListHelper {

    DatabaseReference databaseReference;


    public void clickToRemoveJob(ArrayList<String> workList, ArrayList<String> timeList, ArrayList<String> idList, ArrayAdapter workAdapter, ArrayAdapter timeAdapter, String date, int index) {
        workList.remove(index);
        timeList.remove(index);
        workAdapter.notifyDataSetChanged();
        timeAdapter.notifyDataSetChanged();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(date).child(idList.get(index)).removeValue();
    }



}
