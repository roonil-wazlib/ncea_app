package com.creation.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class StandardsOverviewActivity extends Activity {

    DatabaseHelper myDb;
    TableLayout table;

    SharedPreferences sharedpreferences;
    private ImageView back;
    public static final String mypreference = "mypref";
    private String colour = "night";

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
        setContentView(R.layout.activity_standards_overview);

        TableLayout table = (TableLayout) findViewById(R.id.table);
        //find back to menu arrow to set image resource
        back = (ImageView)findViewById(R.id.back);

        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                back.setImageResource(R.drawable.back_arrow_day);
                colour = "day";
            }
        }

        myDb = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        final int level = extras.getInt("level");

        ArrayList<String> StandardsList = myDb.getStandardNames(level);
        ArrayList<String> GoalsList = myDb.getStandardGoal(level);
        ArrayList<String> MocksList = myDb.getStandardMock(level);
        ArrayList<String> FinalsList = myDb.getStandardFinal(level);

        for(int i=0;i<StandardsList.size();i++)
        {
            TableRow row=new TableRow(this);
            String name = StandardsList.get(i);
            String goal = GoalsList.get(i);
            if(goal.contains("+")){
                goal = "";
            }
            String mock = MocksList.get(i);
            if(mock.contains("+")){
                mock = "";
            }
            String finalstr = FinalsList.get(i);
            if(finalstr.contains("+")){
                finalstr = "";
            }

            TextView tvName=new TextView(this);
            tvName.setText(""+name);

            TextView tvGoal=new TextView(this);
            tvGoal.setText(""+goal);

            TextView tvMock=new TextView(this);
            tvMock.setText(""+mock);

            TextView tvFinal=new TextView(this);
            tvFinal.setText(""+finalstr);

            if(colour=="day"){
                tvName.setTextColor(Color.parseColor("#000000"));
                tvGoal.setTextColor(Color.parseColor("#000000"));
                tvMock.setTextColor(Color.parseColor("#000000"));
                tvFinal.setTextColor(Color.parseColor("#000000"));
            }else {
                tvName.setTextColor(Color.parseColor("#ffffff"));
                tvGoal.setTextColor(Color.parseColor("#ffffff"));
                tvMock.setTextColor(Color.parseColor("#ffffff"));
                tvFinal.setTextColor(Color.parseColor("#ffffff"));
            }
            row.addView(tvName);
            row.addView(tvGoal);
            row.addView(tvMock);
            row.addView(tvFinal);
            table.addView(row);
        }

    }

    public void onClick(View view) {
        onBackPressed();
    }
}
