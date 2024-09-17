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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.model.User;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailId;
    EditText password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailId = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void forgotPassword(View view) {
        makeText(getApplicationContext(), "Forgot password!!", LENGTH_SHORT).show();
        //Redirect Activity to Teams
        Utility.redirectActivity(this,  ForgotPasswordActivity.class);
    }
    public void goToRegister(View view) {
        makeText(getApplicationContext(), "Register Button Clicked", LENGTH_SHORT).show();
        //Redirect Activity to Teams
        Utility.redirectActivity(this,  RegisterActivity.class);
    }
    boolean checkEmptyFields() {
        if(!Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString().trim()).matches()){
            emailId.setError("Valid mail Id is required.");
            emailId.requestFocus();
            return false;
        }
        else if(password.getText().toString().trim().length() < 6){
            password.setError("Minimum Password length Should be 6 characters.");
            password.requestFocus();
            return false;
        }
        else{
            return true;
        }
    }
    public void login(View view){
        if(checkEmptyFields()){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailId.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        /*//for email Versification
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()){
                         */
                        setListActivityObserver();
                        makeText(getApplicationContext(), "User has been login successfully!", LENGTH_SHORT).show();
                        //Redirect Activity to Event
                        Utility.redirectActivity(LoginActivity.this, DateActivity.class);
                        progressBar.setVisibility(View.GONE);
                            /*
                        }
                        else{
                            user.sendEmailVerification();
                            makeText(getApplicationContext(), "Check your email to verify your account!", LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        */
                    }
                    else{
                        makeText(getApplicationContext(), "Failed to login! Please check your credentials!", LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
        else{
            makeText(getApplicationContext(), "Failed to login! Please check your credentials!", LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
    }

    private void setListActivityObserver() {
        //                                                      root/studentList/101/sci001/notes
        RealTimeDatabaseFirebase.getInstance("User", FirebaseAuth.getInstance().getCurrentUser().getUid(), User.class).datarowObservableSubject.subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(User customObj) {
                RealTimeData.getInstance().setUserId(customObj.getUsn());
                RealTimeData.getInstance().setUserName(customObj.getName());
                if(customObj.getUserType().equals("Teacher"))
                    RealTimeData.getInstance().setUserListType("teacherList");
                else
                    RealTimeData.getInstance().setUserListType("studentList");
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });
    }
}