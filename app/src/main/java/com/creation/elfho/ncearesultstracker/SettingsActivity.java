package com.creation.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.util.Locale;


public class SettingsActivity extends Activity {
    private RadioButton level1, level2, level3, english, maori, night, day;
    private RadioGroup radioGroup, radioGroup2, radioGroup3;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String currentLanguage = "en", currentLang;
    String confirmText = "Data Saved";
    Locale myLocale;
    private ImageView house;

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
        setContentView(R.layout.activity_settings);

        level1 = (RadioButton)findViewById(R.id.level1);
        level2 = (RadioButton)findViewById(R.id.level2);
        level3 = (RadioButton)findViewById(R.id.level3);
        english = (RadioButton)findViewById(R.id.english);
        maori = (RadioButton)findViewById(R.id.maori);
        night = (RadioButton)findViewById(R.id.night);
        day = (RadioButton)findViewById(R.id.day);
        radioGroup = (RadioGroup)findViewById(R.id.myRadioGroup);
        radioGroup2 = (RadioGroup)findViewById(R.id.my2ndRadioGroup);
        radioGroup3 = (RadioGroup)findViewById(R.id.my3rdRadioGroup);
        house = (ImageView)findViewById(R.id.home);



        if (sharedpreferences.contains("level")){
            if (sharedpreferences.getString("level", "").contains("1")) {
                radioGroup.check(R.id.level1);
            }else if(sharedpreferences.getString("level", "").contains("2")){
                radioGroup.check(R.id.level2);
            }
            else {
                radioGroup.check(R.id.level3);
            }
        }
        if (sharedpreferences.contains("language")){
            if (sharedpreferences.getString("language", "").contains("english")) {
                radioGroup2.check(R.id.english);
                currentLanguage = "en";
            }
            else {
                currentLanguage = "ma";
                radioGroup2.check(R.id.maori);
                confirmText = "Kua tiakina te raruanga";
            }
        }
        radioGroup3.check(R.id.night);
        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                radioGroup3.check(R.id.day);
                house.setImageResource(R.drawable.home_black);
            }
        }

        Bundle extras = getIntent().getExtras();
        int refresh = extras.getInt("refresh");

        if(refresh==1){
            setLocale(currentLanguage);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.home) {
            onBackPressed();
        }
    }
    public void onRadioClick(View view){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(view.getId()==R.id.level1){
            editor.putString("level", "1");
            editor.commit();
        }
        else if(view.getId()==R.id.level2){
            editor.putString("level", "2");
            editor.commit();
        }
        else if(view.getId()==R.id.level3){
            editor.putString("level", "3");
            editor.commit();

        }
        else if(view.getId()==R.id.english){
            editor.putString("language", "english");
            editor.commit();
            setLocale("en");
            confirmText = "Data Saved";
        }
        else {
            editor.putString("language", "maori");
            editor.commit();
            setLocale("ma");
            confirmText = "Kua tiakina te raruanga";
        }
        Toast.makeText(SettingsActivity.this, confirmText, Toast.LENGTH_LONG).show();
    }
    public void onColorRadioClick(View view){
        SharedPreferences.Editor editor = sharedpreferences.edit();

        if(view.getId()==R.id.night){
            editor.putString("color", "night");
            editor.commit();
            Intent refresh = new Intent(this, SettingsActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            refresh.putExtra("refresh", 0);
            startActivity(refresh);
            overridePendingTransition(0,0);
        }
        else {
            editor.putString("color", "day");
            editor.commit();
            Intent refresh = new Intent(this, SettingsActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            refresh.putExtra("refresh", 0);
            startActivity(refresh);
            overridePendingTransition(0,0);
        }
        //data saved toast
    }

    public void setLocale(String localeName){
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, SettingsActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            refresh.putExtra("refresh", 0);
            startActivity(refresh);
            overridePendingTransition(0,0);
    }

    public void onBackPressed(){
        Intent i = new Intent(this, SplashScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }
}
