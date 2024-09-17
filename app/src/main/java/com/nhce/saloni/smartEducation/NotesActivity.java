package com.nhce.saloni.smartEducation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.nhce.saloni.smartEducation.R.layout.activity_listview;

public class NotesActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    //inertialize Variables
    DrawerLayout drawerLayout;
    ListView listOfNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        listOfNotes = findViewById(R.id.listOfNotes);

        setListActivityObserver();

        listOfNotes.setOnItemClickListener(getListofEventItemClickListner());

        //Assign Variables
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    @NotNull
    private AdapterView.OnItemClickListener getListofEventItemClickListner() {
        return (parent, view, position, id) -> {
           Utility.redirectActivity(NotesActivity.this, IndividualNoteActivity.class,parent.getItemAtPosition(position).toString());
        };
    }
    public void addNewNote(View view) {
        Utility.redirectActivity(NotesActivity.this,NewNoteActivity.class);
    }

    private void setListActivityObserver() {
        RealTimeDatabaseFirebase.getInstance("root/" + RealTimeData.getInstance().getUserListType()+"/"+RealTimeData.getInstance().getUserId()+"/"+RealTimeData.getInstance().getSubjectId()+"/notes", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(HashMap<String, Object> customObj) {
                listOfNotes.setAdapter(new ArrayAdapter<String>(NotesActivity.this, activity_listview, customObj.keySet().stream().map(id -> String.valueOf(id)).collect(Collectors.toList())));
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });
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
            case R.id.mySubjects: Utility.redirectActivity(NotesActivity.this,SubjectActivity.class);
                return true;
            case R.id.schedule: Utility.redirectActivity(NotesActivity.this,DateActivity.class);
                return true;
            case R.id.myAccount: Toast.makeText(NotesActivity.this, "Selected: myAccount", Toast.LENGTH_LONG).show();
                return true;
            default: Toast.makeText(NotesActivity.this, "Nothing selected in Spinner Option", Toast.LENGTH_SHORT).show();
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
        //Redirect Activity to Notes
        Utility.redirectActivity(this,AnnouncementsActivity.class);
    }
    public void ClickNotes(View view){
        //Recreate activity
        recreate();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}