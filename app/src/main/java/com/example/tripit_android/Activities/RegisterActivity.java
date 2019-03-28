package com.example.tripit_android.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tripit_android.R;
import com.example.tripit_android.models.FirebaseModel;
import com.example.tripit_android.models.Model;

public class RegisterActivity extends AppCompatActivity {
    protected boolean enabled = true;
    TextView emailText;
    TextView passwordText;
    TextView rePasswordText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = findViewById(R.id.register_email);
        passwordText = findViewById(R.id.register_password);
        rePasswordText = findViewById(R.id.register_repassword);
        progressBar = findViewById(R.id.register_progressbar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void OnRegisterTapped(View view) {
        progressBar.setVisibility(View.VISIBLE);
        enabled = false;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String rePassword = rePasswordText.getText().toString();

        if (email.equals("") || password.equals("") || !password.equals(rePassword)) {
            if (!password.equals(rePassword)) {
                Toast.makeText(RegisterActivity.this, "Please enter same passwords", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Email and Password cannot be empty", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
            enabled = true;
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Model.instance.signUp(email, password, new FirebaseModel.OnSignUpCompleteListener() {
                @Override
                public void onSignUpComplete(Boolean result) {
                    if (result) {
                        gotoMainView();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        enabled = true;
                        Toast.makeText(RegisterActivity.this, "Failed while trying to register. Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            enabled = true;
            Toast.makeText(RegisterActivity.this, "Email address isn't valid", Toast.LENGTH_LONG).show();
        }
    }

    public void OnLoginTapped(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
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
