package com.example.sugoroku.make_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sugoroku.R;
import com.example.sugoroku.TestSQL;
import com.example.sugoroku.layout.MasuEditView;
import com.example.sugoroku.layout.SetOrDelWindow;
import com.example.sugoroku.map.MapOpenHelper;
import com.example.sugoroku.map.MasuData;

import java.util.ArrayList;
import java.util.List;

//マスの拡大を防止したい
public class MakeMapActivity extends AppCompatActivity {

    private float moveX;
    private float moveY;
    private float currentX;
    private float currentY;
    private float wDisplay;
    private float hDisplay;
    private int wMasu;
    private int hMasu;
    private ScrollView scrollView;
    private HorizontalScrollView horizontalScrollView;
    private ConstraintLayout constraintLayout;

    private TextView[] eventView;
    private TextView[] changeView;
    private MasuData[] masuData;
    private int[][] masuCoordinate;
    private int masuTotal;
    private String mapName;
    boolean read;
    private Button button,button2;
    private SetOrDelWindow setOrDelWindow;
    private MasuEditView masuEditView;
    private boolean newMapFlag = false;
    private boolean masuEditFlag = true;
    private Context context;

    private MapOpenHelper helper;//SQLの操作用
    private SQLiteDatabase db;//DBファイル

    private boolean startFlag = true;

    private List<LinearLayout> linearLayout = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masuTotal = getIntent().getIntExtra("masuTotal",0);
        mapName = getIntent().getStringExtra("mapName");
        read = getIntent().getBooleanExtra("read",false);

        if(masuTotal == 26) {
            setContentView(R.layout.activity_map26);
        }
        scrollView =  findViewById(R.id.vScroll);
        horizontalScrollView =  findViewById(R.id.hScroll);
        constraintLayout = findViewById(R.id.map_activity);

        eventView = new TextView[masuTotal];
        changeView = new TextView[masuTotal];
        masuData = new MasuData[masuTotal];
        masuCoordinate = new int[masuTotal][2];

        TypedArray eText = getResources().obtainTypedArray(R.array.event_text_array26);
        TypedArray cText = getResources().obtainTypedArray(R.array.change_text_array26);
        for(int i = 0;i < eventView.length;i++){
            eventView[i] = findViewById(eText.getResourceId(i,0));
            changeView[i] = findViewById(cText.getResourceId(i,0));
        }

