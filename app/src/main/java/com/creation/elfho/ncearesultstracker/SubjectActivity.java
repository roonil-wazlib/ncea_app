package com.creation.elfho.ncearesultstracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by elfho on 26/11/2017.
 */

public class SubjectActivity extends Activity {

    private TextView subjecttxt, aCredits, mCredits, eCredits, ach, mer, exc, eCredits2, mCredits2, nullCredits, eCredits3, nullCredits2, e, m, noCredits;
    DatabaseHelper myDb;
    private ImageView deleteBtn, backBtn;
    String [] deleteOptions = {"Yes", "No"};
    private ListView lv, gradeView;
    private LinearLayout mEndorseBar, endorseInfo, meritEndorseInfo, excellenceEndorseInfo;
    private FloatingActionButton fab;
    private CheckBox merit_int, merit_ext, excellence_int, excellence_ext;
    View clickSource;
    View touchSource;
    int meritVisible = 0;
    int excellenceVisible = 0;
    int offset = 0;

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
        setContentView(R.layout.activity_subject);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        myDb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        final String subject = extras.getString("subject");
        final int level = extras.getInt("level");
        final String level_str = Integer.toString(level);
        final int subjectId = myDb.getSubjectId(level_str, subject);


        lv = (ListView)findViewById(R.id.standardList);
        gradeView = (ListView)findViewById(R.id.gradeList);

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
        fab = (FloatingActionButton)findViewById(R.id.add_standard);
        endorseInfo = (LinearLayout)findViewById(R.id.endorseInfo);
        excellenceEndorseInfo = (LinearLayout)findViewById(R.id.excellenceEndorseInfo);
        meritEndorseInfo = (LinearLayout)findViewById(R.id.meritEndorseInfo);
        merit_int = (CheckBox)findViewById(R.id.merit_int);
        merit_ext = (CheckBox)findViewById(R.id.merit_ext);
        excellence_int = (CheckBox)findViewById(R.id.excellence_int);
        excellence_ext = (CheckBox)findViewById(R.id.excellence_ext);

        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                fab.setImageResource(R.drawable.fab_plus_day);
            }
        }
        setEndorseInfoVisibility();
        ArrayList<String> mArrayList = myDb.getStandards(subjectId);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.subject_list_item, mArrayList);
        lv.setAdapter(arrayAdapter);

        View footerView =  ((LayoutInflater)SubjectActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.subject_list_footer, null, false);
        lv.addFooterView(footerView);

        ArrayList<String> gradeList = myDb.getGrades(subjectId);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.subject_list_item, gradeList);
        gradeView.setAdapter(arrayAdapter2);

        View footerView2 =  ((LayoutInflater)SubjectActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.subject_list_footer, null, false);
        gradeView.addFooterView(footerView2);

        //make listviews scroll simultaneously
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    gradeView.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });

        gradeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    lv.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });

        //make listview items respond to clicks and open relevant activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // selected item
                String standard = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(SubjectActivity.this, StandardInfoActivity.class);
                // sending data to new activity
                i.putExtra("standard", standard);
                i.putExtra("subject", subjectId);
                i.putExtra("subjectName", subject);
                i.putExtra("level", level);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

        gradeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent2, View view,
                                    int position, long id2) {
                // Launching new Activity on selecting single List Item
                Intent j = new Intent(SubjectActivity.this, AddGradeActivity.class);
                // sending data to new activity
                j.putExtra("subjectId", subjectId);
                j.putExtra("position", position);
                j.putExtra("level", level);
                j.putExtra("subject", subject);
                startActivity(j);
            }
        });

        //fix inconsistencies in listview scrolling motion
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                    gradeView.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        subjecttxt = (TextView)findViewById(R.id.subjectTxt);
        deleteBtn = (ImageView)findViewById(R.id.deleteSubject);
        backBtn = (ImageView)findViewById(R.id.back);


        if (sharedpreferences.contains("color")){
            if (sharedpreferences.getString("color", "").contains("day")) {
                backBtn.setImageResource(R.drawable.back_arrow_day);
                deleteBtn.setImageResource(R.drawable.rubbish_bin_day);
            }
        }

        subjecttxt.setText(subject);
        getCreditInfo();

    }

    public interface ItemClickAdapterListener {
        void itemClick(View v, int position);
    }

    public void onClick(View view) {
        if(view.getId()==R.id.deleteSubject) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete subject? This change can not be undone.")
                    .setSingleChoiceItems(deleteOptions, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                            String selectedOption = deleteOptions[selectedPosition];

                            //respond to delete if clicked
                            if(selectedOption == "Yes") {
                                Bundle extras = getIntent().getExtras();
                                String subject = extras.getString("subject");
                                int level = extras.getInt("level", -1);
                                String level_str = Integer.toString(level);
                                final int subjectId = myDb.getSubjectId(level_str, subject);
                                String subjectId_str = Integer.toString(subjectId);
                                myDb.deleteSubject(subject, level_str);
                                int rows_deleted = myDb.deleteSubjectStandards(subjectId_str);
                                String rows_deleted_str = Integer.toString(rows_deleted);
                                Toast.makeText(SubjectActivity.this, "Subject deleted", Toast.LENGTH_LONG).show();

                                if(level==1){
                                    Intent i = new Intent(SubjectActivity.this, Level1Activity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                                else if(level==2){
                                    Intent i = new Intent(SubjectActivity.this, Level2Activity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Intent i = new Intent(SubjectActivity.this, Level3Activity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    });
            AlertDialog ad = builder.create();
            ad.show();
        }

        else if(view.getId()==R.id.back) {
            Bundle extras = getIntent().getExtras();
            String subject = extras.getString("subject");
            int level = extras.getInt("level", -1);
            if(level==1){
                Intent i = new Intent(SubjectActivity.this, Level1Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            else if(level==2){
                Intent i = new Intent(SubjectActivity.this, Level2Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
            else {
                Intent i = new Intent(SubjectActivity.this, Level3Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        }

        else if(view.getId()==R.id.add_standard) {
            Bundle extras = getIntent().getExtras();
            String subject = extras.getString("subject");
            int level = extras.getInt("level", -1);
            String level_str = Integer.toString(level);

            int subjectId = myDb.getSubjectId(level_str, subject);

            Intent i = new Intent(SubjectActivity.this, NewStandardActivity.class);
            i.putExtra("id", subjectId);
            i.putExtra("subject", subject);
            i.putExtra("level", level);
            startActivity(i);
            overridePendingTransition(0, 0);
            finish();
        }

    }

    public void getCreditInfo() {
        myDb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        String subject = extras.getString("subject");
        int level = extras.getInt("level");
        String level_str = Integer.toString(level);

        int subjectId = myDb.getSubjectId(level_str, subject);
        String subjectIdStr = Integer.toString(subjectId);


        //get credits to calculate subject endorsement
        ArrayList<String> excellenceExternalArrayList = myDb.getSubjectEndorsementCredits("E", level_str, subjectIdStr, "1"); //add parameter for exam type (internal / external)
        ArrayList<String> excellenceInternalArrayList = myDb.getSubjectEndorsementCredits("E", level_str, subjectIdStr, "0");
        ArrayList<String> meritExternalArrayList = myDb.getSubjectEndorsementCredits("M", level_str, subjectIdStr, "1"); //as above
        ArrayList<String> meritInternalArrayList = myDb.getSubjectEndorsementCredits("M", level_str, subjectIdStr, "0");
        ArrayList<String> achievedExternalArrayList = myDb.getSubjectEndorsementCredits("A", level_str, subjectIdStr, "1"); //
        ArrayList<String> achievedInternalArrayList = myDb.getSubjectEndorsementCredits("A", level_str, subjectIdStr, "0");


        int excellenceExternalCredits = getCreditsForEndorsement(excellenceExternalArrayList);
        int excellenceInternalCredits = getCreditsForEndorsement(excellenceInternalArrayList);
        int meritExternalCredits = getCreditsForEndorsement(meritExternalArrayList);
        int meritInternalCredits = getCreditsForEndorsement(meritInternalArrayList);
        //set checkboxes
        if(excellenceExternalCredits >= 3){
            //check excellence_ext
            excellence_ext.setChecked(true);
        }
        if(excellenceInternalCredits >= 3){
            //check excellence_int
            excellence_int.setChecked(true);
        }
        if(excellenceExternalCredits + meritExternalCredits >= 3){
            merit_ext.setChecked(true);
        }
        if(excellenceInternalCredits + meritInternalCredits >= 3){
            merit_int.setChecked(true);
        }
        int totalExcellenceCredits = excellenceExternalCredits + excellenceInternalCredits;
        int totalMeritCredits = meritExternalCredits + meritInternalCredits;
        int totalAchievedCredits = getCreditsForEndorsement(achievedExternalArrayList) + getCreditsForEndorsement(achievedInternalArrayList);

        if(totalExcellenceCredits != 0) {
            String excellenceCreditsStr = Integer.toString(totalExcellenceCredits);
            eCredits.setText(excellenceCreditsStr);
            exc.setText("E");
        }
        if(totalMeritCredits != 0) {
            String meritCreditsStr = Integer.toString(totalMeritCredits);
            mCredits.setText(meritCreditsStr);
            mer.setText("M");
        }
        if(totalAchievedCredits != 0) {
            String achievedCreditsStr = Integer.toString(totalAchievedCredits);
            aCredits.setText(achievedCreditsStr);
            ach.setText("A");
        }
        if(totalAchievedCredits + totalMeritCredits + totalExcellenceCredits != 0) {
            int aWeight = getAPercentCredits(totalAchievedCredits, totalMeritCredits, totalExcellenceCredits);
            int mWeight = getMPercentCredits(totalAchievedCredits, totalMeritCredits, totalExcellenceCredits);
            int eWeight = getEPercentCredits(totalAchievedCredits, totalMeritCredits, totalExcellenceCredits);

            setBarGraphWeights(aWeight, mWeight, eWeight);


            setEEndorsementBars(totalExcellenceCredits);
            if(excellenceExternalCredits >= 3 && excellenceInternalCredits >= 3 && totalExcellenceCredits >= 14){
                //disappear m bar
                mEndorseBar.setVisibility(View.GONE);
                //E ENDORSED
                //set endorsement to E
                nullCredits2.setVisibility(View.GONE);
                e.setVisibility(View.GONE);
                eCredits3.setText("EXCELLENCE ENDORSED");
            }
            else if (totalMeritCredits + totalExcellenceCredits >= 14 && (meritExternalCredits + excellenceExternalCredits) >= 3 && (meritInternalCredits + excellenceInternalCredits) >= 3) {
                //M ENDORSED
                //set endorsement to m
                nullCredits.setVisibility(View.GONE);
                eCredits2.setVisibility(View.GONE);
                m.setVisibility(View.GONE);
                mCredits2.setText("MERIT ENDORSED");
            }
            else {
                //NOT ENDORSED
                setMEndorsementBars(totalExcellenceCredits, totalMeritCredits);
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

            setMEndorsementBars(totalExcellenceCredits, totalMeritCredits);
            setEEndorsementBars(totalExcellenceCredits);

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
        int eCredforMPercent = eCredits * 100 / 14;
        int mCredforMPercent = mCredits * 100 / 14;
        int nullCredforMPercent = (14 - eCredits - mCredits) * 100 / 14;

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
        int eCredforEPercent = eCredits * 100 / 14;
        int nullCredforEPercent = (14 - eCredits) * 100 / 14;

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

    public void setEndorseInfoVisibility(){
        excellenceEndorseInfo.setVisibility(View.GONE);
        meritEndorseInfo.setVisibility(View.GONE);
    }

    public void onMeritClick(View view){
        if(meritVisible==0){
            meritEndorseInfo.setVisibility(View.VISIBLE);
            excellenceEndorseInfo.setVisibility(View.INVISIBLE);
            meritVisible = 1;
        }
        else{
            meritEndorseInfo.setVisibility(View.GONE);
            excellenceEndorseInfo.setVisibility(View.GONE);
            meritVisible = 0;
        }
    }
    public void onExcellenceClick(View view){
        if(excellenceVisible==0){
            excellenceEndorseInfo.setVisibility(View.VISIBLE);
            meritEndorseInfo.setVisibility(View.INVISIBLE);
            excellenceVisible = 1;
        }
        else{
            excellenceEndorseInfo.setVisibility(View.GONE);
            meritEndorseInfo.setVisibility(View.GONE);
            excellenceVisible = 0;
        }
    }
}


