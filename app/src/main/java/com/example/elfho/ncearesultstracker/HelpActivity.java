package com.example.elfho.ncearesultstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by elfho on 22/07/2018.
 */

public class HelpActivity extends AppCompatActivity {
    private TextView title, body_text;
    private Button app_help, ncea_info, suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);

        title=(TextView)findViewById(R.id.title);
        body_text=(TextView)findViewById(R.id.body_text);

        app_help=(Button)findViewById(R.id.app_help);
        ncea_info=(Button)findViewById(R.id.ncea_info);
        suggestions=(Button)findViewById(R.id.suggestions);

        body_text.setText(Html.fromHtml(getString(R.string.app_help_str)));
    }

    public void onClick(View view){
        if(view.getId()==R.id.home) {
            Bundle extras = getIntent().getExtras();
            String level = extras.getString("level");
            if(level.contains("3")){
                Intent i = new Intent(HelpActivity.this, Level3Activity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
            else if(level.contains("2")){
                Intent i = new Intent(HelpActivity.this, Level2Activity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
            else{
                Intent i = new Intent(HelpActivity.this, Level1Activity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        }

        else if(view.getId()==R.id.app_help){
            title.setText("App Help");
            body_text.setText(Html.fromHtml(getString(R.string.app_help_str)));
            //change button backgrounds to look like you've switched tabs or something
            app_help.setBackgroundResource(R.drawable.current_level_back);
            ncea_info.setBackgroundResource(android.R.drawable.btn_default);
            suggestions.setBackgroundResource(android.R.drawable.btn_default);
        }

        else if(view.getId()==R.id.ncea_info){
            title.setText("NCEA Info");
            body_text.setText(Html.fromHtml(getString(R.string.ncea_info_str)));
            //change button backgrounds to look like you've switched tabs or something
            ncea_info.setBackgroundResource(R.drawable.current_level_back);
            app_help.setBackgroundResource(android.R.drawable.btn_default);
            suggestions.setBackgroundResource(android.R.drawable.btn_default);
        }

        else if(view.getId()==R.id.suggestions){
            title.setText("Suggestions");
            body_text.setText(Html.fromHtml(getString(R.string.suggestions_str)));
            //change button backgrounds to look like you've switched tabs or something
            suggestions.setBackgroundResource(R.drawable.current_level_back);
            ncea_info.setBackgroundResource(android.R.drawable.btn_default);
            app_help.setBackgroundResource(android.R.drawable.btn_default);
        }
    }
}
