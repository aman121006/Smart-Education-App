package com.nhce.saloni.smartEducation;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.nhce.saloni.smartEducation.R.layout.activity_listview;

public class SubjectActivity extends AppCompatActivity {
    ListView listOfSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        listOfSubjects = findViewById(R.id.listOfSubjects);

        setListActivityObserver();

        listOfSubjects.setOnItemClickListener(getListofEventItemClickListner());

    }

    @NotNull
    private AdapterView.OnItemClickListener getListofEventItemClickListner() {
        return (parent, view, position, id) -> {
            RealTimeData.getInstance().setSubjectId(parent.getItemAtPosition(position).toString());
            RealTimeData.getInstance().setDate((new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime()));
            makeText(getApplicationContext(), RealTimeData.getInstance().getSubjectId() +" Clicked", LENGTH_SHORT).show();
            Utility.redirectActivity(SubjectActivity.this, EventActivity.class);
        };
    }

    private void setListActivityObserver() {
        RealTimeDatabaseFirebase.getInstance("root/" + RealTimeData.getInstance().getUserListType() +"/" + RealTimeData.getInstance().getUserId(), Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(HashMap<String, Object> customObj) {
                List<String> SubList = new ArrayList<>();
                for(String Subject: customObj.keySet().stream().map(id -> String.valueOf(id)).collect(Collectors.toList())){
                    if(!Subject.equals("name")){
                        SubList.add(Subject);
                    }
                }
                listOfSubjects.setAdapter(new ArrayAdapter<String>(SubjectActivity.this, activity_listview, SubList));
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });
    }

    /*
    public void addItem(View view) {
        //TODO:  Check for Empty  DataRow;
        Toast.makeText(
                this,
                Subjects.getInstance().insertDataRow(null, new Subject(txtsubjectId.getText().toString(), txtsubjectName.getText().toString(), txtuserId.getText().toString()))
                        ? "Deal Saved"
                        : "Deal couldn't be Saved",
                Toast.LENGTH_LONG).show();
        clean();
    }
    private void clean() {
        txtsubjectId.setText("");
        txtsubjectName.setText("");
        txtuserId.setText("");
    }
    */

    public void joinSubject(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.alert_join_subject,null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        ((Button)mView.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ((Button)mView.findViewById(R.id.btn_okay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((EditText)mView.findViewById(R.id.subId)).getText().toString().trim().isEmpty()){
                    ((EditText)mView.findViewById(R.id.subId)).setError("Subject code is required");
                    ((EditText)mView.findViewById(R.id.subId)).requestFocus();
                }
                else{
                    RealTimeDatabaseFirebase join_subject_node = RealTimeDatabaseFirebase.getInstance("root/subjectList/"+((EditText)mView.findViewById(R.id.subId)).getText().toString() +  "/" + RealTimeData.getInstance().getUserListType(), Object.class);
                    join_subject_node.insertDataRow(RealTimeData.getInstance().getUserId(), RealTimeData.getInstance().getUserName());
                    RealTimeDatabaseFirebase new_meeting_node = RealTimeDatabaseFirebase.getInstance("root/" +RealTimeData.getInstance().getUserListType() + "/"+ RealTimeData.getInstance().getUserId() + "/" +  ((EditText)mView.findViewById(R.id.subId)).getText().toString() + "/notes", Object.class);
                    new_meeting_node.insertDataRow("default", "Add notes here");
                    makeText(getApplicationContext(), ((EditText)mView.findViewById(R.id.subId)).getText().toString() + "\n" + RealTimeData.getInstance().getUserName(), LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    public void addSubject(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.alert_new_subject,null);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        ((Button)mView.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ((Button)mView.findViewById(R.id.btn_okay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmptyFields()) {
                    RealTimeDatabaseFirebase new_subject_node = RealTimeDatabaseFirebase.getInstance("root/subjectList/"+((EditText)mView.findViewById(R.id.subId)).getText().toString(), Object.class);
                    new_subject_node.insertDataRow("name", ((EditText)mView.findViewById(R.id.name)).getText().toString());
                    RealTimeDatabaseFirebase new_meeting_node = RealTimeDatabaseFirebase.getInstance("root/" +RealTimeData.getInstance().getUserListType() + "/"+ RealTimeData.getInstance().getUserId() + "/" +  ((EditText)mView.findViewById(R.id.subId)).getText().toString() + "/notes", Object.class);
                    new_meeting_node.insertDataRow("default", "Add notes here");
                    makeText(getApplicationContext(), ((EditText)mView.findViewById(R.id.subId)).getText().toString() + "\n" + ((EditText)mView.findViewById(R.id.name)).getText().toString(), LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
            private boolean checkEmptyFields() {
                ((EditText)mView.findViewById(R.id.name)).setError(null);
                ((EditText)mView.findViewById(R.id.subId)).setError(null);
                if(((EditText)mView.findViewById(R.id.name)).getText().toString().trim().isEmpty()){
                    ((EditText)mView.findViewById(R.id.name)).setError("Subject name is required");
                    ((EditText)mView.findViewById(R.id.name)).requestFocus();
                    return false;
                }
                else if(((EditText)mView.findViewById(R.id.subId)).getText().toString().trim().isEmpty()){
                    ((EditText)mView.findViewById(R.id.subId)).setError("Subject code is required");
                    ((EditText)mView.findViewById(R.id.subId)).requestFocus();
                    return false;
                }
                else
                    return true;
            }
        });
        alertDialog.show();
    }

    public void goToDateActivity(View view) {
        //Redirect Activity to Subjects
        Utility.redirectActivity(this,DateActivity.class);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}