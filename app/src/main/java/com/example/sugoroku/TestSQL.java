package com.example.sugoroku;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.sugoroku.map.MapOpenHelper;


public class TestSQL {
    private static String maney= "所持金：";
    public static String[] a = {"スタート", "b","c","d","e","f","g","h","i","j","k","l","n","m","o","p","q","r","s","t","u","v","w","x","y","ゴール"};

    public static String[] b = {"　",maney+"100","2マス進む",maney+"100",maney+"-100",maney+"100",maney+"-100",maney+"100",  "5マス戻る",
            maney+"100",maney+"-100",maney+"100",maney+"-100",maney+"100",maney+"-100",maney+"100","6マス戻る",maney+"100",maney+"-100",maney+"100",
            maney+"-100",maney+"100",maney+"-100",maney+"100",maney+"-100","　"};


    public static int[] c = {0,100,2,100,-100,100,-100,100,  -5,100,-100,100,-100,100,-100,100,  -6,100,-100,100,-100,100,-100,100,  -100,0};
    public static int[] d = {0,0,2,0,0,0,0,0,  1,0,0,0,0,0,0,0,  1,0,0,0,0,0,0,0,  0,0};
    //北西南東の順に2357で表し進行可能な方角をかけ合わせる
    public static int[] upNextNumber ={-1,0,1,2,3,4,5,6,  -1,  10,11,12,13,14,15,16,-1,  -1,  -1,18,19,20,21,22,23,24};
    public static int[] leftNextNumber ={-1,-1,-1,-1,-1,-1,-1,-1,  7,  8,-1,-1,-1,-1,-1,-1,-1,  16,  17,-1,-1,-1,-1,-1,-1,-1};
    public static int[] downNextNumber ={1,2,3,4,5,6,7,-1,  -1,  -1,9,10,11,12,13,14,15,  -1,  19,20,21,22,23,24,25,-1};
    public static int[] rightNextNumber ={-1,-1,-1,-1,-1,-1,-1,8,  9,  -1,-1,-1,-1,-1,-1,-1,17,  18,  -1,-1,-1,-1,-1,-1,-1,-1};

    public static String[] aa = {"スタート","bb","cc","dd","ee","ff","gg","hh","ii","jj","kk","ll","nn","mm","oo","pp","qq","rr","ss","tt","uu","vv","ww","xx","yy","ゴール"};

}
