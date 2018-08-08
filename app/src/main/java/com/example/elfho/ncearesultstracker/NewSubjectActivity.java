package com.example.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by elfho on 20/12/2017.
 */
public class NewSubjectActivity extends Activity {

    private Button savebtn;
    private EditText enteredSubject;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsubject);
        myDb = new DatabaseHelper(this);
        savebtn = (Button)findViewById(R.id.savebtn);
        enteredSubject = (EditText)findViewById(R.id.enteredSubject);
    }

    public void onClick(View view) {

        if (view.getId() == R.id.savebtn) {

            Bundle extras = getIntent().getExtras();

            int level = extras.getInt("level", -1);
            String ncea_level = Integer.toString(level);

            boolean isInserted = myDb.insertSubject(enteredSubject.getText().toString(), ncea_level);

            if(isInserted == true) {
                Toast.makeText(NewSubjectActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(NewSubjectActivity.this, "You already have a subject with this name", Toast.LENGTH_LONG).show();
            }

            if(level == 1){
                Intent i = new Intent(NewSubjectActivity.this, Level1Activity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
            else if(level == 2){
                Intent i = new Intent(NewSubjectActivity.this, Level2Activity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
            else if(level == 3){
                Intent i = new Intent(NewSubjectActivity.this, Level3Activity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        }
    }
}