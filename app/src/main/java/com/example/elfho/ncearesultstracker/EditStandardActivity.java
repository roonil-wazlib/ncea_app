package com.example.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by elfho on 27/04/2018.
 */

public class EditStandardActivity extends Activity {
    DatabaseHelper myDb;
    private EditText enteredStandard;
    private TextView credits;
    private Switch standardType, examType;
    private ImageView up, down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_standard);

        credits = (TextView)findViewById(R.id.credits);
        standardType = (Switch)findViewById(R.id.standardType);
        examType = (Switch)findViewById(R.id.examType);
        up = (ImageView)findViewById(R.id.up);
        down = (ImageView)findViewById(R.id.down);
        enteredStandard = (EditText)findViewById(R.id.enteredStandard);
        myDb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        String subjectId = extras.getString("id");
        String standard = extras.getString("standard");


        enteredStandard.setText(standard);
        ArrayList<String> mArrayList = myDb.getInfo(subjectId, standard);

        String standard_type = mArrayList.get(0);
        String exam_type = mArrayList.get(1);
        String credit_number = mArrayList.get(2);

        credits.setText(credit_number);
        if(standard_type.contains("0")){
            //set standard to unit standard
            standardType.setChecked(true);
            standardType.setText("Unit Standard");
            examType.setVisibility(View.INVISIBLE);
        }
        if(exam_type.contains("1")){
            //set exam type to external
            examType.setChecked(true);
            examType.setText("External");
        }
        //set other details to saved standard information (code for how to do this is in StandardInfoActivity
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
            Bundle extras = getIntent().getExtras();
            String subject = extras.getString("subject");
            int level = extras.getInt("level");
            Intent i = new Intent(EditStandardActivity.this, SubjectActivity.class);
            i.putExtra("subject", subject);
            i.putExtra("level", level);
            startActivity(i);
            overridePendingTransition(0, 0);
        }

        else if(view.getId() == R.id.save) {
            Bundle extras = getIntent().getExtras();
            String subjectId = extras.getString("id");
            String subject = extras.getString("subject");
            String standard = extras.getString("standard");

            //get grades
            ArrayList<String> gradeList = myDb.getStandardGrades(subjectId, standard);
            final String goal_grade = gradeList.get(0);
            final String mock_grade = gradeList.get(1);
            final String final_grade = gradeList.get(2);

            //delete old version of standard
            myDb.deleteStandard(subjectId, standard);

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

            String standard2 = enteredStandard.getText().toString();

            //create new standard
            boolean isInserted = myDb.insertStandard(standard2, subjectId, standard_type, exam_type, credit_num, "+", level_txt);

            //add old grades to new standard
            myDb.updateGrades("GOAL", goal_grade, standard2, subjectId);
            myDb.updateGrades("MOCK", mock_grade, standard2, subjectId);
            myDb.updateGrades("FINAL", final_grade, standard2, subjectId);

            if(isInserted == true) {
                Toast.makeText(EditStandardActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(EditStandardActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
            }

            Intent i = new Intent(EditStandardActivity.this, SubjectActivity.class);
            i.putExtra("subject", subject);
            i.putExtra("level", level);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
    }

    public int getCredits() {
        String credits_str = credits.getText().toString();
        int credits_int = Integer.parseInt(credits_str);
        return credits_int;
    }
}
