package com.example.agenda.Helper;

import android.os.AsyncTask;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.agenda.Class.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegistrationHelper extends AsyncTask<ArrayList<String>,Void,Void> {
    private final Pattern patternEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    public boolean registration(EditText name, EditText surname, final EditText email, EditText password) {
        final String regname, regsurname, regemail, regpwd, agenda;
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        agenda = "";
        regname = name.getText().toString();
        regsurname = surname.getText().toString();
        regemail = email.getText().toString();
        regpwd = password.getText().toString();
        if (checkfield(name, surname, email, password)) {
            final User user = new User(regname, regsurname, regemail,agenda);

            mAuth.createUserWithEmailAndPassword(regemail, regpwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final DatabaseReference mDBRef = FirebaseDatabase.getInstance().getReference();
                                mDBRef.child("Users").child(mAuth.getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> taskb) {
                                        if (!taskb.isSuccessful()) {
                                            mAuth.getCurrentUser().delete();
                                        }
                                    }
                                });

                            } else {
                                email.setError("Email in uso");
                            }
                        }
                    });

            return true;
        }
        else return false;

    }

    public boolean checkfield(EditText name, EditText surname, EditText email, EditText password) {
        boolean registrable = true;

        if (name.getText().toString().trim().equals("")) {
            name.setError("Campo Vuoto");
            registrable = false;
        }

        if (surname.getText().toString().trim().equals("")) {
            surname.setError("Campo Vuoto");
            registrable = false;
        }

        if (password.getText().toString().trim().equals("")) {
            password.setError("Campo Vuoto");
            registrable = false;
        }

        if (!patternEmail.matcher(email.getText().toString()).matches()) {
            email.setError("Email non valida");
            registrable = false;
        }


        return registrable;
    }


    public boolean checkList(ArrayList<String> arrayList, String email) {
        boolean check = false;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(email)) {
                check = false;
                break;
            } else
                check = true;
        }
        return check;
    }

    @Override
    protected  Void doInBackground(final ArrayList<String>...arrayLists) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String snap = snapshot.getValue().toString();
                    String dbemail;
                    int index = snap.indexOf("email") + 6;
                    int count = 0;
                    while (count != snap.length() - 2) {
                        count = snap.indexOf('}', index);
                        dbemail = snap.substring(index, count);
                        arrayLists[0].add(dbemail);
                        index = snap.indexOf("email", index) + 6;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }
}
