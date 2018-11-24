package com.creation.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by elfho on 27/11/2017.
 */
//set up class in app activity
public class Level2Activity extends Activity implements OnClickListener{
    //define global variables (mainly widgets from xml)
    private TextView endorsement, aCredits, mCredits, eCredits, ach, mer, exc, eCredits2, mCredits2, nullCredits, eCredits3, nullCredits2, e, m, noCredits;
    private Button level1Btn, level3Btn;
    DatabaseHelper myDb;
    private ListView lv;
    private LinearLayout mEndorseBar;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private FloatingActionButton fab;
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //get saved shared preferences
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        //set theme before starting activity
        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                setTheme(R.style.DayTheme);
            }
        }
        //set layout from xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //get instance of database class
        myDb = new DatabaseHelper(this);

        //set up buttons
        level1Btn = (Button)findViewById(R.id.Level1);
        level3Btn = (Button)findViewById(R.id.Level3);
        level1Btn.setOnClickListener(this);
        level3Btn.setOnClickListener(this);

        //set up remaining widgets
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
        fab = (FloatingActionButton)findViewById(R.id.add_subject);

        //once activity widgets are defined, set image to appropriate colour for day/night theme
        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                fab.setImageResource(R.drawable.fab_plus_day);
            }
        }

        //set up overall credit display
        getCreditInfo();

        //get list of subjects at level 2 from database
        ArrayList<String> mArrayList = myDb.getSubjects(2);

        //set up array adapter to populate listview with each subject
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
                i.putExtra("level", 2);
                startActivity(i);
            }
        });
    }

    public void onClick(View view) {

        //if level 1 tab clicked, start level 1 activity
        if(view.getId()==R.id.Level1) {
            Intent i = new Intent(Level2Activity.this, Level1Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //if activity already in stack, bring to top. Else, create new activity
            startActivity(i);
            overridePendingTransition(0, 0); //no transition
        }
        //if level 3 tab clicked, start level 3 activity
        else if(view.getId()==R.id.Level3) {
            Intent i = new Intent(Level2Activity.this, Level3Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //if activity already in stack, bring to top. Else, create new activity. Prevents long backstack.
            startActivity(i);
            overridePendingTransition(0, 0); //no transition
        }
        else if(view.getId()==R.id.add_subject) {
            Intent playIntent = new Intent(this, NewSubjectActivity.class);

            //pass level value to next activity
            playIntent.putExtra("level", 2);

            //start activity to add new subject to database
            this.startActivity(playIntent);
        }
    }
    public void onResultsClick(View view) {
        //if any widget in results section clicked, display ResultsActivity
        Intent i = new Intent(Level2Activity.this, ResultsActivity.class);
        i.putExtra("level", 2);     //pass in level
        startActivity(i);
    }

    public void getCreditInfo() {
        //set up instance of database class
        myDb = new DatabaseHelper(this);
        //get list of standards achieved with excellence
        ArrayList<String> excellenceArrayList = myDb.getCourseEndorsementCredits("E", "2");
        //list of standards achieved with merit
        ArrayList<String> meritArrayList = myDb.getCourseEndorsementCredits("M", "2");
        //list of standards achieved with achieved
        ArrayList<String> achievedArrayList = myDb.getCourseEndorsementCredits("A", "2");

        //get total number of credits achieved at each level (sum of each item in list)
        int excellenceCredits = getCreditsForEndorsement(excellenceArrayList);
        int meritCredits = getCreditsForEndorsement(meritArrayList);
        int achievedCredits = getCreditsForEndorsement(achievedArrayList);

        //set textviews for each credit type to display sum (unless sum is 0, in which case textviews remain invisible)
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

        //if any credits have been achieved, set up results bars with appropriate weights
        if(achievedCredits + meritCredits + excellenceCredits != 0) {

            int aWeight = getAPercentCredits(achievedCredits, meritCredits, excellenceCredits);     //returns number of achieved credits as a percent of total
            int mWeight = getMPercentCredits(achievedCredits, meritCredits, excellenceCredits);     //returns number of merit credits as a percent of total
            int eWeight = getEPercentCredits(achievedCredits, meritCredits, excellenceCredits);     //returns number of excellence credits as a percent of total

            //sets the width of each bar (achieved, merit, excellence) to be proportional to credit percentage
            setBarGraphWeights(aWeight, mWeight, eWeight);

            //if user not endorsed with excellence
            if (excellenceCredits < 50) {
                //display progress towards excellence endorsement
                setEEndorsementBars(excellenceCredits);
                //if user is merit endorsed display merit endorsement
                if (meritCredits + excellenceCredits >= 50) {
                    //set endorsement to m
                    nullCredits.setVisibility(View.GONE);
                    eCredits2.setVisibility(View.GONE);
                    m.setVisibility(View.GONE);
                    mCredits2.setText("MERIT ENDORSED");
                //if not merit endorsed, also display progress to merit endorsement
                } else {
                    setMEndorsementBars(excellenceCredits, meritCredits);
                }
            }
            //if user is endorsed with excellence
            else {
                //make merit endorsement bar invisible
                mEndorseBar.setVisibility(View.GONE);
                //display excellence endorsement
                nullCredits2.setVisibility(View.GONE);
                e.setVisibility(View.GONE);
                eCredits3.setText("EXCELLENCE ENDORSED");
            }
        }
        //if the user has achieved no credits (called so that no divide by 0 errors occur when calculating weights)
        else {
            //set widths of irrelevant sections to 0
            setBarGraphWeights(0, 0, 0);

            //set up bar to display "no credits yet", spanning whole screen
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);
            noCredits.setLayoutParams(param);
            noCredits.setText("No Credits Yet");

            //set endorsement bars to show no progress towards endorsement
            setMEndorsementBars(excellenceCredits, meritCredits);
            setEEndorsementBars(excellenceCredits);

        }
    }

    //get total number of credits achieved at a given level
    public int getCreditsForEndorsement(ArrayList<String> credits) {
        int total = 0;
        //loop through each item in list, add together
        for(String credit: credits) {
            int creditInt = Integer.parseInt(credit);
            total += creditInt;
        }
        //return sum of list
        return total;
    }

    //return number of excellence credits as a percentage of total credits
    public int getEPercentCredits(int a, int m, int e) {
        int total = a + m + e;
        int percent = (e * 100) / total;

        return percent;
    }

    //return number of merit credits as a percentage of total credits
    public int getMPercentCredits(int a, int m, int e) {
        int total = a + m + e;
        int percent = (m * 100) / total;

        return percent;
    }

    //return number of achieved credits as a percentage of total credits
    public int getAPercentCredits(int a, int m, int e) {
        int total = a + m + e;
        int percent = (a * 100) / total;

        return percent;
    }

    //set up merit endorsement bar
    public void setMEndorsementBars(int eCredits, int mCredits) {
        //find percent of total endorsement credits achieved at merit and excellence
        int eCredforMPercent = eCredits * 100 / 50;
        int mCredforMPercent = mCredits * 100 / 50;
        int nullCredforMPercent = (50 - eCredits - mCredits) * 100 / 50;

        //set up parameters for sections of bar containing merit, excellence and no credits

        //set width of excellence bar
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                eCredforMPercent);
        eCredits2.setLayoutParams(param);

        //set width of merit bar
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                mCredforMPercent);
        mCredits2.setLayoutParams(param2);

        //set width of no credit bar
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                nullCredforMPercent);
        nullCredits.setLayoutParams(param3);
    }

    //set up excellence endorsement bar
    public void setEEndorsementBars(int eCredits) {
        //find percentage of way to endorsement is achieved
        int eCredforEPercent = eCredits * 100 / 50;
        //find how much still to achieve
        int nullCredforEPercent = (50 - eCredits) * 100 / 50;

        //set appropriate width of progress bar for excellence credits
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                eCredforEPercent);
        eCredits3.setLayoutParams(param);

        //set appropriate width of progress bar for not yet achieved credits
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                nullCredforEPercent);
        nullCredits2.setLayoutParams(param2);
    }

    //set the width of the bars in the total credits section
    public void setBarGraphWeights(int a, int m, int e) { //passed in are achieved, merit and excellence credits as a percentage of total
        //set width of achieved section
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                a);
        aCredits.setLayoutParams(param);
        ach.setLayoutParams(param);

        //set width of merit section
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                m);
        mCredits.setLayoutParams(param2);
        mer.setLayoutParams(param2);

        //set width of excellence section
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                e);
        eCredits.setLayoutParams(param3);
        exc.setLayoutParams(param3);
    }
}