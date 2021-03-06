package com.df.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus88 on 2016/5/3.
 */
public class DataBaseHelp extends SQLiteOpenHelper {

    private static final String DATABASENAME = "collect.db";
    private static final int DATABASEVERSION = 1;
    private static final String TABLENAME = "collection";

    public DataBaseHelp(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLENAME + " (" + "id VARCHAR(10) PRIMARY KEY ,"+
                "name VARCHAR(50) NOT NULL ," +
                "image VARCHAR(300) NOT NULL ," +
                "description VARCHAR(150) ," +
                "price VARCHAR(30) ," +
                "url VARCHAR(300) NOT NULL)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLENAME;
        sqLiteDatabase.execSQL(sql);
        this.onCreate(sqLiteDatabase);
    }
}
