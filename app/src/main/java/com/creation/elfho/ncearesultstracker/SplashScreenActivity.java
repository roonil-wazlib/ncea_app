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
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SplashScreenActivity extends Activity implements View.OnClickListener {
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String currentLanguage = "en", currentLang;
    private ImageView logo, settings;
    String [] studyTips = {"Study tip: Try to explain difficult concepts to others to help you learn them better.", "Study tip: Practicing past NCEA papers is the most useful way of anticipating what will be in your exam.", "Study tip: Make a cheat sheet of each standard before your exam. You can't take it in, but summarising everything you need to know will help you concentrate on only what is relevant.", "Study tip: Highlighting doesn't help you memorise, Becky. Try some flashcards.", "Study tip: Try the Pomodoro technique - 25 minutes of work, then a 5 minute break to recharge.", "Exam tip: Remember your tissues, no-one wants to hear you sniffing in the middle of their chemistry paper.", "Exam tip: You can install an update for graphics calculators that displays answers in terms of Pi and surd form. It won't get wiped on a reset and can save your life in calculus.", "Study tip: If you have no friends, explaining things to your cat is a good substitute.", "Exam tip: remember to bring like 300 pens because I swear they will all run out of ink in the middle of your first essay.", "Exam tip: you can actually find out what's going to be on the exam ahead of time. It's published in a top secret location called the marking schedule.", "Study tip: If tomorrow's not the due date, today's not the do date.", "Study tip: Make a table of the topics of the questions on past exams to identify what is most likely to come up.", "Study tip: Make sure you have plenty of healthy snacks at your desk to keep your concentration up when studying.", "Exam tip: Remember when you exam is for Christ's sake how do people screw that up?!"};
    private TextView studyTip;
    private int current;
    Locale myLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-2997244423292030~9971169343");

        //set theme before starting activity
        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                setTheme(R.style.DayTheme);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = (ImageView)findViewById(R.id.logo);
        settings = (ImageView)findViewById(R.id.settings);
        studyTip = (TextView)findViewById(R.id.studyTip);

        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                settings.setImageResource(R.drawable.settings_black);
                logo.setImageResource(R.drawable.app_logo_transparent_black);
            }
        }

        //if (sharedpreferences.contains("language")){
            //if (sharedpreferences.getString("language", "").contains("english")) {
                //currentLanguage = "en";
            //}
            //else {
                //currentLanguage = "ma";
            //}
            //setLocale(currentLanguage);
        //}


        Random random = new Random();
        int index = random.nextInt(studyTips.length);
        current = index;
        studyTip.setText(studyTips[index]);
    }

    public void onClick(View view){
        if(view.getId()==R.id.help) {
            Intent i = new Intent(SplashScreenActivity.this, HelpActivity.class);
            startActivity(i);
        }
        else if(view.getId()==R.id.settings) {
            Intent i = new Intent(SplashScreenActivity.this, SettingsActivity.class);
            i.putExtra("refresh", 1);
            startActivity(i);
        }
        else if(view.getId()==R.id.studyTip){
            newTip();
        }
        else {
            if (sharedpreferences.contains("level")){
                if (sharedpreferences.getString("level", "").contains("1")) {
                    Intent i = new Intent(SplashScreenActivity.this, Level1Activity.class);
                    startActivity(i);
                }else if(sharedpreferences.getString("level", "").contains("3")){
                    Intent i = new Intent(SplashScreenActivity.this, Level3Activity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashScreenActivity.this, Level2Activity.class);
                    startActivity(i);
                }
            }
            else {
                Intent i = new Intent(SplashScreenActivity.this, Level2Activity.class);
                startActivity(i);
            }
        }
    }
    public void newTip(){
        if(current==studyTips.length-1){
            current = 0;
        }else {
            current += 1;
        }

        studyTip.setText(studyTips[current]);
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
}
