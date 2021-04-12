package com.example.agenda.Class;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBController {

    DatabaseReference databaseReference;


    public void deletePreviousAgenda(final String date) {
        String currentUser =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("agenda");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String snap = snapshot.getValue().toString();
                    int index = 1;
                    int count = 0;
                    String analizeDate;
                    while (count < snap.length() - 2) {
                        count = snap.indexOf("}", index);
                        analizeDate = snap.substring(index, index + 10);
                        index = snap.indexOf("}", index) + 3;
                        int monthAnalize = formatDate(analizeDate, 3, 4, 4, 5);
                        int dayAnalize = formatDate(analizeDate, 0, 1, 1, 2);
                        int actualMonth = formatDate(date, 3, 4, 4, 5);
                        int actualDay = formatDate(date, 0, 1, 1, 2);
                        if (monthAnalize < actualMonth || ((dayAnalize < actualDay) && (monthAnalize <= actualMonth))) {
                            databaseReference.child(analizeDate).removeValue();

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public int formatDate(String date, int param1, int param2, int param3, int param4) {
        int firstParam = Integer.parseInt(date.substring(param1,param2));
        int secondParam = Integer.parseInt(date.substring(param3, param4));
        switch (firstParam) {
            case 1 : firstParam = 10; break;
            case 2 : firstParam = 20; break;
            case 3 : firstParam = 30; break;
        }
        int result = firstParam + secondParam;
        return result;
    }

}
