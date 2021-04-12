package com.example.agenda.Helper;

import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class LoginHelper {
    public void login(EditText logemail, EditText logpassword, OnCompleteListener listener) {
        String email, pwd;
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        email = logemail.getText().toString();
        pwd = logpassword.getText().toString();

        if (email.trim().isEmpty())
            logemail.setError("Campo Vuoto");
        else if (pwd.trim().isEmpty())
            logpassword.setError("Campo Vuoto");
        else {
            mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(listener);
        }
    }
}
