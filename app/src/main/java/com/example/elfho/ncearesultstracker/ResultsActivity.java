package com.example.elfho.ncearesultstracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by elfho on 19/03/2018.
 */

public class ResultsActivity extends Activity {
    private TextView achTxtOverall, merTxtOverall, excTxtOverall, achTxtMock, merTxtMock, excTxtMock, achTxtGoal, merTxtGoal, excTxtGoal, achBarOverall, merBarOverall, excBarOverall, achBarMock, merBarMock, excBarMock, achBarGoal, merBarGoal, excBarGoal, noCreditsOverall, noCreditsMock, noCreditsGoal, title, goalMTxt, goalETxt, goalEforM, goalMforM, goalNullforM, goalEforE, goalNullforE, mockMTxt, mockETxt, mockEforM, mockMforM, mockNullforM, mockEforE, mockNullforE, overallMTxt, overallETxt, overallEforM, overallMforM, overallNullforM, overallEforE, overallNullforE, rankScoreInfo, goalRankScore, mockRankScore, overallRankScore, rankScoreWarning;
    DatabaseHelper myDb;
    private LinearLayout goalMEndorseBar, mockMEndorseBar, overallMEndorseBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //find widgets for text above main progress bars
        achTxtOverall = (TextView)findViewById(R.id.achTxtOverall);
        merTxtOverall = (TextView)findViewById(R.id.merTxtOverall);
        excTxtOverall = (TextView)findViewById(R.id.excTxtOverall);

        achTxtMock = (TextView)findViewById(R.id.achTxtMock);
        merTxtMock = (TextView)findViewById(R.id.merTxtMock);
        excTxtMock = (TextView)findViewById(R.id.excTxtMock);

        achTxtGoal = (TextView)findViewById(R.id.achTxtGoal);
        merTxtGoal = (TextView)findViewById(R.id.merTxtGoal);
        excTxtGoal = (TextView)findViewById(R.id.excTxtGoal);

        //find widgets for textviews in main progress bar
        achBarOverall = (TextView)findViewById(R.id.achBarOverall);
        merBarOverall = (TextView)findViewById(R.id.merBarOverall);
        excBarOverall = (TextView)findViewById(R.id.excBarOverall);

        achBarMock = (TextView)findViewById(R.id.achBarMock);
        merBarMock = (TextView)findViewById(R.id.merBarMock);
        excBarMock = (TextView)findViewById(R.id.excBarMock);

        achBarGoal = (TextView)findViewById(R.id.achBarGoal);
        merBarGoal = (TextView)findViewById(R.id.merBarGoal);
        excBarGoal = (TextView)findViewById(R.id.excBarGoal);

        noCreditsOverall = (TextView)findViewById(R.id.noCreditsOverall);
        noCreditsMock = (TextView)findViewById(R.id.noCreditsMock);
        noCreditsGoal = (TextView)findViewById(R.id.noCreditsGoal);

        //find widgets for goal endorsements
        goalEforE = (TextView)findViewById(R.id.goalEforE);
        goalNullforE = (TextView)findViewById(R.id.goalnullForE);
        goalETxt = (TextView)findViewById(R.id.goalETxt);

        goalEforM = (TextView)findViewById(R.id.goalEforM);
        goalMforM = (TextView)findViewById(R.id.goalMforM);
        goalNullforM = (TextView)findViewById(R.id.goalNullforM);
        goalMTxt = (TextView)findViewById(R.id.goalMTxt);
        goalMEndorseBar = (LinearLayout)findViewById(R.id.goalMEndorseBar);

        //find widgets for mock endorsements
        mockEforE = (TextView)findViewById(R.id.mockEforE);
        mockNullforE = (TextView)findViewById(R.id.mockNullforE);
        mockETxt = (TextView)findViewById(R.id.mockETxt);

