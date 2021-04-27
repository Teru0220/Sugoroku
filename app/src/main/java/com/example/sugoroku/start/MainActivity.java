package com.example.sugoroku.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sugoroku.TestSQL;
import com.example.sugoroku.map.MapOpenHelper;
import com.example.sugoroku.menu.MenuListActivity;
import com.example.sugoroku.R;

public class MainActivity extends AppCompatActivity {

    Button start;
    SQLiteDatabase db;
    MapOpenHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初期マップ挿入
        //ヘルパーがセットされていない場合は生成する。
        if(helper == null){
            helper = new MapOpenHelper(getApplicationContext());
        }
        //DBがセットされていない場合はセットする。
        if(db == null){
            db = helper.getReadableDatabase();//読み込み用データベースを取得
        }
        db.close();

        start = findViewById(R.id.Stbn);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuList = new Intent(MainActivity.this, MenuListActivity.class);
                startActivity(menuList);
            }
        });


    }
}