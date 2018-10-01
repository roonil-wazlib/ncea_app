package com.creation.elfho.ncearesultstracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by elfho on 27/11/2017.
 */

public class Level3Activity extends AppCompatActivity implements OnClickListener {
    private TextView aCredits, mCredits, eCredits, ach, mer, exc, eCredits2, mCredits2, nullCredits, eCredits3, nullCredits2, e, m, noCredits;
    private Button level1Btn, level2Btn;
    DatabaseHelper myDb;
    private ListView lv;
    private LinearLayout mEndorseBar;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private FloatingActionButton fab;

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
        setContentView(R.layout.activity_level3);

        myDb = new DatabaseHelper(this);

        //set up buttons
        level1Btn = (Button)findViewById(R.id.Level1);
        level2Btn = (Button)findViewById(R.id.Level2);
        level1Btn.setOnClickListener(this);
        level2Btn.setOnClickListener(this);

        lv = (ListView)findViewById(R.id.subjectList3);
        aCredits = (TextView)findViewById(R.id.aCredits);
        mCredits = (TextView)findViewById(R.id.mCredits);
        eCredits = (TextView)findViewById(R.id.eCredits);
        ach = (TextView)findViewById(R.id.ach);
        mer = (TextView)findViewById(R.id.mer);
        exc = (TextView)findViewById(R.id.exc);
        eCredits2 = (TextView)findViewById(R.id.eCredits2);
        mCredits2 = (TextView)findViewById(R.id.mCredits2);
        nullCredits = (TextView)findViewById(R.id.nullCredits);
        eCredits3 = (TextView)findViewById(R.id.eCredits3);
        nullCredits2 = (TextView)findViewById(R.id.nullCredits2);
        e = (TextView)findViewById(R.id.e);
        m = (TextView)findViewById(R.id.m);
        noCredits = (TextView)findViewById(R.id.noCredits);
        mEndorseBar = (LinearLayout)findViewById(R.id.mEndorseBar);
        fab = (FloatingActionButton)findViewById(R.id.add_subject);

        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                fab.setImageResource(R.drawable.fab_plus_day);
            }
        }

        getCreditInfo();

        ArrayList<String> mArrayList = myDb.getSubjects(3);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.subject_list_item, mArrayList);
        lv.setAdapter(arrayAdapter);

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String subject = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), SubjectActivity.class);
                // sending data to new activity
                i.putExtra("subject", subject);
                i.putExtra("level", 3);
                startActivity(i);
            }
        });
    }

    public void onClick(View view) {

        if(view.getId()==R.id.Level1) {
            Intent i = new Intent(Level3Activity.this, Level1Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
        else if(view.getId()==R.id.Level2) {
            Intent i = new Intent(Level3Activity.this, Level2Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
        else if(view.getId()==R.id.add_subject) {
            Intent playIntent = new Intent(this, NewSubjectActivity.class);

            //pass info to next activity
            playIntent.putExtra("level", 3);
            this.startActivity(playIntent);
        }
    }
    public void onResultsClick(View view) {
        Intent i = new Intent(Level3Activity.this, ResultsActivity.class);
        i.putExtra("level", 3);
        startActivity(i);
    }
    public void getCreditInfo() {
        myDb = new DatabaseHelper(this);
        ArrayList<String> excellenceArrayList = myDb.getCourseEndorsementCredits("E", "3");
        ArrayList<String> meritArrayList = myDb.getCourseEndorsementCredits("M", "3");
        ArrayList<String> achievedArrayList = myDb.getCourseEndorsementCredits("A", "3");

        int excellenceCredits = getCreditsForEndorsement(excellenceArrayList);
        int meritCredits = getCreditsForEndorsement(meritArrayList);
        int achievedCredits = getCreditsForEndorsement(achievedArrayList);

        if(excellenceCredits != 0) {
            String excellenceCreditsStr = Integer.toString(excellenceCredits);
            eCredits.setText(excellenceCreditsStr);
            exc.setText("E");
        }
        if(meritCredits != 0) {
            String meritCreditsStr = Integer.toString(meritCredits);
            mCredits.setText(meritCreditsStr);
            mer.setText("M");
        }
        if(achievedCredits != 0) {
            String achievedCreditsStr = Integer.toString(achievedCredits);
            aCredits.setText(achievedCreditsStr);
            ach.setText("A");
        }
        if(achievedCredits + meritCredits + excellenceCredits != 0) {
            int aWeight = getAPercentCredits(achievedCredits, meritCredits, excellenceCredits);
            int mWeight = getMPercentCredits(achievedCredits, meritCredits, excellenceCredits);
            int eWeight = getEPercentCredits(achievedCredits, meritCredits, excellenceCredits);

            setBarGraphWeights(aWeight, mWeight, eWeight);

            if (excellenceCredits < 50) {
                setEEndorsementBars(excellenceCredits);
                if (meritCredits + excellenceCredits >= 50) {
                    //set endorsement to m
                    nullCredits.setVisibility(View.GONE);
                    eCredits2.setVisibility(View.GONE);
                    m.setVisibility(View.GONE);
                    mCredits2.setText("MERIT ENDORSED");
                } else {
                    setMEndorsementBars(excellenceCredits, meritCredits);
                }
            }
            else {
                //disappear m bar
                mEndorseBar.setVisibility(View.GONE);
                //set endorsement to E
                nullCredits2.setVisibility(View.GONE);
                e.setVisibility(View.GONE);
                eCredits3.setText("EXCELLENCE ENDORSED");
            }
        }
        else {
            //disappear everything irrelevant
            setBarGraphWeights(0, 0, 0);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);
            noCredits.setLayoutParams(param);
            noCredits.setText("No Credits Yet");

            setMEndorsementBars(excellenceCredits, meritCredits);
            setEEndorsementBars(excellenceCredits);

        }
    }

    public int getCreditsForEndorsement(ArrayList<String> credits) {
        int total = 0;
        for(String credit: credits) {
            int creditInt = Integer.parseInt(credit);
            total += creditInt;
        }
        return total;
    }

    public int getEPercentCredits(int a, int m, int e) {
        int total = a + m + e;
        int percent = (e * 100) / total;

        return percent;
    }

    public int getMPercentCredits(int a, int m, int e) {
        int total = a + m + e;
        int percent = (m * 100) / total;

        return percent;
    }

    public int getAPercentCredits(int a, int m, int e) {
        int total = a + m + e;
        int percent = (a * 100) / total;

        return percent;
    }

    public void setMEndorsementBars(int eCredits, int mCredits) {
        int eCredforMPercent = eCredits * 100 / 50;
        int mCredforMPercent = mCredits * 100 / 50;
        int nullCredforMPercent = (50 - eCredits - mCredits) * 100 / 50;

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                eCredforMPercent);
        eCredits2.setLayoutParams(param);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                mCredforMPercent);
        mCredits2.setLayoutParams(param2);

        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                nullCredforMPercent);
        nullCredits.setLayoutParams(param3);
    }

    public void setEEndorsementBars(int eCredits) {
        int eCredforEPercent = eCredits * 100 / 50;
        int nullCredforEPercent = (50 - eCredits) * 100 / 50;

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                eCredforEPercent);
        eCredits3.setLayoutParams(param);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                nullCredforEPercent);
        nullCredits2.setLayoutParams(param2);
    }

    public void setBarGraphWeights(int a, int m, int e) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                a);
        aCredits.setLayoutParams(param);
        ach.setLayoutParams(param);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                m);
        mCredits.setLayoutParams(param2);
        mer.setLayoutParams(param2);

        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                e);
        eCredits.setLayoutParams(param3);
        exc.setLayoutParams(param3);
    }
}