package com.example.sugoroku.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sugoroku.TestSQL;
import com.example.sugoroku.map.MapOpenHelper;
import com.example.sugoroku.menu.MenuListActivity;
import com.example.sugoroku.R;

public class MainActivity extends AppCompatActivity {

    Button start,setting;
    SQLiteDatabase db;
    MapOpenHelper helper;
    Intent menuList;
    MediaPlayer bgm;

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
        setting = findViewById(R.id.Stbn2);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuList = new Intent(MainActivity.this, MenuListActivity.class);
                menuList.putExtra("mode","play");
                startActivity(menuList);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuList = new Intent(MainActivity.this, MenuListActivity.class);
                menuList.putExtra("mode","setting");
                startActivity(menuList);
            }
        });
    }
    @Override
    protected  void onResume(){
        super.onResume();
        bgm = MediaPlayer.create(this,R.raw.home_bgm);
        bgm.setLooping(true);
        bgm.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        bgm.release();
    }
}