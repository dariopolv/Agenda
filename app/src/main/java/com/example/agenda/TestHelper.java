package com.example.agenda;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestHelper {
    public  DatabaseReference databaseReference;
    private CustomData customData;


    public void add(Work work, String date) {
        databaseReference = FirebaseDatabase.getInstance().getReference(date);
        databaseReference.push().setValue(work.getWork() + " " + work.getTime());
    }
}
