package com.example.flappybird_prm391;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.flappybird_prm391.model.Score;

import java.util.ArrayList;
import java.util.List;

public class LocalDataHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Flappybird.db";
    // tbl_score
    private static final String TABLE_SCORE = "tbl_score";
    private static final String KEY_ID = "ID";
    private static final String KEY_DATE = "CDATE";
    private static final String KEY_SCORE = "SCORE";
    // tbl_setting
    private static final String TABLE_SETTING = "tbl_setting";
    private static final String KEY_SETTING = "SETTING";
    private static final String KEY_VALUE = "VALUE";
    // Database instance
    private SQLiteDatabase db;

    public LocalDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        this.db = db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORE + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATE
                + " TEXT, " + KEY_SCORE + " INTEGER)";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SETTING + " ( "
                + KEY_SETTING + " TEXT PRIMARY KEY, " + KEY_VALUE
                + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
        onCreate(db);
    }

    public void insertScore(Score score) {
        this.db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, score.getDate());
        values.put(KEY_SCORE, score.getScore());
        db.insert(TABLE_SCORE, null, values);
    }

    public void insertBatchScore(List<Score> scores){
        for(Score score : scores){
            insertScore(score);
        }
    }

    public void clearData(){
        this.db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_SCORE);
    }

    public List<Score> getScoreBoard(){
        List<Score> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SCORE + " ORDER BY " + KEY_SCORE + " DESC LIMIT 10";
        this.db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Score s = new Score();
                s.setId(cursor.getInt(0));
                s.setDate(cursor.getString(1));
                s.setScore(cursor.getInt(2));
                result.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public Score getTopScore(){
        Score result = new Score();
        String query = "SELECT * FROM " + TABLE_SCORE + " ORDER BY " + KEY_SCORE + " DESC LIMIT 1";
        this.db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Score s = new Score();
                s.setId(cursor.getInt(0));
                s.setDate(cursor.getString(1));
                s.setScore(cursor.getInt(2));
                result = s;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result.getId() != 0 ? result : null;
    }

    public void putSetting(String setting, String value){
        this.db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SETTING + " WHERE " + KEY_SETTING + " = '" + setting + "'");
        ContentValues values = new ContentValues();
        values.put(KEY_SETTING, setting);
        values.put(KEY_VALUE, value);
        db.insert(TABLE_SETTING, null, values);
    }

    public String getSetting(String setting){
        String query = "SELECT * FROM " + TABLE_SETTING + " WHERE " + KEY_SETTING + " = '" + setting + "'";
        this.db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(query, null);
        cursor.moveToFirst();
        String result = "";
        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    @Override
    public synchronized void close () {
        if (db != null) {
            db.close();
            super.close();
        }
    }
}
