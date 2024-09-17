package com.nhce.saloni.smartEducation;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nhce.saloni.smartEducation.data.model.User;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText mailId;
    EditText userName;
    EditText usn;
    EditText password;
    RadioGroup accountType;
    RadioButton accountTypeSelected;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mailId = (EditText) findViewById(R.id.emailId);
        userName = (EditText) findViewById(R.id.userName);
        usn = (EditText) findViewById(R.id.usn);
        password = (EditText) findViewById(R.id.password);
        accountType=(RadioGroup) findViewById(R.id.role);
    }

    public void GoToLogin(View view){
        makeText(getApplicationContext(), "Enter Login Credentials ", LENGTH_SHORT).show();
        //Redirect Activity to Teams
        Utility.redirectActivity(this,LoginActivity.class);
    }

    public void register(View view) {
        int selectedId = accountType.getCheckedRadioButtonId();
        accountTypeSelected = (RadioButton) findViewById(selectedId);
        progressBar.setVisibility(View.VISIBLE);
        if(checkEmptyFields()) {
            mAuth.createUserWithEmailAndPassword(mailId.getText().toString().trim(),password.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User( mailId.getText().toString().trim(), userName.getText().toString().trim(), usn.getText().toString().trim(), password.getText().toString().trim(), accountTypeSelected.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        makeText(getApplicationContext(), "User has been registered successfully!", LENGTH_SHORT).show();
                                        if(accountTypeSelected.getText().toString().trim().equals("Teacher")){
                                            RealTimeDatabaseFirebase new_register_node = RealTimeDatabaseFirebase.getInstance("root/teacherList/" + usn.getText().toString().trim(), Object.class);
                                            new_register_node.insertDataRow("name", userName.getText().toString().trim());
                                        }
                                        else {
                                            RealTimeDatabaseFirebase new_register_node = RealTimeDatabaseFirebase.getInstance("root/studentList/" + usn.getText().toString().trim(), Object.class);
                                            new_register_node.insertDataRow("name", userName.getText().toString().trim());
                                        }

                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        makeText(getApplicationContext(), "Failed to register! try again!", LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            makeText(getApplicationContext(), "Failed to register! try again!", LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        }
    }
    boolean checkEmptyFields() {
        if(!Patterns.EMAIL_ADDRESS.matcher(mailId.getText().toString()).matches()){
            progressBar.setVisibility(View.GONE);
            mailId.setError("Valid mail Id is required.");
            mailId.requestFocus();
            return false;
        }
        else if(userName.getText().toString().trim().isEmpty()){
            progressBar.setVisibility(View.GONE);
            userName.setError("User Name is required.");
            userName.requestFocus();
            return false;
        }
        else if(usn.getText().toString().trim().isEmpty()){
            progressBar.setVisibility(View.GONE);
            usn.setError("USN is required.");
            usn.requestFocus();
            return false;
        }
        else if(password.getText().toString().length() < 6){
            progressBar.setVisibility(View.GONE);
            password.setError("Minimum Password length Should be 6 characters.");
            password.requestFocus();
            return false;
        }
        else{
            return true;
        }
    }
}