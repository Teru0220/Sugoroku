package com.example.sugoroku;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.sugoroku.map.MapOpenHelper;


public class TestSQL {

    //テストデータ作成用
    SQLiteDatabase db;

    static String[] a = {"a","b","c","d","e","f","g","h","i","j","k","l","n","m","o","p","q","r","s","t","u","v","w","x","y","z"};
    static String[] b = {"0","1","2","3","4","5","6","7",  "8",  "9","10","11","12","13","14","15","16",  "17",  "18","19","20","21","22","23","24","25"};
    //北西南東の順に2357で表し進行可能な方角をかけ合わせる
    //static int[] c ={5,10,10,10,10,10,10,14,  21,  6,10,10,10,10,10,10,35,  6,  15,10,10,10,10,10,10,0};
    static int[] upNextNumber ={-1,0,1,2,3,4,5,6,  -1,  10,11,12,13,14,15,16,-1,  -1,  -1,18,19,20,21,22,23,-1};
    static int[] leftNextNumber ={-1,-1,-1,-1,-1,-1,-1,-1,  7,  8,-1,-1,-1,-1,-1,-1,-1,  16,  17,-1,-1,-1,-1,-1,-1,-1};
    static int[] downNextNumber ={1,2,3,4,5,6,7,-1,  -1,  -1,9,10,11,12,13,14,15,  -1,  19,20,21,22,23,24,25,-1};
    static int[] rightNextNumber ={-1,-1,-1,-1,-1,-1,-1,8,  9,  -1,-1,-1,-1,-1,-1,-1,17,  18,  -1,-1,-1,-1,-1,-1,-1,-1};

    static String[] aa = {"aa","bb","cc","dd","ee","ff","gg","hh","ii","jj","kk","ll","nn","mm","oo","pp","qq","rr","ss","tt","uu","vv","ww","xx","yy","zz"};
    static String[] bb = {"00","11","22","33","44","55","66","77","88","99","1010","1111","1212","1313","1414","1515","1616","1717","1818","1919","2020","2121","2222","2323","2424","2525"};


    public TestSQL(SQLiteDatabase db){
        this.db = db;
    }
    public void sqlData(){

        for(int i = 0 ; i < a.length;i++){
            insertData(db,a[i],b[i],i,upNextNumber[i],leftNextNumber[i],downNextNumber[i],rightNextNumber[i],"map1db");
        }
        for(int i = 0 ; i < a.length;i++){
            insertData(db,a[i],b[i],i,upNextNumber[i],leftNextNumber[i],downNextNumber[i],rightNextNumber[i],"map2db");
        }
    }

    //Testデータ作成用,SQLへの入力処理
    private void insertData(SQLiteDatabase db, String event, String changeEvent, int changeMoney,
                            int upNextNumber, int leftNextNumber, int downNextNumber, int rightNextNumber, String tableName){
        ContentValues values = new ContentValues();
        values.put("event", event);
        values.put("changeEvent", changeEvent);
        values.put("changeMoney", changeMoney);
        values.put("upNextNumber", upNextNumber);
        values.put("leftNextNumber", leftNextNumber);
        values.put("downNextNumber", downNextNumber);
        values.put("rightNextNumber", rightNextNumber);

        db.insert(tableName, null, values);
    }
}
