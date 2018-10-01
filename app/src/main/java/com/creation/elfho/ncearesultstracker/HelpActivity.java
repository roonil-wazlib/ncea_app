package com.creation.elfho.ncearesultstracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by elfho on 22/07/2018.
 */

public class HelpActivity extends AppCompatActivity {
    private TextView title, body_text;
    private Button app_help, ncea_info, suggestions;
    private ImageView house;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public String color = "night";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        //set theme before starting activity
        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                setTheme(R.style.DayTheme);
                color = "day";
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);

        title=(TextView)findViewById(R.id.title);
        body_text=(TextView)findViewById(R.id.body_text);

        app_help=(Button)findViewById(R.id.app_help);
        ncea_info=(Button)findViewById(R.id.ncea_info);
        suggestions=(Button)findViewById(R.id.suggestions);
        house=(ImageView)findViewById(R.id.home);

        body_text.setText(Html.fromHtml(getString(R.string.app_help_str)));

        if (color=="day") {
            house.setImageResource(R.drawable.home_black);
        }
    }

    public void onClick(View view){
        app_help.setTextColor(Color.parseColor("#000000"));
        ncea_info.setTextColor(Color.parseColor("#000000"));
        suggestions.setTextColor(Color.parseColor("#000000"));
        app_help.setBackgroundResource(android.R.drawable.btn_default);
        ncea_info.setBackgroundResource(android.R.drawable.btn_default);
        suggestions.setBackgroundResource(android.R.drawable.btn_default);

        if(view.getId()==R.id.home) {
            onBackPressed();
        }

        else if(view.getId()==R.id.app_help){
            title.setText("App Help");
            body_text.setText(Html.fromHtml(getString(R.string.app_help_str)));
            //change button background to look like you've switched tabs or something
            app_help.setBackgroundResource(R.drawable.current_level_back);
            if(color=="day"){
                app_help.setTextColor(Color.parseColor("#ffffffff"));
            }
        }

        else if(view.getId()==R.id.ncea_info){
            title.setText("NCEA Info");
            body_text.setText(Html.fromHtml(getString(R.string.ncea_info_str)));
            //change button background to look like you've switched tabs or something
            ncea_info.setBackgroundResource(R.drawable.current_level_back);
            if(color=="day"){
                ncea_info.setTextColor(Color.parseColor("#ffffffff"));
            }
        }

        else if(view.getId()==R.id.suggestions){
            title.setText("Suggestions");
            body_text.setText(Html.fromHtml(getString(R.string.suggestions_str)));
            //change button backgrounds to look like you've switched tabs or something
            suggestions.setBackgroundResource(R.drawable.current_level_back);
            if(color=="day"){
                suggestions.setTextColor(Color.parseColor("#ffffffff"));
            }
        }
    }
}