        mockEforM = (TextView)findViewById(R.id.mockEforM);
        mockMforM = (TextView)findViewById(R.id.mockMforM);
        mockNullforM = (TextView)findViewById(R.id.mockNullforM);
        mockMTxt = (TextView)findViewById(R.id.mockMTxt);
        mockMEndorseBar = (LinearLayout)findViewById(R.id.mockMEndorse);

        //find widgets for overall endorsements
        overallEforE = (TextView)findViewById(R.id.overallEforE);
        overallNullforE = (TextView)findViewById(R.id.overallNullforE);
        overallETxt = (TextView)findViewById(R.id.overallETxt);

        overallEforM = (TextView)findViewById(R.id.overallEforM);
        overallMforM = (TextView)findViewById(R.id.overallMforM);
        overallNullforM = (TextView)findViewById(R.id.overallNullforM);
        overallMTxt = (TextView)findViewById(R.id.overallMTxt);
        overallMEndorseBar = (LinearLayout)findViewById(R.id.overallMEndorseBar);

        //find widgets for rank score stuff
        rankScoreInfo = (TextView)findViewById(R.id.rankScoreInfo);
        rankScoreWarning = (TextView)findViewById(R.id.rankScoreWarning);
        goalRankScore = (TextView)findViewById(R.id.goalRankScore);
        mockRankScore = (TextView)findViewById(R.id.mockRankScore);
        overallRankScore = (TextView)findViewById(R.id.overallRankScore);


        Bundle extras = getIntent().getExtras();
        int level = extras.getInt("level");
        String level_str = Integer.toString(level);
        String level_txt = "Level " + level_str;
        title = (TextView)findViewById(R.id.title);
        title.setText(level_txt);

        //if displaying non level 3 results, get rid of stuff about rank score
        if(level != 3){
            rankScoreInfo.setVisibility(View.GONE);
            rankScoreWarning.setVisibility(View.GONE);
            goalRankScore.setVisibility(View.GONE);
            mockRankScore.setVisibility(View.GONE);
            overallRankScore.setVisibility(View.GONE);
        }

