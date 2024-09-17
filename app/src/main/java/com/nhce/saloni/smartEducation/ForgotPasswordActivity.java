package com.nhce.saloni.smartEducation;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailId = (EditText) findViewById(R.id.emailId);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void submit(View view) {
        if(!Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString().trim()).matches()){
            emailId.setError("Valid registered mail Id is required.");
            emailId.requestFocus();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(emailId.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        makeText(getApplicationContext(), "Check your email to reset your password!", LENGTH_SHORT).show();
                    else
                        makeText(getApplicationContext(), "Something went wrong! Try again!", LENGTH_SHORT).show();
                }
            });
        }
    }
}