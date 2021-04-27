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
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sugoroku.R;
import com.example.sugoroku.layout.Roulette;

//　　4/24 縦横移動スクロール追尾完了。スクロールが早すぎるかも
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

    protected int musuHeight;
    protected int musuWidth;
    protected int wDisplay;
    protected int hDisplay;
    protected FrameLayout frameLayout;

    private PlayerIcon playerIcon;
    private Player player;

    private Roulette roulette;

    private MapOpenHelper helper;//SQLの操作用
    private SQLiteDatabase db;//DBファイル

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
    @Override
    protected void onResume() {
        super.onResume();
        readData();
    }

    @Override//layoutが呼び出されてから実行される状態
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        musuHeight = changeView[0].getHeight() * 3;
        musuWidth = changeView[0].getWidth();
        //すべてのマスの絶対座標を取得
        for(int i =0;i <masuCoordinate.length;i++){
            eventView[i].getLocationInWindow(masuCoordinate[i]);
        }
        //プレイヤーアイコンを作成
        playerIcon = new PlayerIcon(this,frameLayout,musuHeight,musuWidth, wDisplay, hDisplay,
                masuCoordinate,scrollView,horizontalScrollView);
        playerIcon.makePlayerIcon();
        //プレイヤー情報の入力
        player = new Player("マリン", 1000, playerIcon);
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
            cursor.moveToNext();
        }
        //SQLの終了
        cursor.close();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://指を付けたとき
                //テスト用
                makeRolette();
                player.getIcon().makeArrows(masuData[player.getIcon().nowPoint]);
                //
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
    public void makeRolette(){
        roulette = new Roulette(this);
        roulette.setX((playerIcon.scrollNowXY[0]+ wDisplay)-(wDisplay/2.0f));
        roulette.setY((playerIcon.scrollNowXY[1] + hDisplay) -(wDisplay/1.5f));
        ConstraintLayout.LayoutParams leyer= new ConstraintLayout.LayoutParams((int)Math.ceil(wDisplay/2.5f),(int)Math.ceil(wDisplay/2.5f));
        constraintLayout.addView(roulette,leyer);
    }
}