        displayOverallInfo();
        displayGoalInfo();
        displayMockInfo();
    }

    public void displayOverallInfo(){
        myDb = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        int level = extras.getInt("level");
        String level_str = Integer.toString(level);

        //get overall results
        ArrayList<String> excellenceArrayList = myDb.getCourseEndorsementCredits("E", level_str);
        ArrayList<String> meritArrayList = myDb.getCourseEndorsementCredits("M", level_str);
        ArrayList<String> achievedArrayList = myDb.getCourseEndorsementCredits("A", level_str);

        int eCredits = findCreditTotal(excellenceArrayList);
        int mCredits = findCreditTotal(meritArrayList);
        int aCredits = findCreditTotal(achievedArrayList);

        int totalCredits = eCredits + mCredits + aCredits;

        //if non-zero number of credits at each grade, display that grade on home-screen
        if(eCredits != 0) {
            String excellenceCreditsStr = Integer.toString(eCredits);
            excBarOverall.setText(excellenceCreditsStr);
            excTxtOverall.setText("E");
        }
        if(mCredits != 0) {
            String meritCreditsStr = Integer.toString(mCredits);
            merBarOverall.setText(meritCreditsStr);
            merTxtOverall.setText("M");
        }
        if(aCredits != 0) {
            String achievedCreditsStr = Integer.toString(aCredits);
            achBarOverall.setText(achievedCreditsStr);
            achTxtOverall.setText("A");
        }

        if(totalCredits != 0){
            int ePercent = getPercentage(totalCredits, eCredits);
            int mPercent = getPercentage(totalCredits, mCredits);
            int aPercent = getPercentage(totalCredits, aCredits);

            setBarGraphWeights(aPercent, mPercent, ePercent, achTxtOverall, merTxtOverall, excTxtOverall, achBarOverall, merBarOverall, excBarOverall);
            //also set endorsements here
        }
        else {
            //no credits have been earned yet

            //disappear everything irrelevant
            setBarGraphWeights(0, 0, 0, achTxtOverall, merTxtOverall, excTxtOverall, achBarOverall, merBarOverall, excBarOverall);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);
            noCreditsOverall.setLayoutParams(param);
            noCreditsOverall.setText("No final results entered yet");
        }
        calculateEndorsement(eCredits, mCredits, overallEforM, overallMforM, overallNullforM, overallEforE, overallNullforE, overallMEndorseBar, overallETxt, overallMTxt);

        if(level==3){
            calculateRankScore(0);
        }
    }
    public void displayMockInfo(){
        myDb = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        int level = extras.getInt("level");
        String level_str = Integer.toString(level);

        //get overall results
        ArrayList<String> excellenceMockArrayList = myDb.getMockCredits("E", level_str);
        ArrayList<String> meritMockArrayList = myDb.getMockCredits("M", level_str);
        ArrayList<String> achievedMockArrayList = myDb.getMockCredits("A", level_str);

        ArrayList<String> excellenceOverallArrayList = myDb.getCourseEndorsementCredits("E", level_str);
        ArrayList<String> meritOverallArrayList = myDb.getCourseEndorsementCredits("M", level_str);
        ArrayList<String> achievedOverallArrayList = myDb.getCourseEndorsementCredits("A", level_str);

        int eCredits = findCreditTotal(excellenceMockArrayList) + findCreditTotal(excellenceOverallArrayList);
        int mCredits = findCreditTotal(meritMockArrayList) + findCreditTotal(meritOverallArrayList);
        int aCredits = findCreditTotal(achievedMockArrayList) + findCreditTotal(achievedOverallArrayList);

        int totalCredits = eCredits + mCredits + aCredits;

        //if non-zero number of credits at each grade, display that grade on home-screen
        if(eCredits != 0) {
            String excellenceCreditsStr = Integer.toString(eCredits);
            excBarMock.setText(excellenceCreditsStr);
            excTxtMock.setText("E");
        }
        if(mCredits != 0) {
            String meritCreditsStr = Integer.toString(mCredits);
            merBarMock.setText(meritCreditsStr);
            merTxtMock.setText("M");
        }
        if(aCredits != 0) {
            String achievedCreditsStr = Integer.toString(aCredits);
            achBarMock.setText(achievedCreditsStr);
            achTxtMock.setText("A");
        }

        if(totalCredits != 0){
            int ePercent = getPercentage(totalCredits, eCredits);
            int mPercent = getPercentage(totalCredits, mCredits);
            int aPercent = getPercentage(totalCredits, aCredits);
            setBarGraphWeights(aPercent, mPercent, ePercent, achTxtMock, merTxtMock, excTxtMock, achBarMock, merBarMock, excBarMock);
            //also set endorsements here
        }
        else {
            //no credits have been earned yet

            //disappear everything irrelevant
            setBarGraphWeights(0, 0, 0, achTxtMock, merTxtMock, excTxtMock, achBarMock, merBarMock, excBarMock);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);
            noCreditsMock.setLayoutParams(param);
            noCreditsMock.setText("No mock results entered yet");
        }
        calculateEndorsement(eCredits, mCredits, mockEforM, mockMforM, mockNullforM, mockEforE, mockNullforE, mockMEndorseBar, mockETxt, mockMTxt);
        if(level==3){
            calculateRankScore(1);
        }
    }
    public void displayGoalInfo(){
        myDb = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        int level = extras.getInt("level");
        String level_str = Integer.toString(level);

        //get overall results
        ArrayList<String> excellenceGoalArrayList = myDb.getGoalCredits("E", level_str);
        ArrayList<String> meritGoalArrayList = myDb.getGoalCredits("M", level_str);
        ArrayList<String> achievedGoalArrayList = myDb.getGoalCredits("A", level_str);

        ArrayList<String> excellenceOverallArrayList = myDb.getCourseEndorsementCredits("E", level_str);
        ArrayList<String> meritOverallArrayList = myDb.getCourseEndorsementCredits("M", level_str);
        ArrayList<String> achievedOverallArrayList = myDb.getCourseEndorsementCredits("A", level_str);

        int eCredits = findCreditTotal(excellenceGoalArrayList) + findCreditTotal(excellenceOverallArrayList);
        int mCredits = findCreditTotal(meritGoalArrayList) + findCreditTotal(meritOverallArrayList);
        int aCredits = findCreditTotal(achievedGoalArrayList) + findCreditTotal(achievedOverallArrayList);

        int totalCredits = eCredits + mCredits + aCredits;

        //if non-zero number of credits at each grade, display that grade on home-screen
        if(eCredits != 0) {
            String excellenceCreditsStr = Integer.toString(eCredits);
            excBarGoal.setText(excellenceCreditsStr);
            excTxtGoal.setText("E");
        }
        if(mCredits != 0) {
            String meritCreditsStr = Integer.toString(mCredits);
            merBarGoal.setText(meritCreditsStr);
            merTxtGoal.setText("M");
        }
        if(aCredits != 0) {
            String achievedCreditsStr = Integer.toString(aCredits);
            achBarGoal.setText(achievedCreditsStr);
            achTxtGoal.setText("A");
        }

        if(totalCredits != 0){
            int ePercent = getPercentage(totalCredits, eCredits);
            int mPercent = getPercentage(totalCredits, mCredits);
            int aPercent = getPercentage(totalCredits, aCredits);
            setBarGraphWeights(aPercent, mPercent, ePercent, achTxtGoal, merTxtGoal, excTxtGoal, achBarGoal, merBarGoal, excBarGoal);
            //also set endorsements here
        }
        else {
            //no credits have been earned yet

            //disappear everything irrelevant
            setBarGraphWeights(0, 0, 0, achTxtGoal, merTxtGoal, excTxtGoal, achBarGoal, merBarGoal, excBarGoal);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1);
            noCreditsGoal.setLayoutParams(param);
            noCreditsGoal.setText("No goal results entered yet");
        }

        calculateEndorsement(eCredits, mCredits, goalEforM, goalMforM, goalNullforM, goalEforE, goalNullforE, goalMEndorseBar, goalETxt, goalMTxt);
        if(level==3){
            calculateRankScore(2);
        }
    }

    public int findCreditTotal(ArrayList<String> credits){
        int total = 0;
        for(String credit: credits) {
            int creditInt = Integer.parseInt(credit);
            total += creditInt;
        }
        return total;
    }

    public int getPercentage(int total, int numerator){
        int percent = (numerator * 100) / total;

        return percent;
    }

    //set overal credit proportions in linearlayout weight of main progress bar
    public void setBarGraphWeights(int a, int m, int e, TextView aTxt, TextView mTxt, TextView eTxt, TextView aBar, TextView mBar, TextView eBar) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                a);
        aTxt.setLayoutParams(param);
        aBar.setLayoutParams(param);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                m);
        mTxt.setLayoutParams(param2);
        mBar.setLayoutParams(param2);

        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                e);
        eTxt.setLayoutParams(param3);
        eBar.setLayoutParams(param3);
    }

    public void calculateEndorsement(int eCredits, int mCredits, TextView eBarforM, TextView mBarforM, TextView nullBarforM, TextView eBarforE, TextView nullBarforE, LinearLayout mEndorseBar, TextView eTxt, TextView mTxt){
        //if not excellence endorsed, set up e endorsement progress bar
        if (eCredits < 50) {
            setEEndorsementBars(eCredits, eBarforE, nullBarforE);
            if (mCredits + eCredits >= 50) {
                //set endorsement to m
                setMEndorsementBars(0, 50, eBarforM, mBarforM, nullBarforM);
                mTxt.setVisibility(View.GONE);
                mBarforM.setText("MERIT ENDORSED");

            } else {
                //if not merit endorsed, set up m endorsement progress bar
                setMEndorsementBars(eCredits, mCredits, eBarforM, mBarforM, nullBarforM);
            }
        }
        else {
            //if E endorsed:
            //disappear m bar
            mEndorseBar.setVisibility(View.GONE);
            //set endorsement to E
            nullBarforE.setVisibility(View.GONE);
            eTxt.setVisibility(View.GONE);
            eBarforE.setText("EXCELLENCE ENDORSED");
        }
    }

    public void setEEndorsementBars(int eCredits, TextView eBar, TextView nullBar) {
        int eCredforEPercent = eCredits * 100 / 50;
        int nullCredforEPercent = (50 - eCredits) * 100 / 50;

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                eCredforEPercent);
        eBar.setLayoutParams(param);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                nullCredforEPercent);
        nullBar.setLayoutParams(param2);
    }

    public void setMEndorsementBars(int eCredits, int mCredits, TextView eBar, TextView mBar, TextView nullBar) {
        int eCredforMPercent = eCredits * 100 / 50;
        int mCredforMPercent = mCredits * 100 / 50;
        int nullCredforMPercent = (50 - eCredits - mCredits) * 100 / 50;

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                eCredforMPercent);
        eBar.setLayoutParams(param);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                mCredforMPercent);
        mBar.setLayoutParams(param2);

        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                nullCredforMPercent);
        nullBar.setLayoutParams(param3);
    }

    public void calculateRankScore(int rankScoreType){
        myDb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        int level = extras.getInt("level");
        String level_str = Integer.toString(level);

        //arraylist of subject ids for given ncea level
        ArrayList<String> subjectsArrayList = myDb.getSubjectIds(level_str);

        Map<String, Integer> subjectRankScoreMap = new HashMap<String, Integer>();
        int aCredits = 0;
        int mCredits = 0;
        int eCredits = 0;

        for (int i = 0; i < subjectsArrayList.size(); i++) {
            if(rankScoreType == 0) {
                aCredits = findCreditTotal(myDb.getRankScoreTotalCredits("A", level_str, subjectsArrayList.get(i)));
                mCredits = findCreditTotal(myDb.getRankScoreTotalCredits("M", level_str, subjectsArrayList.get(i)));
                eCredits = findCreditTotal(myDb.getRankScoreTotalCredits("E", level_str, subjectsArrayList.get(i)));
            } else if(rankScoreType == 1){
                aCredits = findCreditTotal(myDb.getRankScoreMockCredits("A", level_str, subjectsArrayList.get(i))) + findCreditTotal(myDb.getTotalSubjectCredits("A", level_str, subjectsArrayList.get(i)));
                mCredits = findCreditTotal(myDb.getRankScoreMockCredits("M", level_str, subjectsArrayList.get(i))) + findCreditTotal(myDb.getTotalSubjectCredits("M", level_str, subjectsArrayList.get(i)));
                eCredits = findCreditTotal(myDb.getRankScoreMockCredits("E", level_str, subjectsArrayList.get(i))) + findCreditTotal(myDb.getTotalSubjectCredits("E", level_str, subjectsArrayList.get(i)));
            } else {
                aCredits = findCreditTotal(myDb.getRankScoreGoalCredits("A", level_str, subjectsArrayList.get(i))) + findCreditTotal(myDb.getTotalSubjectCredits("A", level_str, subjectsArrayList.get(i)));
                mCredits = findCreditTotal(myDb.getRankScoreGoalCredits("M", level_str, subjectsArrayList.get(i))) + findCreditTotal(myDb.getTotalSubjectCredits("M", level_str, subjectsArrayList.get(i)));
                eCredits = findCreditTotal(myDb.getRankScoreGoalCredits("E", level_str, subjectsArrayList.get(i))) + findCreditTotal(myDb.getTotalSubjectCredits("E", level_str, subjectsArrayList.get(i)));
            }
            int totalCredits = aCredits + mCredits + eCredits;
            int subjectRankScore = 0;

            if (totalCredits >= 24) {
                if ((mCredits + eCredits) >= 24) {
                    if (eCredits >= 24) {
                        subjectRankScore = 4 * 24;
                    } else {
                        subjectRankScore = 4 * eCredits + 3 * (24 - eCredits);
                    }
                } else {
                    subjectRankScore = 4 * eCredits + 3 * mCredits + 2 * (24 - eCredits - mCredits);
                }
            } else {
                subjectRankScore = 4 * eCredits + 3 * mCredits + 2 * aCredits;
            }

            //put that disgusting thing you just did inside that mappy thing you don't know how to use yet

            subjectRankScoreMap.put(subjectsArrayList.get(i), subjectRankScore);
        }


        //THE PROBLEMATIC BIT FUCK THIS LOOP TO HELL COZ IT NEEDS TO BE A WHILE LOOP BUT IF FUCKING BREAKS FOR NO FUCKING REASON
        while(subjectRankScoreMap.size() > 5){
            int min = 96;
            String minSubject = "";
            for(Map.Entry<String,Integer> entry : subjectRankScoreMap.entrySet()){
                if (entry.getValue() <= min){
                    min = entry.getValue();
                    minSubject = entry.getKey();
                }
                //key by entry.getKey() and value by entry.getValue()
            }
            subjectRankScoreMap.remove(minSubject);
        }

        int totalECredits = 0;
        int totalMCredits = 0;
        int totalACredits = 0;

        for(Map.Entry<String,Integer> entry : subjectRankScoreMap.entrySet()){

            totalECredits += findCreditTotal(myDb.getRankScoreTotalCredits("E", level_str, entry.getKey()));
            totalMCredits += findCreditTotal(myDb.getRankScoreTotalCredits("M", level_str, entry.getKey()));
            totalACredits += findCreditTotal(myDb.getRankScoreTotalCredits("A", level_str, entry.getKey()));

            if(rankScoreType == 1){
                totalECredits += findCreditTotal(myDb.getRankScoreMockCredits("E", level_str, entry.getKey()));
                totalMCredits += findCreditTotal(myDb.getRankScoreMockCredits("M", level_str, entry.getKey()));
                totalACredits += findCreditTotal(myDb.getRankScoreMockCredits("A", level_str, entry.getKey()));
            } else if(rankScoreType == 2){
                totalECredits += findCreditTotal(myDb.getRankScoreGoalCredits("E", level_str, entry.getKey()));
                totalMCredits += findCreditTotal(myDb.getRankScoreGoalCredits("M", level_str, entry.getKey()));
                totalACredits += findCreditTotal(myDb.getRankScoreGoalCredits("A", level_str, entry.getKey()));
            }
        }

        int totalCredits = totalECredits + totalMCredits + totalACredits;
        
        int totalRankScore = 0;
        if (totalCredits >= 80) {
            if ((totalMCredits + totalECredits) >= 80) {
                if (totalECredits >= 80) {
                    totalRankScore = 4 * 80;
                } else {
                    totalRankScore = 4 * totalECredits + 3 * (80 - totalECredits);
                }
            } else {
                totalRankScore = 4 * totalECredits + 3 * totalMCredits + 2 * (80 - totalECredits - totalMCredits);
            }
        } else {
            totalRankScore = 4 * totalECredits + 3 * totalMCredits + 2 * totalACredits;
        }

        String totalRankScoreStr = Integer.toString(totalRankScore);
        String rankScoreTxt = "Rank Score: " + totalRankScoreStr;

        if(rankScoreType == 0){
            overallRankScore.setText(rankScoreTxt);
        }else if(rankScoreType == 1){
            mockRankScore.setText(rankScoreTxt);
        }else{
            goalRankScore.setText(rankScoreTxt);
        }
    }
}