package com.example.flappybird_prm391;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.flappybird_prm391.model.Score;

import java.util.ArrayList;
import java.util.List;

public class LocalScoreManagement extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Flappybird.db";
    private static final String TABLE_SCORE = "tbl_score";
    private static final String KEY_ID = "ID";
    private static final String KEY_DATE = "CDATE";
    private static final String KEY_SCORE = "SCORE";

    public LocalScoreManagement(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create question table
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORE + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATE
                + " TEXT, " + KEY_SCORE + " INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }

    public void insertScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_SCORE);
    }

    public List<Score> getScoreBoard(){
        List<Score> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SCORE + " ORDER BY " + KEY_SCORE + " DESC LIMIT 10";
        SQLiteDatabase db = this.getReadableDatabase();
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
        return result;
    }

    public Score getTopScore(){
        Score result = new Score();
        String query = "SELECT * FROM " + TABLE_SCORE + " ORDER BY " + KEY_SCORE + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
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
        return result.getId() != 0 ? result : null;
    }
}
