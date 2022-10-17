package com.example.lab_4;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "Lab5DB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "ToDoList";
    public final static String COL_ITEMS = "Item";
    public final static String COL_ID = "ID";
    public final static int COL_URGENT = 0;

    public MyOpener(MainActivity ctx) { super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ITEMS + " TEXT," +
                COL_URGENT + " INTEGER NOT NULL)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int NewVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
