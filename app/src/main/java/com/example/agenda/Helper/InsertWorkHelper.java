package com.example.agenda.Helper;

import com.example.agenda.Class.CustomData;
import com.example.agenda.Class.Work;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class InsertWorkHelper {
    public  DatabaseReference databaseReference;
    public FirebaseAuth firebaseAuth;
    private CustomData customData;


    public void add(Work work, String date, String note) {
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("agenda").child(date);
        databaseReference.push().setValue(work.getWork() + " " + work.getTime() + "/" + note);

    }
}
