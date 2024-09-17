package com.nhce.saloni.smartEducation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nhce.saloni.smartEducation.data.model.TVSerial;
import com.nhce.saloni.smartEducation.data.table.TVSeries;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class AddActivity extends AppCompatActivity {

    EditText txtTitle,txtDescription, txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_button:
                return createAndAddMovieToDatabase();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Boolean createAndAddMovieToDatabase() {
        //TODO:  Check for Empty  DataRow;
        Toast.makeText(
                this,
                TVSeries.getInstance().insertDataRow(null, new TVSerial(txtTitle.getText().toString(), txtDescription.getText().toString(), txtPrice.getText().toString(), ""))
                        ? "Deal Saved"
                        : "Deal couldn't be Saved",
                Toast.LENGTH_LONG).show();
        clean();
        return true;
    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    public void goToSeeList(View view){
        makeText(getApplicationContext(), "Register Button Clicked", LENGTH_SHORT).show();
        //Redirect Activity to SignUp
        Intent intent = new Intent(this,ListActivity.class);
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