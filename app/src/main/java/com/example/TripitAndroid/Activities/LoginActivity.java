package com.example.TripitAndroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    protected boolean enabled = true;
    TextView emailText;
    TextView passwordText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.register_email);
        passwordText = findViewById(R.id.register_password);
        progressBar = findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        FirebaseApp.initializeApp(this);
        FirebaseUser user = Model.instance.currentUser();
        if (user != null) {
            gotoMainView();
        }
    }


    public void OnLoginTapped(View view) {
        progressBar.setVisibility(View.VISIBLE);
        enabled = false;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.equals("") || password.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            enabled = true;
            Toast.makeText(LoginActivity.this, "Email and Password cannot be empty", Toast.LENGTH_LONG).show();
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Model.instance.login(email, password, new FirebaseModel.OnLoginCompleteListener() {
                @Override
                public void onLoginComplete(FirebaseUser user) {
                    if (user != null) {
                        gotoMainView();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        enabled = true;
                        Toast.makeText(LoginActivity.this, "Failed while trying to Login. Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            enabled = true;
            Toast.makeText(LoginActivity.this, "Email address isn't valid", Toast.LENGTH_LONG).show();
        }
    }

    public void OnRegisterTapped(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void gotoMainView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return enabled ?
                super.dispatchTouchEvent(ev) :
                true;
    }
}
