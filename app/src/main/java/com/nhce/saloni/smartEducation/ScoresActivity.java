package com.nhce.saloni.smartEducation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.nhce.saloni.smartEducation.R.layout.activity_listview;

public class ScoresActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    //inertialize Variables
    DrawerLayout drawerLayout;
    ListView listOfScores;
    TextView textView;
    Button addScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //Assign Variables
        drawerLayout = findViewById(R.id.drawer_layout);
        //listOfScores = (ListView)findViewById(R.id.listOfScores);
        textView = (TextView)findViewById(R.id.textView);
        addScores = (Button)findViewById(R.id.addScores);
        if(RealTimeData.getInstance().getUserListType().equals("studentList")){
            addScores.setVisibility(View.GONE);
        }

        setListActivityObserver();

        listOfScores.setOnItemClickListener(getListofEventItemClickListner());

    }

    @NotNull
    private AdapterView.OnItemClickListener getListofEventItemClickListner() {
        return (parent, view, position, id) -> {
                if(RealTimeData.getInstance().getUserListType().equals("teacherList")) {
                    makeText(getApplicationContext(), parent.getItemAtPosition(position).toString().split(" : ")[0] + " Clicked", LENGTH_SHORT).show();
                    Utility.redirectActivity(ScoresActivity.this, ParticipantsScoreActivity.class, parent.getItemAtPosition(position).toString().split(" : ")[0]);
                }
        };
    }
    private void setListActivityObserver() {
        if(RealTimeData.getInstance().getUserListType().equals("teacherList")) {
            //                                      root/subjectList/sci001/grades
            RealTimeDatabaseFirebase.getInstance("root/subjectList/" + RealTimeData.getInstance().getSubjectId() + "/grades", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(HashMap<String, Object> customObj) {
                    if (customObj.isEmpty())
                        textView.setVisibility(View.VISIBLE);
                    else
                        listOfScores.setAdapter(new ArrayAdapter<String>(ScoresActivity.this, activity_listview, customObj.keySet().stream().map(id -> String.valueOf(id) + " : " + customObj.get(id)).collect(Collectors.toList())));
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
        }
        else{
            //                                      root/studentList/101/sci001/grades
            RealTimeDatabaseFirebase.getInstance("root/studentList/" + RealTimeData.getInstance().getUserId() + "/" + RealTimeData.getInstance().getSubjectId() + "/grades", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(HashMap<String, Object> customObj) {
                    if (customObj.isEmpty())
                        textView.setVisibility(View.VISIBLE);
                    else
                        listOfScores.setAdapter(new ArrayAdapter<String>(ScoresActivity.this, activity_listview, customObj.keySet().stream().map(id -> String.valueOf(id) + " : " + customObj.get(id)).collect(Collectors.toList())));
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
        }
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
            case R.id.mySubjects: Utility.redirectActivity(ScoresActivity.this,SubjectActivity.class);
                return true;
            case R.id.schedule: Utility.redirectActivity(ScoresActivity.this,DateActivity.class);
                return true;
            case R.id.myAccount: Toast.makeText(ScoresActivity.this, "Selected: myAccount", Toast.LENGTH_LONG).show();
                return true;
            default: Toast.makeText(ScoresActivity.this, "Nothing selected in Spinner Option", Toast.LENGTH_SHORT).show();
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
    public void ClickScores(View view){
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