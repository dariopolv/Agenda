package com.example.agenda.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.agenda.Helper.LoginHelper;
import com.example.agenda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    private Button login;
    private Button register;
    private Button biometricsButton;
    private Button loginWithMailAndPassword;
    private EditText email, password;
    private TextView messageTv;
    private CheckBox saveLogin;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLoginControl;
    public LoginHelper loginHelper;
    protected boolean checklogin;
    protected OnCompleteListener onCompleteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        executor = ContextCompat.getMainExecutor(getApplicationContext());
        biometricPrompt = new BiometricPrompt(Login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Login Failure", Toast.LENGTH_LONG).show();
                //error
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    gotomenu();
                    Toast.makeText(getApplicationContext(), "Autenticazione effettuata" , Toast.LENGTH_LONG ).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Autenticazione fallita: \nEffettua il primo accesso prima di utilizzare l'autenticazione biometrica ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Login Failure", Toast.LENGTH_LONG).show();
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticazione Biometrica")
                .setSubtitle("Scan your finger")
                .setNegativeButtonText("Indietro")
                .build();


        login = findViewById(R.id.login);
        login.setVisibility(View.GONE);
        messageTv = findViewById(R.id.messageTextView);
        loginWithMailAndPassword = findViewById(R.id.loginWithMailAndPassword);
        register = findViewById(R.id.register);
        email = findViewById(R.id.logemail);
        email.setVisibility(View.GONE);
        password = findViewById(R.id.logpassword);
        password.setVisibility(View.GONE);
        saveLogin = findViewById(R.id.checkBox);
        saveLogin.setVisibility(View.GONE);
        biometricsButton = findViewById(R.id.loginFingerPrint);
        loginHelper = new LoginHelper();

        loginWithMailAndPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                saveLogin.setVisibility(View.VISIBLE);
                biometricsButton.setVisibility(View.GONE);
                loginWithMailAndPassword.setVisibility(View.GONE);
                messageTv.setVisibility(View.GONE);

            }
        });

        biometricsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });


        onCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                    checklogin = true;
                else
                    checklogin = false;
                authentication();
            }
        };

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLoginControl = loginPreferences.getBoolean("saveLoginControl", false);
        if (saveLoginControl == true) {
            email.setText(loginPreferences.getString("email", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLogin.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginHelper.login(email, password, onCompleteListener);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                if (saveLogin.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLoginControl", true);
                    loginPrefsEditor.putString("email", email.getText().toString());
                    loginPrefsEditor.putString("password", password.getText().toString());
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoregistration();
            }
        });
    }

    private void authentication() {
        if (checklogin) {
            Toast.makeText(Login.this, "Accesso Effettuato", Toast.LENGTH_LONG).show();
            gotomenu();
        } else
            Toast.makeText(Login.this, "Nome Utente e/o Password Errati", Toast.LENGTH_LONG).show();
    }

    private void gotomenu() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void gotoregistration() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
