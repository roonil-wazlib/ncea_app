package com.creation.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by elfho on 20/12/2017.
 */
public class NewSubjectActivity extends Activity {

    private Button savebtn;
    private EditText enteredSubject;
    DatabaseHelper myDb;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private AdView mAdView;
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
        setContentView(R.layout.activity_newsubject);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
            else if(level == 2){
                Intent i = new Intent(NewSubjectActivity.this, Level2Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
            else if(level == 3){
                Intent i = new Intent(NewSubjectActivity.this, Level3Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }
        }
    }
}