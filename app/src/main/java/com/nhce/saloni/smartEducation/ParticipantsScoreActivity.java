package com.nhce.saloni.smartEducation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;

import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.nhce.saloni.smartEducation.R.layout.activity_listview;

public class ParticipantsScoreActivity extends AppCompatActivity {
    TextView exam;
    ListView studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_score);

        exam = (TextView) findViewById(R.id.exam);
        studentList = (ListView) findViewById(R.id.studentList);

        exam.setText(getIntent().getStringExtra("extraInfo"));
        setListActivityObserver(exam.getText().toString());

    }

    private void setListActivityObserver(String examType) {
        //                                                  root/subjectList/sci001/studentList
        RealTimeDatabaseFirebase.getInstance("root/subjectList/" + RealTimeData.getInstance().getSubjectId() + "/studentList", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(HashMap<String, Object> customObj) {
                HashMap<String, String> individualScores = new HashMap<String, String>();
                for(String studentId : customObj.keySet().stream().map(id -> String.valueOf(id)).collect(Collectors.toList())){
                    //                                                      root/studentList/101/sci001/grades
                    RealTimeDatabaseFirebase.getInstance("root/studentList/" + studentId + "/" + RealTimeData.getInstance().getSubjectId() + "/grades", getIntent().getStringExtra("extraInfo"), String.class).datarowObservableSubject.subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) { }
                        @Override
                        public void onNext(String customObj) {
                            individualScores.put(studentId,customObj);
                        }
                        @Override
                        public void onError(Throwable e) { }
                        @Override
                        public void onComplete() { }
                    });
                }
                studentList.setAdapter(new ArrayAdapter<String>(ParticipantsScoreActivity.this, activity_listview, individualScores.keySet().stream().map(id -> String.valueOf(id) + " : " + individualScores.get(id)).collect(Collectors.toList())));
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });
    }
}