package com.example.andrea.inventoryappcerealsstage2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.andrea.inventoryappcerealsstage2.Data.CerealsContract.CerealsEntry;

public class DatabaseCereals extends SQLiteOpenHelper {
    public static final String LOG_TAG = DatabaseCereals.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "DatabaseCereals.db";

    public DatabaseCereals(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        /**
         * This is called when the database is created for the first time.
         */
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the pets table

        String SQL_CREATE_CEREALS_TABLE = "CREATE TABLE " + CerealsEntry.TABLE_NAME + " ("
                + CerealsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CerealsEntry.Column_PRODUCT_NAME + " TEXT NOT NULL, "
                + CerealsEntry.Column_QUANTITY + " INTEGER NOT NULL, "
                + CerealsEntry.Column_PRICE + " INTEGER NOT NULL, "
                + CerealsEntry.Column_PARTNER_NAME + " TEXT NOT NULL, "
                + CerealsEntry.Column_PARTNER_CONTACT + " INTEGER NOT NULL);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CEREALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}