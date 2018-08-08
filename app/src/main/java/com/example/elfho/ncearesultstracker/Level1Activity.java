package com.example.elfho.ncearesultstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by elfho on 27/11/2017.
 */

public class Level1Activity extends Activity implements OnClickListener {

    //define variables
    private TextView aCredits, mCredits, eCredits, ach, mer, exc, eCredits2, mCredits2, nullCredits, eCredits3, nullCredits2, e, m, noCredits;
    private Button level2Btn, level3Btn;
    DatabaseHelper myDb;
    private ListView lv;
    private LinearLayout mEndorseBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);

        //define class name to call database functions from
        myDb = new DatabaseHelper(this);

        //set up buttons
        level2Btn = (Button)findViewById(R.id.Level2);
        level3Btn = (Button)findViewById(R.id.Level3);
        level2Btn.setOnClickListener(this);
        level3Btn.setOnClickListener(this);

        //find objects form xml
        lv = (ListView)findViewById(R.id.subjectList);
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


        //display progress towards endorsement
        getCreditInfo();


        //display scrolling list of subjects
        ArrayList<String> mArrayList = myDb.getSubjects(1);

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
                i.putExtra("level", 1);
                startActivity(i);
            }
        });
    }

    //listen for item click
    public void onClick(View view) {

        if(view.getId()==R.id.Level2) {
            Intent i = new Intent(Level1Activity.this, Level2Activity.class);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
        else if(view.getId()==R.id.Level3) {
            Intent i = new Intent(Level1Activity.this, Level3Activity.class);
            startActivity(i);
            overridePendingTransition(0, 0);
        }
        else if(view.getId()==R.id.newSubject||view.getId()==R.id.add_subject) {
            Intent i = new Intent(Level1Activity.this, NewSubjectActivity.class);
            //pass info to next activity
            i.putExtra("level", 1);
            startActivity(i);
        }
        else if(view.getId()==R.id.help){
            Intent i = new Intent(Level1Activity.this, HelpActivity.class);
            i.putExtra("level", "1");
            startActivity(i);
            overridePendingTransition(0,0);
        }
    }

    //separate onClick function for objects in results class
    public void onResultsClick(View view) {
        Intent i = new Intent(Level1Activity.this, ResultsActivity.class);
        i.putExtra("level", 1);
        startActivity(i);
    }

    //get info about credits, display endorsement progress
    public void getCreditInfo() {

        //call all level 1 credits from the database
        myDb = new DatabaseHelper(this);
        ArrayList<String> excellenceArrayList = myDb.getCourseEndorsementCredits("E", "1");
        ArrayList<String> meritArrayList = myDb.getCourseEndorsementCredits("M", "1");
        ArrayList<String> achievedArrayList = myDb.getCourseEndorsementCredits("A", "1");

        //sum credits from arraylists into ints
        int excellenceCredits = getCreditsForEndorsement(excellenceArrayList);
        int meritCredits = getCreditsForEndorsement(meritArrayList);
        int achievedCredits = getCreditsForEndorsement(achievedArrayList);


        //if non-zero number of credits at each grade, display that grade on home-screen
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

        //if some credits have been achieved, set progress bars accordingly
        if(achievedCredits + meritCredits + excellenceCredits != 0) {

            //calculate weights based on percentage of total
            int aWeight = getAPercentCredits(achievedCredits, meritCredits, excellenceCredits);
            int mWeight = getMPercentCredits(achievedCredits, meritCredits, excellenceCredits);
            int eWeight = getEPercentCredits(achievedCredits, meritCredits, excellenceCredits);

            //set weight of linearlayouts based on calculated percentages
            setBarGraphWeights(aWeight, mWeight, eWeight);

            //if not excellence endorsed, set up e endorsement progress bar
            if (excellenceCredits < 50) {
                setEEndorsementBars(excellenceCredits);
                if (meritCredits + excellenceCredits >= 50) {
                    //set endorsement to m
                    setMEndorsementBars(0, 50);
                    m.setVisibility(View.GONE);
                    mCredits2.setText("MERIT ENDORSED");

                } else {
                    //if not merit endorsed, set up m endorsement progress bar
                    setMEndorsementBars(excellenceCredits, meritCredits);
                }
            }
            else {
                //if E endorsed:
                //disappear m bar
                mEndorseBar.setVisibility(View.GONE);
                //set endorsement to E
                nullCredits2.setVisibility(View.GONE);
                e.setVisibility(View.GONE);
                eCredits3.setText("EXCELLENCE ENDORSED");
            }
        }
        else {
            //no credits have been earned yet

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

    //sum items from arraylist
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


    //set progress towards merit endorsement in linearlayout weights
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

    //set progress towards excellence endorsement in linearlayout weights
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

    //set overal credit proportions in linearlayout weight of main progress bar
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
    public void onBackPressed() {
        //do absolutely nothing MWAHAHA
    }
}