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

    private static final String CUSTOMER_ID = "customer_id";
    private static final String ID = "id";
   private static final String CUSTOMER_TABLE = "customer_table";

    public Database(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String TABLE="CREATE TABLE "+CREATE_TABLE_LIST+"("+KEY_ID +" TEXT,"+NAME +" TEXT"+")";
        String CUSTOMER="CREATE TABLE "+CUSTOMER_TABLE+"("+ID +" TEXT,"+CUSTOMER_ID +" TEXT"+")";
        sqLiteDatabase.execSQL(TABLE);
        sqLiteDatabase.execSQL(CUSTOMER);
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

    public long insertCustomerTable(String id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID,id);
        values.put(CUSTOMER_ID,name);
        long list=db.insert(CUSTOMER_TABLE, null, values);
        db.close();
        return list;
    }
    public  String getCustomerId() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CUSTOMER_TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();


        String data = cursor.getString(1);

        return data;
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

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ CREATE_TABLE_LIST);
        db.close();

        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("delete from "+ CUSTOMER_TABLE);
        db1.close();
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
