package com.example.agenda.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.Helper.RegistrationHelper;
import com.example.agenda.R;

import java.util.ArrayList;

public class Register extends AppCompatActivity {
    private Button registrationButton;
    private EditText regname, regsurname, regpwd, regemail;
    public RegistrationHelper registerHelper;
    public ArrayList<String> arrayList;
    public ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);


        registrationButton = findViewById(R.id.registrationButton);
        regname = findViewById(R.id.regName);
        regsurname = findViewById(R.id.regSurname);
        regemail = findViewById(R.id.regEmail);
        regpwd = findViewById(R.id.regPassword);
        registerHelper = new RegistrationHelper();
        arrayList = new ArrayList<>();

    }

    protected void onStart()  {
        super.onStart();
        new RegistrationHelper().execute(arrayList);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result =  registerHelper.registration(regname,regsurname,regemail,regpwd);
                if(registerHelper.checkList(arrayList, regemail.getText().toString()) && result) {
                    arrayList.add(regemail.getText().toString());
                    goback();
                    Toast.makeText(Register.this, "Registrazione riuscita", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(Register.this, "Registrazione non riuscita" , Toast.LENGTH_LONG).show();

            }
        });
    }

    private void goback() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
