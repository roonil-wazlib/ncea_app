package com.example.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by elfho on 25/01/2018.
 */

public class AddGradeActivity extends Activity {

    private ImageView goalGradeImg, mockGradeImg, finalGradeImg, metaN, metaA, metaM, metaE, arrow1, arrow2, arrow3;
    private DatabaseHelper myDb;
    private TextView goalGradeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        goalGradeImg = (ImageView) findViewById(R.id.goalGradeImg);
        mockGradeImg = (ImageView) findViewById(R.id.mockGradeImg);
        finalGradeImg = (ImageView) findViewById(R.id.finalGradeImg);

        metaN = (ImageView) findViewById(R.id.metaN);
        metaA = (ImageView) findViewById(R.id.metaA);
        metaM = (ImageView) findViewById(R.id.metaM);
        metaE = (ImageView) findViewById(R.id.metaE);

        arrow1 = (ImageView) findViewById(R.id.arrow1);
        arrow2 = (ImageView) findViewById(R.id.arrow2);
        arrow3 = (ImageView) findViewById(R.id.arrow3);

        goalGradeTxt = (TextView) findViewById(R.id.goalGradeTxt);

        displayGrades();
        goInvisible();
    }

    public void onFirstClick(View view) {
        arrow1.setVisibility(View.INVISIBLE);
        arrow2.setVisibility(View.INVISIBLE);
        arrow3.setVisibility(View.INVISIBLE);
        displayMeta();
        if (view.getId() == R.id.goalGradeImg) {
            arrow1.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.mockGradeImg) {
            arrow2.setVisibility(View.VISIBLE);
        } else {
            arrow3.setVisibility(View.VISIBLE);
        }
    }

    public void goInvisible() {
        metaN.setVisibility(View.INVISIBLE);
        metaA.setVisibility(View.INVISIBLE);
        metaM.setVisibility(View.INVISIBLE);
        metaE.setVisibility(View.INVISIBLE);

        arrow1.setVisibility(View.INVISIBLE);
        arrow2.setVisibility(View.INVISIBLE);
        arrow3.setVisibility(View.INVISIBLE);
    }

    public void displayMeta() {
        metaN.setVisibility(View.VISIBLE);
        metaA.setVisibility(View.VISIBLE);
        metaM.setVisibility(View.VISIBLE);
        metaE.setVisibility(View.VISIBLE);
    }

    public void onSecondClick(View view) {
        myDb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        int subjectId = extras.getInt("subjectId"); //subject unique ID
        int position = extras.getInt("position"); //index of standard on list within subject
        String subject_str = Integer.toString(subjectId);

        ArrayList<String> mArrayList = myDb.getStandards(subjectId);
        String standard_str = mArrayList.get(position);

        int option = checkGradeOption();

        if (option == 1) {
            myDb.updateGrades("GOAL", view.getTag().toString(), standard_str, subject_str);
        } else if (option == 2) {
            myDb.updateGrades("MOCK", view.getTag().toString(), standard_str, subject_str);
        } else {
            myDb.updateGrades("FINAL", view.getTag().toString(), standard_str, subject_str);
        }
        goInvisible();
        displayGrades();
    }

    public int checkGradeOption() {
        int option = 0;
        if (arrow1.getVisibility() == View.VISIBLE) {
            option = 1;
        } else if (arrow2.getVisibility() == View.VISIBLE) {
            option = 2;
        } else {
            option = 3;
        }
        return option;
    }

    public void displayGrades() {
        myDb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        int subjectId = extras.getInt("subjectId"); //subject unique ID
        int position = extras.getInt("position"); //index of standard on list within subject
        final String subject = Integer.toString(subjectId);

        ArrayList<String> mArrayList = myDb.getStandards(subjectId);
        final String standard = mArrayList.get(position);

        ArrayList<String> gradeList = myDb.getStandardGrades(subject, standard);
        final String goal_grade = gradeList.get(0);
        final String mock_grade = gradeList.get(1);
        final String final_grade = gradeList.get(2);

        //setImages(goal_grade,
        setImage(goal_grade, goalGradeImg);
        setImage(mock_grade, mockGradeImg);
        setImage(final_grade, finalGradeImg);
    }

    public void setImage(String grade, ImageView image) {
        if (grade.contains("A")) {
            image.setImageResource(R.drawable.achieved);
        } else if (grade.contains("M")) {
            image.setImageResource(R.drawable.merit);
        } else if (grade.contains("E")) {
            image.setImageResource(R.drawable.excellence);
        } else if (grade.contains("N")) {
            image.setImageResource(R.drawable.not_achieved);
        }
    }

    @Override
    public void onBackPressed() {
        Bundle extras = getIntent().getExtras();
        String subject = extras.getString("subject");
        int level = extras.getInt("level");
        Intent i = new Intent(AddGradeActivity.this, SubjectActivity.class);
        // sending data to new activity
        i.putExtra("subject", subject);
        i.putExtra("level", level);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
}