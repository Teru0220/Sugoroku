package com.example.sugoroku.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.sugoroku.map.MapOpenHelper;
import com.example.sugoroku.R;

import java.util.ArrayList;

public class MenuListActivity extends AppCompatActivity{

    RecyclerView recyclerView;

    private MapOpenHelper helper;//SQLの操作用
    private SQLiteDatabase db;//DBファイル

    ArrayList<MenuData> list = new ArrayList<>();//仮で名前のみ
    ArrayList<String> tableName = new ArrayList<>();
    ArrayList<Integer> masuTotal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        recyclerView = findViewById(R.id.card_list);
        //RecyclerViewのサイズが変わらないときの性能向上
        recyclerView.setHasFixedSize(true);
        //RecyclerViewの制御を行うマネージャーの生成
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //マネージャーに動作する方向を設定
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerViewにマネージャーを設定
        recyclerView.setLayoutManager(linearLayoutManager);
        //SQLからリストを作成
        makeList();
        //画面変移用にthisも引き渡しておく。
        RecyclerView.Adapter adapter = new MenuAdapter(list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        //テスト用
        /*TestSQL testSQL = new TestSQL(this);
        testSQL.sqlData();*/
    }

    //DBからタイトルリスト作成
    public void makeSqlTitleList(){
        //コンソールを生成しテーブル名一覧を取得
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ", null);
        c.moveToFirst();
        c.moveToNext();
        while (!c.isAfterLast()){//isAfterLast()で最後のデータか確認してるみたい
            tableName.add(c.getString(0));
            c.moveToNext();
        }
        //SQLの終了
        c.close();
    }

    public void makeSqlCountList() {
        for(int i = 0;i < tableName.size();i++){
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + tableName.get(i), null);
        c.moveToFirst();
        masuTotal.add(Integer.parseInt(c.getString(0)));
        c.close();
        }
    }

    public void makeList(){
        startSql();
        makeSqlTitleList();
        makeSqlCountList();
        for(int i = 0; i < tableName.size();i++){
            list.add(new MenuData(tableName.get(i),masuTotal.get(i)));
        }
    }
    public void startSql(){
        if(helper == null){
            helper = new MapOpenHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getReadableDatabase();//読み込み用データベースを取得
        }
    }
}