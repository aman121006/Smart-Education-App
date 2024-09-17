package com.nhce.saloni.smartEducation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;

import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setListActivityObserver();
    }

    private void setListActivityObserver() {
        //RealTimeDatabaseFirebase.getInstance("root/date/2020-11-21/classes", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
        RealTimeDatabaseFirebase.getInstance("root/date/2020-11-21/classes/science/hostList/1001", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(HashMap<String, Object> customObj) {
                ((TextView) findViewById(R.id.MovieDeals)).setText(
                        customObj.keySet().stream().map(id -> String.valueOf(id) + ": " + customObj.get(id)).collect(Collectors.joining(", \n"))
                );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void goToAddRow(View view){
        makeText(getApplicationContext(), "Login Button Clicked", LENGTH_SHORT).show();
        //Redirect Activity to SignUp
        Intent intent = new Intent(this,AddActivity.class);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        this.startActivity(intent);
    }

    public void goToSubjectChannel(View view) {
        makeText(getApplicationContext(), "Register Button Clicked", LENGTH_SHORT).show();
        //Redirect Activity to SignUp
        Intent intent = new Intent(this,SubjectActivity.class);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        this.startActivity(intent);
    }
}
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setListActivityObserver();
    }

    private void setListActivityObserver() {
        TVSeries.getInstance().datatableObservableSubject.subscribe(new Observer<HashMap<String, TVSerial>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(HashMap<String, TVSerial> tVSeries) {
                ((TextView) findViewById(R.id.MovieDeals)).setText(
                        tVSeries.keySet().stream().map(id -> tVSeries.get(id).getTitle()).collect(Collectors.joining(", \n"))
                );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void GoToaddRow(View view){
        makeText(getApplicationContext(), "Login Button Clicked", LENGTH_SHORT).show();
        //Redirect Activity to SignUp
        Intent intent = new Intent(this,AddActivity.class);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        this.startActivity(intent);
    }

    public void GoToSubjectChannel(View view) {
        makeText(getApplicationContext(), "Register Button Clicked", LENGTH_SHORT).show();
        //Redirect Activity to SignUp
        Intent intent = new Intent(this,SubjectActivity.class);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        this.startActivity(intent);
    }

 */


