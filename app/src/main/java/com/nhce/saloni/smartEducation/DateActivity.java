package com.nhce.saloni.smartEducation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhce.saloni.smartEducation.data.injection.RealTimeData;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;
import com.nhce.saloni.smartEducation.utility.Utility;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.nhce.saloni.smartEducation.R.layout.activity_listview;

public class DateActivity extends AppCompatActivity {
    ListView listOfEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        listOfEvent = findViewById(R.id.listOfEvent);
        // Set Current Date to button
        //button.setText().format("yyyy-MM-dd").(RealTimeData.date);
        RealTimeData.getInstance().setDate((new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime()));
        ((Button)findViewById(R.id.btn_date)).setText(RealTimeData.getInstance().getDate());

        setListActivityObserver(RealTimeData.getInstance().getDate());

        listOfEvent.setOnItemClickListener(getListofEventItemClickListner());
    }

    @NotNull
    private AdapterView.OnItemClickListener getListofEventItemClickListner() {
        return (parent, view, position, id) -> {
            //new AdapterView.OnItemClickListener() {
            //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RealTimeData.getInstance().setSubjectId(parent.getItemAtPosition(position).toString().split(" ")[0]);
            RealTimeData.getInstance().setStartTime(parent.getItemAtPosition(position).toString().replace(parent.getItemAtPosition(position).toString().split(" ")[0]+" ",""));
            makeText(getApplicationContext(), RealTimeData.getInstance().getSubjectId() +" Clicked", LENGTH_SHORT).show();
            Utility.redirectActivity(DateActivity.this, EventActivity.class);
        };
    }

    public void selectDate(View view) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, selectedYear, monthOfYear, dayOfMonth) -> {
                    //new DatePickerDialog.OnDateSetListener() {
                    //    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                    calendar.set(selectedYear, monthOfYear, dayOfMonth);
                    RealTimeData.getInstance().setDate((new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime()));
                    ((Button)findViewById(R.id.btn_date)).setText(RealTimeData.getInstance().getDate());
                    setListActivityObserver(RealTimeData.getInstance().getDate());
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setListActivityObserver(String path) {
        RealTimeDatabaseFirebase.getInstance("root/date/" + path, Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) {
                listOfEvent.setAdapter(null);
            }
            @Override
            public void onNext(HashMap<String, Object> customObj) {
                    listOfEvent.setAdapter(new ArrayAdapter<String>(DateActivity.this, activity_listview, customObj.keySet().stream().map(id -> String.valueOf(id)).collect(Collectors.toList())));
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });
    }

    public void setTime(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    //new TimePickerDialog.OnTimeSetListener() {
                    //    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minuteOfHour);
                    editText.setText(new SimpleDateFormat("hh:mm a").format(calendar.getTime()));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    public void addEvent(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.alert_new_event,null);
        Spinner sub= (Spinner)mView.findViewById(R.id.sub);
        ((EditText)mView.findViewById(R.id.startTime)).setText(new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime()));
        ((EditText)mView.findViewById(R.id.endTime)).setText(new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime()));
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        final String[] subSelected = new String[1];
        RealTimeDatabaseFirebase.getInstance("root/subjectList", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
            @Override
            public void onSubscribe(Disposable d) { }
            @Override
            public void onNext(HashMap<String, Object> customObj) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, customObj.keySet().stream().map(id -> String.valueOf(id)).collect(Collectors.toList()));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sub.setAdapter(adapter);
                sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        subSelected[0] = parent.getItemAtPosition(position).toString();
                        Toast.makeText(parent.getContext(), "Selected: " + subSelected[0], Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });
            }
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { }
        });

        ((Button)mView.findViewById(R.id.btnStartTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime((EditText)mView.findViewById(R.id.startTime));
            }
        });
        ((Button)mView.findViewById(R.id.btnEndTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setTime((EditText)mView.findViewById(R.id.endTime)); }
        });
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
                    RealTimeDatabaseFirebase new_meeting_node = RealTimeDatabaseFirebase.getInstance("root/date/" + RealTimeData.getInstance().getDate() + "/" + subSelected[0]+" ("+((EditText)mView.findViewById(R.id.startTime)).getText().toString()+")", Object.class);
                    new_meeting_node.insertDataRow("startTime", ((EditText)mView.findViewById(R.id.startTime)).getText().toString());
                    new_meeting_node.insertDataRow("endTime", ((EditText)mView.findViewById(R.id.endTime)).getText().toString());
                    new_meeting_node.insertDataRow("readingMaterial", ((EditText)mView.findViewById(R.id.readingMaterial)).getText().toString());
                    new_meeting_node.insertDataRow("objective", ((EditText)mView.findViewById(R.id.objective)).getText().toString());
                    RealTimeDatabaseFirebase new_hostList_node = RealTimeDatabaseFirebase.getInstance("root/date/" + RealTimeData.getInstance().getDate() + "/" + subSelected[0]  +" ("+  ((EditText)mView.findViewById(R.id.startTime)).getText().toString() + ")"+ "/teacherList", Object.class);
                    RealTimeDatabaseFirebase.getInstance("root/subjectList/" + subSelected[0] + "/teacherList", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
                        @Override
                        public void onSubscribe(Disposable d) { }
                        @Override
                        public void onNext(HashMap<String, Object> customObj) {
                            for(HashMap.Entry<String, Object> Teacher: customObj.entrySet())
                                new_hostList_node.insertDataRow(Teacher.getKey(), Teacher.getValue());
                        }
                        @Override
                        public void onError(Throwable e) { }
                        @Override
                        public void onComplete() { }
                    });
                    RealTimeDatabaseFirebase new_participationList_node = RealTimeDatabaseFirebase.getInstance("root/date/" + RealTimeData.getInstance().getDate() + "/" + subSelected[0]  +" ("+  ((EditText)mView.findViewById(R.id.startTime)).getText().toString() + ")"+ "/studentList", Object.class);
                    RealTimeDatabaseFirebase.getInstance("root/subjectList/" + subSelected[0] + "/studentList", Object.class).datatableObservableSubject.subscribe(new Observer<HashMap<String, Object>>() {
                        @Override
                        public void onSubscribe(Disposable d) { }
                        @Override
                        public void onNext(HashMap<String, Object> customObj) {
                            for(HashMap.Entry<String, Object> Student: customObj.entrySet())
                                new_participationList_node.insertDataRow(Student.getKey(), false    );
                        }
                        @Override
                        public void onError(Throwable e) { }
                        @Override
                        public void onComplete() { }
                    });
                    String text = ((EditText)mView.findViewById(R.id.startTime)).getText().toString() + "\n" + ((EditText)mView.findViewById(R.id.endTime)).getText().toString() + "\n" + ((EditText)mView.findViewById(R.id.objective)).getText().toString() + "\n" + ((EditText)mView.findViewById(R.id.readingMaterial)).getText().toString()+"\n"+subSelected[0];
                    makeText(getApplicationContext(), text, LENGTH_SHORT).show();

                    alertDialog.dismiss();
                }
            }
            private boolean checkEmptyFields() {
                ((EditText)mView.findViewById(R.id.objective)).setError(null);
                ((EditText)mView.findViewById(R.id.readingMaterial)).setError(null);
                if(((EditText)mView.findViewById(R.id.objective)).getText().toString().trim().isEmpty()){
                    ((EditText)mView.findViewById(R.id.objective)).setError("objective is required");
                    ((EditText)mView.findViewById(R.id.objective)).requestFocus();
                    return false;
                }
                else if(((EditText)mView.findViewById(R.id.readingMaterial)).getText().toString().trim().isEmpty()){
                    ((EditText)mView.findViewById(R.id.readingMaterial)).setError("reading Material is required");
                    ((EditText)mView.findViewById(R.id.readingMaterial)).requestFocus();
                    return false;
                }
                else{
                    return true;
                }
            }
        });
        alertDialog.show();
    }

    public void goToSubjectChannel(View view){
        //Redirect Activity to Subjects
        Utility.redirectActivity(this,SubjectActivity.class);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}