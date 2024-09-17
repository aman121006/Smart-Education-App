package com.nhce.saloni.smartEducation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.model.EventId;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EventActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    //inertialize Variables
    DrawerLayout drawerLayout;
    ListView  listOfEvent;
    //event_listview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //Assign Variables
        drawerLayout = findViewById(R.id.drawer_layout);
        listOfEvent  = (ListView)findViewById(R.id.listOfEvent);

        //((TextView) findViewById(R.id.SubjectList)).setText("root/date/" + RealTimeData.getInstance().getDate()+"/"+ RealTimeData.getInstance().getSubjectId()+" "+ RealTimeData.getInstance().getStartTime());

        setListActivityObserver2();
    }

    private void setListActivityObserver2(){
        RealTimeData.getDate();
        RealTimeData.getSubjectId();
        RealTimeDatabaseFirebase.getInstance("root/date/" + RealTimeData.getDate(), Object.class).datatableObservableSubject.subscribe(new Observer<Object>(){
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Object customObj) {
                listOfEvent.setAdapter(
                    new ListviewEvent(
                        EventActivity.this,
                        R.layout.event_listview,
                        (((HashMap<String, Object>) customObj)
                            .keySet()
                            .stream()
                            .filter(id -> id.contains(RealTimeData.getSubjectId()))
                            .map(id -> (HashMap<String, Object>) ((HashMap<String, Object>) customObj).get(id))
                            .map(customobj2 -> new EventId(
                                    customobj2.get("startTime").toString(),
                                    customobj2.get("endTime").toString(),
                                    customobj2.get("objective").toString(),
                                    customobj2.get("readingMaterial").toString()
                                )
                            )
                            .collect(Collectors.toList())
                        )
                    )
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

        /*
        RealTimeDatabaseFirebase.getInstance("root/date/" + RealTimeData.getInstance().getDate(), RealTimeData.getInstance().getSubjectId()+" "+ RealTimeData.getInstance().getStartTime(), EventId.class).datarowObservableSubject.subscribe(new Observer<EventId>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(EventId customObj) {
                //((TextView) findViewById(R.id.SubjectList)).setText(customObj.getReadingMaterial() + "\t" + customObj.getStartTime());
                                EventList.add(new EventId(subdesc.getStartTime(), subdesc.getEndTime(), subdesc.getObjective(), subdesc.getReadingMaterial()));
                ((TextView) findViewById(R.id.startTime)).setText(customObj.getStartTime());
                ((TextView) findViewById(R.id.endTime)).setText(customObj.getEndTime());
                ((TextView) findViewById(R.id.objective)).setText(customObj.getObjective());
                ((TextView) findViewById(R.id.readingMaterial)).setText(customObj.getReadingMaterial());
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });
         */



    public void ClickOptions(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.options);
        popup.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mySubjects: Utility.redirectActivity(EventActivity.this,SubjectActivity.class);
                return true;
            case R.id.schedule: Utility.redirectActivity(EventActivity.this,DateActivity.class);
                return true;
            case R.id.myAccount: Toast.makeText(EventActivity.this, "Selected: myAccount", Toast.LENGTH_LONG).show();
                return true;
            default: Toast.makeText(EventActivity.this, "Nothing selected in Spinner Option", Toast.LENGTH_SHORT).show();
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
        //Recreate activity
        recreate();
        //Or Close drawer
        //closeDrawer(drawerLayout);
    }
    public void ClickAnnouncements(View view){
        //Redirect Activity to Teams
        Utility.redirectActivity(this,AnnouncementsActivity.class);
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