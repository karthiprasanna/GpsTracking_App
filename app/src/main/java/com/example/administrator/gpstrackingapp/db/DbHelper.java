package com.example.administrator.gpstrackingapp.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.administrator.gpstrackingapp.model.TrackingData;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "trackingdata";

    // TRACKING_DATA table name
    private static final String TABLE_TRACKING_DATA = "routemap";

    // Contacts Table Columns names
    private static final String KEY_POSITION_ID = "_id";

    private static final String KEY_LAT = "latitude";
    private static final String KEY_LON = "longitute";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRACKING_DATA + "("
                + KEY_POSITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + KEY_LAT + " INTEGER ," + KEY_LON + " TEXT"
                +")";
        db.execSQL(CREATE_CONTACTS_TABLE);


        Log.e("table","...."+CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKING_DATA);

        // Create tables again
        onCreate(db);
    }

    public void addProduct(TrackingData trackingData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

            //values.put(KEY_POSITION_ID, trackingData.get_id());
        values.put(KEY_LAT, trackingData.getLatitude());
        values.put(KEY_LON, trackingData.getLongitude());



        // Inserting Row
        db.insert(TABLE_TRACKING_DATA, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    TrackingData getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRACKING_DATA, new String[] { KEY_POSITION_ID,KEY_LAT,
                        KEY_LON }, KEY_LAT + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TrackingData contact = new TrackingData(
        );
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<TrackingData> getAllProducts() {
        List<TrackingData> contactList = new ArrayList<TrackingData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRACKING_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrackingData contact = new TrackingData();
                contact.set_id(Integer.parseInt(cursor.getString(0)));

                contact.setLatitude(cursor.getFloat(cursor.getColumnIndex(KEY_LAT)));
                contact.setLongitude(cursor.getFloat(cursor.getColumnIndex(KEY_LON)));




                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateProduct(TrackingData contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
      //  values.put(KEY_LON, contact.getThumbnailUrl());


        // updating row
        return db.update(TABLE_TRACKING_DATA, values, KEY_LAT + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }

    // Deleting single contact
    public void deleteProduct(TrackingData contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACKING_DATA, KEY_POSITION_ID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRACKING_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


}