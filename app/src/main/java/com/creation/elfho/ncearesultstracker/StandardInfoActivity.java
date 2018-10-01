package com.creation.elfho.ncearesultstracker;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by elfho on 24/01/2018.
 */

public class StandardInfoActivity extends Activity {
    DatabaseHelper myDb;
    private TextView standardTxt, standardTypeTxt, examTypeTxt, creditsTxt;
    private Button editBtn, deleteBtn;
    String [] deleteOptions = {"Yes", "No"};
    static StandardInfoActivity activityB;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        //set theme before starting activity
        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                setTheme(R.style.DayTheme);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_info);

        myDb = new DatabaseHelper(this);

        activityB = this;

        standardTxt = (TextView)findViewById(R.id.standardTxt);
        standardTypeTxt = (TextView)findViewById(R.id.standardTypeTxt);
        examTypeTxt = (TextView)findViewById(R.id.examTypeTxt);
        creditsTxt = (TextView)findViewById(R.id.creditsTxt);

        editBtn = (Button)findViewById(R.id.editBtn);
        deleteBtn = (Button)findViewById(R.id.editBtn);

        Bundle extras = getIntent().getExtras();
        String standard = extras.getString("standard");
        int subjectId = extras.getInt("subject");
        String subject = Integer.toString(subjectId);

        ArrayList<String> mArrayList = myDb.getInfo(subject, standard);

        String standardType = mArrayList.get(0);
        String examType = mArrayList.get(1);
        String credits = mArrayList.get(2);

        standardTxt.setText(standard);

        if(standardType.contains("0")) {
            standardTypeTxt.setText("Unit Standard");
        }
        else {
            standardTypeTxt.setText("Achievement Standard");
        }


        if(examType.contains("0")) {
            examTypeTxt.setText("Internal");
        }
        else if(examType.contains("1")) {
            examTypeTxt.setText("External");
        }
        else {
            examTypeTxt.setText("N/A");
        }
        creditsTxt.setText(credits);
    }

    public void onClick(View view) {
        if(view.getId()==R.id.deleteBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?")
                    .setSingleChoiceItems(deleteOptions, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                            String selectedOption = deleteOptions[selectedPosition];

                            //respond to delete if clicked
                            if(selectedOption == "Yes") {
                                Bundle extras = getIntent().getExtras();
                                String standard = extras.getString("standard");
                                int subjectId = extras.getInt("subject");
                                String subject = Integer.toString(subjectId);

                                String subjectName = extras.getString("subjectName");
                                int level = extras.getInt("level");

                                myDb.deleteStandard(subject, standard);
                                Toast.makeText(StandardInfoActivity.this, "Standard deleted", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(StandardInfoActivity.this, SubjectActivity.class);
                                i.putExtra("subject", subjectName);
                                i.putExtra("level", level);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                            }
                        }
                    });
            AlertDialog ad = builder.create();
            ad.show();
        }
        else if(view.getId()==R.id.editBtn){
            Bundle extras = getIntent().getExtras();

            int subjectId = extras.getInt("subject");
            String subject = Integer.toString(subjectId);
            String standard = extras.getString("standard");
            String subjectName = extras.getString("subjectName");
            int level = extras.getInt("level");


            Intent i = new Intent(StandardInfoActivity.this, EditStandardActivity.class);
            i.putExtra("id", subject); //subject id
            i.putExtra("standard", standard);
            i.putExtra("subject", subjectName); //subject name
            i.putExtra("level", level);
            startActivity(i);
        }
        //else if edit go to create standard activity, pass standard in
        // (add stuff to the subject activity to pass in null and check if that happened),
        // adjust layout based on settings, if standard edited just delete old one and make a new one
    }

    public static StandardInfoActivity getInstance(){
        return activityB;
    }
}
