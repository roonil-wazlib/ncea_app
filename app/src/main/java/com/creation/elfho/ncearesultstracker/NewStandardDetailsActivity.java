package com.creation.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by elfho on 21/01/2018.
 */

public class NewStandardDetailsActivity extends Activity {
    DatabaseHelper myDb;
    private TextView standardName, credits;
    private Switch standardType, examType;
    private ImageView up, down;
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
        setContentView(R.layout.activity_newstandard_details);

        standardName = (TextView)findViewById(R.id.standardName);
        credits = (TextView)findViewById(R.id.credits);
        standardType = (Switch)findViewById(R.id.standardType);
        examType = (Switch)findViewById(R.id.examType);
        up = (ImageView) findViewById(R.id.up);
        down = (ImageView)findViewById(R.id.down);
        myDb = new DatabaseHelper(this);

        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                up.setImageResource(R.drawable.up_arrow_day);
                down.setImageResource(R.drawable.down_arrow_day);
            }
        }

        Bundle extras = getIntent().getExtras();
        String subjectId = extras.getString("id");
        String standard = extras.getString("standard");

        standardName.setText(standard);
    }

    public void onClick(View view) {

        if (view.getId() == R.id.standardType) {
            if(standardType.getText().toString() == "Unit Standard") {
                standardType.setText("Achievement Standard");
                examType.setVisibility(View.VISIBLE);
            }
            else {
                standardType.setText("Unit Standard");
                examType.setVisibility(View.INVISIBLE);
            }
        }

        else if(view.getId() == R.id.examType) {
            if(examType.getText().toString() == "External") {
                examType.setText("Internal");
            }
            else {
                examType.setText("External");
            }
        }

        else if(view.getId() == R.id.up) {
            int num_credits = getCredits();
            int new_credits = num_credits + 1;
            String new_credits_str = Integer.toString(new_credits);
            credits.setText(new_credits_str);
        }

        else if(view.getId() == R.id.down) {
            int num_credits = getCredits();
            if(num_credits > 1) {
                int new_credits = num_credits - 1;
                String new_credits_str = Integer.toString(new_credits);
                credits.setText(new_credits_str);
            }
        }

        else if(view.getId() == R.id.cancel) {
            onBackPressed();
        }

        else if(view.getId() == R.id.save) {
            Bundle extras = getIntent().getExtras();

            String subjectId = extras.getString("id");
            String standard = extras.getString("standard");
            String subject = extras.getString("subject");
            String standard_type = "1";
            String exam_type = "0";
            int level = extras.getInt("level");
            if(standardType.getText().toString() == "Unit Standard") {
                standard_type = "0";
                //exam type is 0 by default
            }
            if(examType.getText().toString() == "External") {
                exam_type = "1";
            }
            String level_txt = Integer.toString(level);
            String credit_num = credits.getText().toString();

            boolean isInserted = myDb.insertStandard(standard, subjectId, standard_type, exam_type, credit_num, "+", level_txt);

            if(isInserted == true) {
                Toast.makeText(NewStandardDetailsActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(NewStandardDetailsActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
            }

            Intent i = new Intent(NewStandardDetailsActivity.this, SubjectActivity.class);
            i.putExtra("subject", subject);
            i.putExtra("level", level);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    public int getCredits() {
        String credits_str = credits.getText().toString();
        int credits_int = Integer.parseInt(credits_str);
        return credits_int;
    }
}
