package com.masterandroid.timerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.masterandroid.timerapp.fragment.AlarmFragment;
import com.masterandroid.timerapp.model.StopwatchModel;
import com.masterandroid.timerapp.model.TimeAlarmModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    //DATABASE VERSION
    private static final int DATABASE_VERSION = 6;

    //DATABASE NAME
    private static final String DATABASE_NAME = "TimerApp";

    //TABLE NAME
    private static final String TABLE_NAME = "AlarmTable";

    // Contacts Table Column Name
    private static final String KEY_ID = "id";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_NOTE = "note";
    private static final String KEY_CHECK = "ischeck";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_HOUR + " INTEGER, "
                + KEY_MINUTE + " INTEGER, "
                + KEY_CHECK + " INTEGER, "
                + KEY_NOTE + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertRow(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);

        db.close();
    }

    public void deleteRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});



        db.close();
    }

    public void updateRow(int id, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    public ArrayList<TimeAlarmModel> getAllRow() {
        ArrayList<TimeAlarmModel> timeAlarmModels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID, KEY_HOUR, KEY_MINUTE, KEY_NOTE, KEY_CHECK};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, KEY_HOUR + ", " + KEY_MINUTE + " ASC");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TimeAlarmModel model = new TimeAlarmModel();
                    String hour = String.format("%02d", cursor.getInt(cursor.getColumnIndex(KEY_HOUR)));
                    String minute = String.format("%02d", cursor.getInt(cursor.getColumnIndex(KEY_MINUTE)));
                    String note = cursor.getString(cursor.getColumnIndex(KEY_NOTE));
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    int check = cursor.getInt(cursor.getColumnIndex(KEY_CHECK));

                    model.setId(id);
                    model.setHour(hour);
                    model.setMinute(minute);
                    model.setNote(note);
                    model.setCheck(check);

                    timeAlarmModels.add(model);
                } while (cursor.moveToNext());
            }
            cursor.close(); // Đảm bảo đóng cursor sau khi sử dụng
        }

        db.close(); // Đóng cơ sở dữ liệu
        return timeAlarmModels;
    }

    public int getMaxId() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT MAX(" + KEY_ID + ") AS MAX_ID FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        int maxId = -1;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                maxId = cursor.getInt(cursor.getColumnIndex("MAX_ID"));
            }
            cursor.close();
        }

        db.close();
        return maxId;
    }

}
