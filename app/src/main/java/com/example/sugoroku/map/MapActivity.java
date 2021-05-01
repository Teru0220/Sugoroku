package com.example.sugoroku.map;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sugoroku.R;
import com.example.sugoroku.layout.Roulette;

//　　4/30 ゲーム完了までの流れを完成。CPUはまだ前にしか進めないためゴールできない。全員プレイヤーでゴールの検証が必要。
public class MapActivity extends AppCompatActivity {
    //スクロールビューのためのフィールド
    private float moveX;
    private float moveY;
    private float currentX;
    private float currentY;
    private ScrollView scrollView;
    private HorizontalScrollView horizontalScrollView;
    private ConstraintLayout constraintLayout;

    private TextView[] eventView;
    private TextView[] changeView;
    private MasuData[] masuData;
    private int[][] masuCoordinate;
    private int masuTotal;

    protected static int musuHeight;
    protected static int musuWidth;
    protected static int wDisplay;
    protected static int hDisplay;
    protected static float startX;
    protected static float startY;
    protected FrameLayout frameLayout;

    public static int startPoint = 0;
    public static int goalPoint = 0;

    private MapOpenHelper helper;//SQLの操作用
    private SQLiteDatabase db;//DBファイル

    private boolean startFlag = true;

    private GameMaster gameMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masuTotal = getIntent().getIntExtra("masuTotal",0);
        if(masuTotal == 26) {
            setContentView(R.layout.activity_map26);
        }

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        wDisplay = point.x;
        hDisplay = point.y;

        eventView = new TextView[masuTotal];
        changeView = new TextView[masuTotal];
        masuData = new MasuData[masuTotal];
        masuCoordinate = new int[masuTotal][2];

        constraintLayout = findViewById(R.id.map_activity);
        scrollView =  findViewById(R.id.vScroll);
        horizontalScrollView =  findViewById(R.id.hScroll);
        frameLayout = findViewById(R.id.frame);

        TypedArray eText = getResources().obtainTypedArray(R.array.event_text_array26);
        TypedArray cText = getResources().obtainTypedArray(R.array.change_text_array26);
        for(int i = 0;i < eventView.length;i++){
            eventView[i] = findViewById(eText.getResourceId(i,0));
            changeView[i] = findViewById(cText.getResourceId(i,0));
        }
    }

    @Override//layoutが呼び出されてから実行される状態
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        musuHeight = changeView[0].getHeight() * 3;
        musuWidth = changeView[0].getWidth();
        //データの読み込み
        readData();
        //
        startPoint = 18;
        //
        startX = eventView[startPoint].getX();
        startY = eventView[startPoint].getY();
        //すべてのマスの絶対座標を取得
        for(int i =0;i <masuCoordinate.length;i++){
            eventView[i].getLocationInWindow(masuCoordinate[i]);
        }
        String[] name = getIntent().getStringArrayExtra("playerName");
        Player[] players = new Player[4];
        int playCPU = getIntent().getIntExtra("playCPU",0);

        if(startFlag) {
            gameMaster = new GameMaster(players, masuData, this, constraintLayout);
            for(int i = 0;i< players.length-playCPU;i++) {
                //プレイヤーアイコンを作成用インスタンス
                PlayerIcon playerIcon = new PlayerIcon(this, frameLayout,
                        masuCoordinate, scrollView, horizontalScrollView,gameMaster);
                //プレイヤー情報の入力
                players[i] = new Player(name[i], 1000, playerIcon,i);
            }
            for(int i = players.length-playCPU;i< players.length;i++) {
                //プレイヤーアイコンを作成
                PlayerIcon playerIcon = new PlayerIcon(this, frameLayout,
                        masuCoordinate, scrollView, horizontalScrollView,gameMaster);
                //プレイヤー情報の入力
                players[i] = new Player(name[i], 1000, playerIcon,i);
                players[i].setCpu();
            }
            gameMaster.StartGeme();
            startFlag = false;
        }
    }

   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        finish();
        return true;
    }

    //DBからのデータ呼び出し
    public void readData(){
        //ヘルパーがセットされていない場合は生成する。
        if(helper == null){
            helper = new MapOpenHelper(getApplicationContext());
        }
        //DBがセットされていない場合はセットする。
        if(db == null){
            db = helper.getReadableDatabase();//読み込み用データベースを取得
        }
        //コンソールを生成してデータを呼び出す
        Cursor cursor = db.query(
                getIntent().getStringExtra("mapName"),//テーブル（表の名前）
                new String[] { "event", "changeEvent","changeMoney" ,"upNextNumber" ,"leftNextNumber" ,"downNextNumber" ,"rightNextNumber"},//呼び出す列名
                null,
                null,
                null,
                null,
                null
        );
        //SQLの読み出し開始
        cursor.moveToFirst();

        for(int i = 0; i < masuTotal;i++){
            masuData[i] = new MasuData(cursor.getString(0),cursor.getString(1),cursor.getInt(2),
                    cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6),eventView[i]);
            eventView[i].setText(masuData[i].getEvent());
            changeView[i].setText(masuData[i].getChangeEvent());
            if(masuData[i].getEvent().equals("スタート")){this.startPoint = i;}
            if(masuData[i].getEvent().equals("ゴール")){this.goalPoint = i;}
            cursor.moveToNext();
        }
        //SQLの終了
        cursor.close();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gameMaster.players[gameMaster.turn].isCpu()){
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://指を付けたとき
                moveX = event.getX();
                moveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE://指を動かした動き
                currentX = event.getX();
                currentY = event.getY();
                horizontalScrollView.scrollBy((int) (moveX - currentX), (int) 0);
                scrollView.scrollBy((int) 0, (int) (moveY - currentY));
                moveX = currentX;
                moveY = currentY;
                break;
            case MotionEvent.ACTION_UP://指を離した動き
        }
        return true;
    }
}