        TypedArray baseMasu = getResources().obtainTypedArray(R.array.base_masu);
        for(int i = 0; i < masuTotal;i++){
            linearLayout.add(findViewById(baseMasu.getResourceId(i,0)));
            linearLayout.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(masuEditFlag) {
                        v.setBackgroundResource(R.drawable.masu_background2);
                        masuEditView.visible(linearLayout.indexOf((LinearLayout) v));
                        masuEditView.setEventText(eventView[masuEditView.getNumber()].getText().toString());
                        masuEditView.setChangeMoney(masuData[masuEditView.getNumber()].getChangeMoney());
                    }
                }
            });
        }

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        wDisplay = point.x;
        hDisplay = point.y;

        this.context = this;
    }

    @Override//windowがフォーカスされたときとフォーカスが外れたときの2回呼び出されるので注意
             //初回だけ呼び出したい処理はflagをつけて回避する。
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        wMasu = linearLayout.get(0).getWidth();
        hMasu = linearLayout.get(0).getHeight();
        for(int i =0;i <masuCoordinate.length;i++){
            eventView[i].getLocationInWindow(masuCoordinate[i]);
        }
        if(startFlag) {
            if (read) {
                readData();
            }else {
                for (int i = 0; i<masuTotal; i++){
                    masuData[i] = new MasuData();
                }
            }

            makeSetButton();
            makeDelButton();
            makeSetOrDelWindow();
            makeEditBox();
            startFlag = false;
        }
    }

    public void readData(){
       readSql();
        //コンソールを生成してデータを呼び出す
        Cursor cursor = db.query(
                mapName,//テーブル（表の名前）
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
                    cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));
            eventView[i].setText(masuData[i].getEvent());
            changeView[i].setText(masuData[i].getChangeEvent());

            cursor.moveToNext();
        }
        //SQLの終了
        cursor.close();
    }

    //新しいテーブルを作成
    public void newTableSql(){
        db.execSQL("CREATE TABLE " + mapName +
                " (_id INTEGER PRIMARY KEY,event TEXT,changeEvent TEXT,changeMoney INTEGER," +
                "upNextNumber INTEGER,leftNextNumber INTEGER,downNextNumber INTEGER,rightNextNumber INTEGER)");
    }

    //テーブルを削除
    public void dropTableSql(){
        db.execSQL("DROP TABLE IF EXISTS " + mapName);
    }

    //読み込み用DBの起動
    public void readSql(){
        if(helper == null){
            helper = new MapOpenHelper(getApplicationContext());
        }
        //DBがセットされていない場合はセットする。
        if(db == null){
            db = helper.getReadableDatabase();//読み込み用データベースを取得
        }
    }

    //書き込み用DBの起動
    public void writeSql(){
        if(helper == null){
            helper = new MapOpenHelper(getApplicationContext());
        }
        //DBがセットされていない場合はセットする。
        if(db == null){
            db = helper.getWritableDatabase();//読み込み用データベースを取得
        }
    }

    //新しく書き込まれたデータの読み出し
    public void newReadMasuDate(){
        for(int i = 0; i < masuTotal;i++){
            masuData[i].setEvent(eventView[i].getText().toString());
            masuData[i].setChangeEvent(changeView[i].getText().toString());
            masuData[i].setUpNextNumber(TestSQL.upNextNumber[i]);
            masuData[i].setLeftNextNumber(TestSQL.leftNextNumber[i]);
            masuData[i].setDownNextNumber(TestSQL.downNextNumber[i]);
            masuData[i].setRightNextNumber(TestSQL.rightNextNumber[i]);
        }
    }
    //保存用のボタン作成
    public void makeSetButton(){
        button = new Button(this);
        button.setX(wDisplay / 5.0f * 4);
        button.setY(0.0f);
        button.setText("保存");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrDelWindow.setText();
                setOrDelWindow.visible();
            }
        });

        ConstraintLayout.LayoutParams layer = new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 5.0f), (int) Math.ceil(wDisplay / 8.0f));
        constraintLayout.addView(button,layer);
    }

    public void makeDelButton(){
       button2 = new Button(this);
       button2.setX(0.0f);
       button2.setY(0.0f);
       button2.setText("削除");
       button2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setOrDelWindow.delText();
               setOrDelWindow.visible();
           }
       });

        ConstraintLayout.LayoutParams layer = new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 5.0f), (int) Math.ceil(wDisplay / 8.0f));
        constraintLayout.addView(button2,layer);
    }

    //保存時、削除時のwindowを作成
    public void makeSetOrDelWindow(){
        setOrDelWindow = new SetOrDelWindow(this);
        setOrDelWindow.setX(wDisplay /5.0f);
        setOrDelWindow.setY(hDisplay /3.0f);
        setOrDelWindow.getYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setOrDelWindow.isSetFlag()){
                    writeSql();
                    if(read || newMapFlag){
                        dropTableSql();
                    }
                    newReadMasuDate();
                    newTableSql();
                    for(int i = 0; i < masuTotal;i++) {
                        helper.insertData(db,masuData[i].getEvent(),masuData[i].getChangeEvent(),masuData[i].getChangeMoney(),
                                masuData[i].getUpNextNumber(),masuData[i].getLeftNextNumber(),masuData[i].getDownNextNumber(),
                                masuData[i].getRightNextNumber(),mapName);
                    }
                    read = false;
                    newMapFlag = true;
                    db.close();
                }else {
                    writeSql();
                    if(read || newMapFlag){
                        dropTableSql();
                        read = false;
                        newMapFlag = false;
                    }
                }
                setOrDelWindow.invisible();
            }
        });
        setOrDelWindow.getNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrDelWindow.invisible();
            }
        });
        ConstraintLayout.LayoutParams layer = new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 5.0f *3), ViewGroup.LayoutParams.WRAP_CONTENT);
        constraintLayout.addView(setOrDelWindow,layer);
        setOrDelWindow.invisible();
    }

    //選択したマスの上でViewを生成し、選択したマスのデータを読み込み編集できるようにする。
    public void makeEditBox(){
        masuEditView = new MasuEditView(context);
        masuEditView.setX(wDisplay/10.0f);
        masuEditView.setY(hDisplay/3.0f);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) (wDisplay/10*8), (int)(hDisplay/3));
        constraintLayout.addView(masuEditView, layoutParams);
        masuEditFlag = false;
        masuEditView.getEnd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masuData[masuEditView.getNumber()].setEvent(masuEditView.getEventText());
                masuData[masuEditView.getNumber()].setChangeEvent(masuEditView.getChangeEvent());
                masuData[masuEditView.getNumber()].setChangeMoney(masuEditView.getChangeMoney());
                //
                eventView[masuEditView.getNumber()].setText(masuData[masuEditView.getNumber()].getEvent());
                changeView[masuEditView.getNumber()].setText(masuData[masuEditView.getNumber()].getChangeEvent());
                //
                linearLayout.get(masuEditView.getNumber()).setBackgroundResource(R.drawable.masu_background);
                masuEditFlag = true;
                masuEditView.invisible();
            }
        });
        masuEditView.invisible();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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