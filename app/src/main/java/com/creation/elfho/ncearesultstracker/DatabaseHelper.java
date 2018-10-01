package com.creation.elfho.ncearesultstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;


/**
 * Created by elfho on 23/12/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NCEA_app";
    private static final int DATABASE_VERSION = 1;

    //subject table
    public static final String SUBJECTS_TABLE_NAME = "subject_table";
    public static final String SUBJECTS_COL_1 = "ID";
    public static final String SUBJECTS_COL_2 = "SUBJECT";
    public static final String SUBJECTS_COL_3 = "LEVEL";
    public static final String CREATE_SUBJECTS_TABLE = "create table " + SUBJECTS_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,SUBJECT TEXT,LEVEL INTEGER, unique(SUBJECT, LEVEL))";

    //standards table
    public static final String STANDARDS_TABLE_NAME = "standards_table";
    public static final String STANDARDS_COL_1 = "ID";
    public static final String STANDARDS_COL_2 = "STANDARD";
    public static final String STANDARDS_COL_3 = "SUBJECT";
    public static final String STANDARDS_COL_4 = "STANDARD_TYPE";
    public static final String STANDARDS_COL_5 = "EXAM_TYPE";
    public static final String STANDARDS_COL_6 = "CREDITS";
    public static final String STANDARDS_COL_7 = "GOAL";
    public static final String STANDARDS_COL_8 = "MOCK";
    public static final String STANDARDS_COL_9 = "FINAL";
    public static final String STANDARDS_COL_10 = "LEVEL";

    public static final String CREATE_STANDARDS_TABLE = "CREATE TABLE " + STANDARDS_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, STANDARD TEXT, SUBJECT TEXT, STANDARD_TYPE TEXT, EXAM_TYPE TEXT, CREDITS TEXT, GOAL TEXT, MOCK TEXT, FINAL TEXT, LEVEL TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SUBJECTS_TABLE);
        db.execSQL(CREATE_STANDARDS_TABLE);
    }

    //set up system for upgrading app
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //functions for subjects table:

    //insert new subject
    public boolean insertSubject(String subject,String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBJECTS_COL_2,subject);
        contentValues.put(SUBJECTS_COL_3,level);
        long result = db.insert(SUBJECTS_TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //return list of subjects for a given level
    public ArrayList getSubjects(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + SUBJECTS_TABLE_NAME + " where " + SUBJECTS_COL_3 + "="+level,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(1));
        }
        return mArrayList;
    }
    //return list of subject ids for a given level
    public ArrayList getSubjectIds(String level){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {SUBJECTS_COL_1};
        String [] selectionArgs = new String[] {level};
        Cursor subjectCursor = db.query(SUBJECTS_TABLE_NAME, columnsToReturn, SUBJECTS_COL_3 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(0));
        }
        return mArrayList;
    }

    //delete subject by name and level
    public void deleteSubject (String subject, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + SUBJECTS_TABLE_NAME + " WHERE " + SUBJECTS_COL_2 + " = ?" + " AND " + SUBJECTS_COL_3 + " = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, subject);
        statement.bindString(2, level);

        int numRowsDeleted = statement.executeUpdateDelete();
        ////might want to check the number of records which were actually deleted
    }


    //functions for standards table
    public int getSubjectId(String level, String subject){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {SUBJECTS_COL_1, SUBJECTS_COL_2, SUBJECTS_COL_3 };
        String [] selectionArgs = new String[] {subject, level};
        Cursor subjectCursor = db.query(SUBJECTS_TABLE_NAME, columnsToReturn, SUBJECTS_COL_2 + " =? AND " + SUBJECTS_COL_3 + " =?", selectionArgs, null, null, null);
        ArrayList<Integer> mArrayList = new ArrayList<Integer>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getInt(0));
        }
        return mArrayList.get(0);
    }

    //insert new standard
    public boolean insertStandard(String standard, String subject, String standard_type, String exam_type, String credits, String grade, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STANDARDS_COL_2, standard);
        contentValues.put(STANDARDS_COL_3, subject);
        contentValues.put(STANDARDS_COL_4, standard_type);
        contentValues.put(STANDARDS_COL_5, exam_type);
        contentValues.put(STANDARDS_COL_6, credits);
        contentValues.put(STANDARDS_COL_7, grade);
        contentValues.put(STANDARDS_COL_8, grade);
        contentValues.put(STANDARDS_COL_9, grade);
        contentValues.put(STANDARDS_COL_10, level);

        long result = db.insert(STANDARDS_TABLE_NAME,null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //get all standards for a particular subject
    public ArrayList getStandards(int subject){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + STANDARDS_TABLE_NAME + " where " + STANDARDS_COL_3 + "="+subject,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(1));
        }
        return mArrayList;
    }

    //get all standards for a particular level
    public ArrayList getStandardNames(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + STANDARDS_TABLE_NAME + " where " + STANDARDS_COL_10 + "="+level,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(1));
        }
        return mArrayList; //need to return columns 2, 6, 7, 8 and 9 for standards overview table
    }

    //get all standard for a particular level
    public ArrayList getStandardCredits(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + STANDARDS_TABLE_NAME + " where " + STANDARDS_COL_10 + "="+level,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(5));
        }
        return mArrayList; //need to return columns 2, 6, 7, 8 and 9 for standards overview table
    }

    //get all standard goal grades for a particular level
    public ArrayList getStandardGoal(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + STANDARDS_TABLE_NAME + " where " + STANDARDS_COL_10 + "="+level,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(6));
        }
        return mArrayList; //need to return columns 2, 6, 7, 8 and 9 for standards overview table
    }

    //get all standards mock grades for a particular level
    public ArrayList getStandardMock(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + STANDARDS_TABLE_NAME + " where " + STANDARDS_COL_10 + "="+level,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(7));
        }
        return mArrayList; //need to return columns 2, 6, 7, 8 and 9 for standards overview table
    }
    //get all standards final grades for a particular level
    public ArrayList getStandardFinal(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + STANDARDS_TABLE_NAME + " where " + STANDARDS_COL_10 + "="+level,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(8));
        }
        return mArrayList; //need to return columns 2, 6, 7, 8 and 9 for standards overview table
    }

    //get all grades for a particular subject
    public ArrayList getGrades(int subject){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor subjectCursor = db.rawQuery("select * from " + STANDARDS_TABLE_NAME + " where " + STANDARDS_COL_3 + "="+subject,null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(8));
        }
        return mArrayList;
    }

    //return all info for a particular standard
    public ArrayList getInfo(String subject, String standard) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {STANDARDS_COL_4, STANDARDS_COL_5, STANDARDS_COL_6};
        String [] selectionArgs = new String[] {standard, subject};
        Cursor subjectCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_2 + " =? AND " + STANDARDS_COL_3 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(0));
            mArrayList.add(subjectCursor.getString(1));
            mArrayList.add(subjectCursor.getString(2));
        }
        return mArrayList;
    }

    public void updateGrades(String column, String tag, String standard, String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column, tag);
        String [] selectionArgs = new String[] {standard, subject};
        db.update(STANDARDS_TABLE_NAME, cv, STANDARDS_COL_2 + "=? AND " + STANDARDS_COL_3 + "=?", selectionArgs);
    }

    //return all info for a particular standard
    public ArrayList getStandardGrades(String subject, String standard) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {STANDARDS_COL_7, STANDARDS_COL_8, STANDARDS_COL_9};
        String [] selectionArgs = new String[] {standard, subject};
        Cursor subjectCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_2 + " =? AND " + STANDARDS_COL_3 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(subjectCursor.moveToFirst(); !subjectCursor.isAfterLast(); subjectCursor.moveToNext()) {
            mArrayList.add(subjectCursor.getString(0));
            mArrayList.add(subjectCursor.getString(1));
            mArrayList.add(subjectCursor.getString(2));
        }
        return mArrayList;
    }


    //return arraylist of final results based on grade
    public ArrayList getCourseEndorsementCredits(String grade, String level) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] { STANDARDS_COL_6 };
        String [] selectionArgs = new String[] {grade, level};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + "=?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    //return arraylist of mock results based on grade
    public ArrayList getMockCredits(String grade, String level) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] { STANDARDS_COL_6 };
        String [] selectionArgs = new String[] {grade, "+", level};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_8 + "=? AND " + STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + "=?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    //return arraylist of goal results based on grade
    public ArrayList getGoalCredits(String grade, String level) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] { STANDARDS_COL_6 };
        String [] selectionArgs = new String[] {grade, "+", level};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_7 + "=? AND " + STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + "=?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    public ArrayList getSubjectEndorsementCredits(String grade, String level, String subjectId, String exam_type) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {STANDARDS_COL_6};
        String [] selectionArgs = new String[] {subjectId, grade, level, exam_type};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_3 + "=? AND " + STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + "+? AND " + STANDARDS_COL_5 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    public int deleteSubjectStandards (String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + STANDARDS_TABLE_NAME + " WHERE " + STANDARDS_COL_3 + " =?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, subject);
        int numRowsDeleted = statement.executeUpdateDelete();
        return numRowsDeleted;
    }

    public ArrayList getTotalSubjectCredits(String grade, String level, String subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {STANDARDS_COL_6};
        String [] selectionArgs = new String[] {subjectId, grade, level};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_3 + "=? AND " + STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    public ArrayList getRankScoreTotalCredits(String grade, String level, String subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {STANDARDS_COL_6};
        String [] selectionArgs = new String[] {subjectId, grade, level, "1"};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_3 + "=? AND " + STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + " =? AND " + STANDARDS_COL_4 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    public ArrayList getRankScoreGoalCredits(String grade, String level, String subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {STANDARDS_COL_6};
        String [] selectionArgs = new String[] {subjectId, grade, "+", level, "1"};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_3 + "=? AND " + STANDARDS_COL_7 + "=? AND " + STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + " =? AND " + STANDARDS_COL_4 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    public ArrayList getRankScoreMockCredits(String grade, String level, String subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columnsToReturn = new String[] {STANDARDS_COL_6};
        String [] selectionArgs = new String[] {subjectId, grade, "+", level, "1"};
        Cursor creditCursor = db.query(STANDARDS_TABLE_NAME, columnsToReturn, STANDARDS_COL_3 + "=? AND " + STANDARDS_COL_8 + "=? AND " + STANDARDS_COL_9 + "=? AND " + STANDARDS_COL_10 + " =? AND " + STANDARDS_COL_4 + " =?", selectionArgs, null, null, null);
        ArrayList<String> mArrayList = new ArrayList<String>();
        for(creditCursor.moveToFirst(); !creditCursor.isAfterLast(); creditCursor.moveToNext()) {
            mArrayList.add(creditCursor.getString(0));
        }
        return mArrayList;
    }

    //delete all info for a particular standard
    public int deleteStandard(String subject, String standard) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + STANDARDS_TABLE_NAME + " WHERE " + STANDARDS_COL_2 + " =? AND " + STANDARDS_COL_3 + "=?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1, standard);
        statement.bindString(2, subject);
        int numRowsDeleted = statement.executeUpdateDelete();
        return numRowsDeleted;
    }

}