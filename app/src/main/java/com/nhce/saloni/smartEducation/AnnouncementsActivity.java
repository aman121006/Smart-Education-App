package com.nhce.saloni.smartEducation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.nhce.saloni.smartEducation.R.layout.activity_listview;

public class AnnouncementsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    //inertialize Variables
    DrawerLayout drawerLayout;
    ListView listOfAnnouncements;
    TextView textView;
    Button addAnnouncements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        //Assign Variables
        drawerLayout = findViewById(R.id.drawer_layout);
        listOfAnnouncements = (ListView)findViewById(R.id.listOfAnnouncements);
        textView = (TextView)findViewById(R.id.textView);
        /*
        addAnnouncements = (Button)findViewById(R.id.addAnnouncements);
        if(RealTimeData.getInstance().getUserListType().equals("studentList")){
            addAnnouncements.setVisibility(View.GONE);
        }

         */
        setListActivityObserver();
    }

    private void setListActivityObserver() {
        //                                      root/subjectList/sci001/grades
        RealTimeDatabaseFirebase.getInstance("root/subjectList/" + RealTimeData.getInstance().getSubjectId() + "/announcements", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(HashMap<String, String> customObj) {
                listOfAnnouncements.setAdapter(new ArrayAdapter<String>(AnnouncementsActivity.this, activity_listview, customObj.keySet().stream().map(id -> String.valueOf(id) + ":\n" + customObj.get(id)).collect(Collectors.toList())));
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }


    public void addAnnouncements(View view) {
        makeText(getApplicationContext(), "Create a new announcement", LENGTH_SHORT).show();
        Utility.redirectActivity(AnnouncementsActivity.this, NewAnnouncementActivity.class);
    }

    public void ClickOptions(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.options);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mySubjects: Utility.redirectActivity(AnnouncementsActivity.this,SubjectActivity.class);
                return true;
            case R.id.schedule: Utility.redirectActivity(AnnouncementsActivity.this,DateActivity.class);
                return true;
            case R.id.myAccount: makeText(AnnouncementsActivity.this, "Selected: myAccount", Toast.LENGTH_LONG).show();
                return true;
            default: makeText(AnnouncementsActivity.this, "Nothing selected in Spinner Option", LENGTH_SHORT).show();
                return false;
        }
    }

    public void ClickMenu(View view){
        //Open drawer
        openDrawer(drawerLayout);
    }
    private static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer Layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        // Close draawer layout
        //Check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickEvents(View view){
        //Redirect Activity to Teams
        Utility.redirectActivity(this, EventActivity.class);
    }
    public void ClickAnnouncements(View view){
        //Recreate activity
        recreate();
    }
    public void ClickNotes(View view){
        //Redirect Activity to Notes
        Utility.redirectActivity(this,NotesActivity.class);
    }
    public void ClickParticipant(View view){
        //Redirect Activity to Notes
        Utility.redirectActivity(this,ParticipantActivity.class);
    }
    public void ClickAccount(View view){
        //Redirect Activity to Notes
        alertbtn(this);
    }
    private static void alertbtn(final Activity activity){
        //Inertialize to Alert dialogBox
        new AlertDialog.Builder(activity)
            .setTitle("Logout")
            .setMessage("Click  yes to cont...")
            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Finish activity
                    activity.finishAffinity();
                    //Exit app
                    System.exit(0);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ////Dismiss dialog
                    dialog.dismiss();
                }
            })
            .show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}