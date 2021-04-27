package com.example.sugoroku.map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sugoroku.TestSQL;

public class MapOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    public static final int DATABASE_VERSION = 1;
    // データーベース名
    public static final String DATABASE_NAME = "MapDB.db";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TITLE = "event";
    private static final String COLUMN_NAME_TITLE2 = "changeEvent";
    private static final String COLUMN_NAME_EVENT = "changeMoney";
    private static final String COLUMN_NAME_DIRECTION = "directions";
    private Context context;

    public MapOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 初期マップテーブル作成
        db.execSQL("CREATE TABLE map1db " +
                "(_id INTEGER PRIMARY KEY,event TEXT,changeEvent TEXT,changeMoney INTEGER," +
                "upNextNumber INTEGER,leftNextNumber INTEGER,downNextNumber INTEGER,rightNextNumber INTEGER)");
        db.execSQL("CREATE TABLE map2db " +
                "(_id INTEGER PRIMARY KEY,event TEXT,changeEvent TEXT,changeMoney INTEGER," +
                "upNextNumber INTEGER,leftNextNumber INTEGER,downNextNumber INTEGER,rightNextNumber INTEGER)");
        TestSQL testSQL = new TestSQL(db);
        testSQL.sqlData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別、古いバージョンは削除して新規作成
        db.execSQL("DROP TABLE IF EXISTS map1db");
        db.execSQL("DROP TABLE IF EXISTS map1db");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db,
                            int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
