package com.nhce.saloni.smartEducation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nhce.saloni.smartEducation.utility.Utility;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    /*
    public void GoToLogin(View view){
        makeText(getApplicationContext(), "Login Button Clicked", LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void GoToSignUp(View view){
        makeText(getApplicationContext(), "Register Button Clicked", LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
    public void Skipped(View view){
        makeText(getApplicationContext(), "Skipped", LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, TeamsActivity.class);
        startActivity(intent);
    }*/

    public void GoToLogin(View view){
        makeText(getApplicationContext(), "Enter Login Credentials ", LENGTH_SHORT).show();
        //Redirect Activity to Teams
        Utility.redirectActivity(this,LoginActivity.class);
    }
    public void goToRegistration(View view){
        makeText(getApplicationContext(), "Register Button Clicked", LENGTH_SHORT).show();
        //Redirect Activity to SignUp
        Utility.redirectActivity(this,RegisterActivity.class);
    }
    public void goToDateActivity(View view) {
        makeText(getApplicationContext(), "Date Activity", LENGTH_SHORT).show();
        //Redirect Activity to Date
        Utility.redirectActivity(this,DateActivity.class);
    }
    public void goToSubjectChannel(View view){
        makeText(getApplicationContext(), "Skipped", LENGTH_SHORT).show();
        //Redirect Activity to Teams
        Utility.redirectActivity(this,SubjectActivity.class);
    }
}