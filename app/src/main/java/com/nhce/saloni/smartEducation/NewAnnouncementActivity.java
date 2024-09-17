package com.nhce.saloni.smartEducation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class NewAnnouncementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_announcement);
    }
    public void goToNotes(View view) {
        makeText(getApplicationContext(), "Announcement will not be saved", LENGTH_SHORT).show();
        Utility.redirectActivity(NewAnnouncementActivity.this, AnnouncementsActivity.class);
    }

    public void clickSave(View view) {
        makeText(getApplicationContext(), "Announcement will be saved", LENGTH_SHORT).show();
        //                                                                                                 root/subjectList/sci001/announcements
        RealTimeDatabaseFirebase new_meeting_node = RealTimeDatabaseFirebase.getInstance("root/subjectList/" + RealTimeData.getInstance().getSubjectId()+"/announcements", Object.class);
        new_meeting_node.insertDataRow(((EditText)findViewById(R.id.title)).getText().toString(), ((EditText)findViewById(R.id.information)).getText().toString());
        Utility.redirectActivity(NewAnnouncementActivity.this, AnnouncementsActivity.class);
    }
}