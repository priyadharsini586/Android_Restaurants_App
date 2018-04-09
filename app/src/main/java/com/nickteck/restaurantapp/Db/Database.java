package com.nickteck.restaurantapp.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 4/9/2018.
 */

public class Database  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "ID";
    private static final String NAME= "NAME";

    private static final String DATABASE_NAME = "TABLE";
    private static final String CREATE_TABLE_LIST ="table_list";



    public Database(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String TABLE="CREATE TABLE "+CREATE_TABLE_LIST+"("+KEY_ID +" TEXT,"+NAME +" TEXT"+")";
        sqLiteDatabase.execSQL(TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +CREATE_TABLE_LIST);
        onCreate(sqLiteDatabase);
    }
    public long insertTable(String id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,id);
        values.put(NAME,name);
        long list=db.insert(CREATE_TABLE_LIST, null, values);
        db.close();
        return list;
    }
    public  String getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CREATE_TABLE_LIST;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();


        String data = cursor.getString(1);

        return data;
    }
    public boolean checkTables() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + CREATE_TABLE_LIST, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
}
