package com.creation.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by elfho on 20/12/2017.
 */
public class NewStandardActivity extends Activity {

    private Button savebtn;
    private EditText enteredStandard;
    DatabaseHelper myDb;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        //set theme before starting activity
        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                setTheme(R.style.DayTheme);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstandard);
        myDb = new DatabaseHelper(this);
        savebtn = (Button)findViewById(R.id.savebtn);
        enteredStandard = (EditText)findViewById(R.id.enteredStandard);
    }

    public void onClick(View view) {

        if (view.getId() == R.id.savebtn) {

            Bundle extras = getIntent().getExtras();
            int subjectId = extras.getInt("id");
            String subjectId_str = Integer.toString(subjectId);
            String subject = extras.getString("subject");
            int level = extras.getInt("level");

            Intent i = new Intent(NewStandardActivity.this, NewStandardDetailsActivity.class);
            i.putExtra("id", subjectId_str);
            i.putExtra("standard", enteredStandard.getText().toString());
            i.putExtra("subject", subject);
            i.putExtra("level", level);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }
    }
}