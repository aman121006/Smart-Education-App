package com.nhce.saloni.smartEducation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class IndividualNoteActivity extends AppCompatActivity {
    String noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_note);

        noteId = getIntent().getStringExtra("extraInfo");
        ((EditText)findViewById(R.id.noteTitle)).setText(noteId);

        setListActivityObserver();
    }


    private void setListActivityObserver() {
        //                                                      root/studentList/101/sci001/notes
        RealTimeDatabaseFirebase.getInstance("root/" + RealTimeData.getInstance().getUserListType() + "/" + RealTimeData.getInstance().getUserId() + "/" + RealTimeData.getInstance().getSubjectId() + "/notes", noteId, String.class).datarowObservableSubject.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(String customObj) {
                ((EditText) findViewById(R.id.noteInfo)).setText(customObj);
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });
    }

    public void goToNotes(View view) {
        makeText(getApplicationContext(), "Edit in the note will be saved", LENGTH_SHORT).show();
        //                                                                                                 root/studentList/101/sci001/notes
        RealTimeDatabaseFirebase new_meeting_node = RealTimeDatabaseFirebase.getInstance("root/" + RealTimeData.getInstance().getUserListType()+"/"+RealTimeData.getInstance().getUserId()+"/"+RealTimeData.getInstance().getSubjectId()+"/notes", Object.class);
        new_meeting_node.insertDataRow(((EditText)findViewById(R.id.noteTitle)).getText().toString(), ((EditText)findViewById(R.id.noteInfo)).getText().toString());
        Utility.redirectActivity(IndividualNoteActivity.this, NotesActivity.class);
    }
}