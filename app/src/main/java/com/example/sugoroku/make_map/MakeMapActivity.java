package com.example.sugoroku.make_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import android.widget.Toast;

import com.example.sugoroku.R;
import com.example.sugoroku.TestSQL;
import com.example.sugoroku.layout.MasuCopyWindow;
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

    private ScrollView scrollView;
    private HorizontalScrollView horizontalScrollView;
    private ConstraintLayout constraintLayout;

    private TextView[] eventView;
    private TextView[] changeView;
    private MasuData[] masuData;
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
    private MasuCopyWindow masuCopyWindow;

    private TypedArray eText;
    private TypedArray cText;
    private TypedArray baseMasu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        masuTotal = getIntent().getIntExtra("masuTotal",0);
        mapName = getIntent().getStringExtra("mapName");
        read = getIntent().getBooleanExtra("read",false);

        if(masuTotal == 26) {
            setContentView(R.layout.activity_map26);
            eText = getResources().obtainTypedArray(R.array.event_text_array26);
            cText = getResources().obtainTypedArray(R.array.change_text_array26);
            baseMasu = getResources().obtainTypedArray(R.array.base_masu_array26);
        }
        scrollView =  findViewById(R.id.vScroll);
        horizontalScrollView =  findViewById(R.id.hScroll);
        constraintLayout = findViewById(R.id.map_activity);

        eventView = new TextView[masuTotal];
        changeView = new TextView[masuTotal];
        masuData = new MasuData[masuTotal];

        for(int i = 0;i < eventView.length;i++){
            eventView[i] = findViewById(eText.getResourceId(i,0));
            changeView[i] = findViewById(cText.getResourceId(i,0));
        }

        for(int i = 0; i < masuTotal-1;i++){
            linearLayout.add(findViewById(baseMasu.getResourceId(i,0)));
            if(i > 0) {
                linearLayout.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (masuEditFlag) {
                            v.setBackgroundResource(R.drawable.masu_background2);
                            masuEditView.visible();
                            masuEditView.setViewNumber(linearLayout.indexOf(v));
                            masuEditView.setEventText(eventView[masuEditView.getViewNumber()].getText().toString());
                            masuEditView.setChangePoint(masuData[masuEditView.getViewNumber()].getChangePoint());
                            int sp = masuData[masuEditView.getViewNumber()].getEventNumber() > 0 ? 1 : 0;
                            masuEditView.getEventSet().setSelection(sp);
                            masuEditFlag = false;
                        }
                    }
                });
                linearLayout.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (masuEditFlag) {
                            v.setBackgroundResource(R.drawable.masu_background2);
                            masuCopyWindow.visible();
                            masuCopyWindow.setViewNumber(linearLayout.indexOf(v));
                            masuEditFlag = false;
                        }
                        return true;
                    }
                });
            }
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
        //マスの幅を固定
        for(int i =0;i <masuTotal;i++){
            eventView[i].setHeight(eventView[i].getHeight());
            changeView[i].setHeight(changeView[0].getHeight());
            eventView[i].setWidth(eventView[i].getWidth());
            changeView[i].setWidth(changeView[i].getWidth());
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
            makeMasuCopyWindow();
            startFlag = false;
        }
    }

    public void readData(){
       readSql();
        //コンソールを生成してデータを呼び出す
        Cursor cursor = db.query(
                mapName,//テーブル（表の名前）
                new String[] { "event", "changeEvent","changePoint" ,"eventNumber" ,"upNextNumber" ,"leftNextNumber" ,"downNextNumber" ,"rightNextNumber"},//呼び出す列名
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
                    cursor.getInt(3), cursor.getInt(4),cursor.getInt(5),cursor.getInt(6),
                    cursor.getInt(7));
            eventView[i].setText(masuData[i].getEvent());
            if(!masuData[i].getEvent().equals("スタート") && !masuData[i].getEvent().equals("ゴール") ) {
                SpannableStringBuilder changeMsg = new SpannableStringBuilder(masuData[i].getChangeEvent());
                int color = masuData[i].getChangePoint() > 0 ? Color.BLUE : Color.RED;
                changeMsg.setSpan(new ForegroundColorSpan(color), 0, changeMsg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                changeView[i].setText(changeMsg);
            }

            cursor.moveToNext();
        }
        //SQLの終了
        cursor.close();
    }

    //新しいテーブルを作成
    public void newTableSql(){
        db.execSQL("CREATE TABLE " + mapName +
                " (_id INTEGER PRIMARY KEY,event TEXT,changeEvent TEXT,changePoint INTEGER,eventNumber INTGER," +
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

        ConstraintLayout.LayoutParams layer =
                new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 5.0f), (int) Math.ceil(wDisplay / 8.0f));
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

        ConstraintLayout.LayoutParams layer =
                new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 5.0f), (int) Math.ceil(wDisplay / 8.0f));
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
                        helper.insertData(db,masuData[i].getEvent(),masuData[i].getChangeEvent(),masuData[i].getChangePoint(),
                                masuData[i].getEventNumber(), masuData[i].getUpNextNumber(),masuData[i].getLeftNextNumber(),
                                masuData[i].getDownNextNumber(), masuData[i].getRightNextNumber(),mapName);
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
        masuEditView.setY(wDisplay / 7.0f);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) (wDisplay/10*8), (int)(hDisplay/5*2.5));
        constraintLayout.addView(masuEditView, layoutParams);
        masuEditView.getWrite().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masuEditView.getEventSet().getSelectedItem().toString().equals("所持金：") ||
                        (masuEditView.getChangePoint() > 0 || masuEditView.getChangePoint() > -(masuEditView.getViewNumber()+1))) {
                masuData[masuEditView.getViewNumber()].setEvent(masuEditView.getEventText());
                masuData[masuEditView.getViewNumber()].setChangeEvent(masuEditView.getChangeEvent());
                masuData[masuEditView.getViewNumber()].setChangePoint(masuEditView.getChangePoint());
                masuData[masuEditView.getViewNumber()].setEventNumber(masuEditView.getEventNumber());
                eventView[masuEditView.getViewNumber()].setText(masuData[masuEditView.getViewNumber()].getEvent());
                changeView[masuEditView.getViewNumber()].setText(masuData[masuEditView.getViewNumber()].getChangeEvent());
                linearLayout.get(masuEditView.getViewNumber()).setBackgroundResource(R.drawable.masu_background);
                masuEditFlag = true;
                masuEditView.invisible();
            } else {
                Toast toast = Toast.makeText(context, "スタート地点より前に戻ることはできません", Toast.LENGTH_LONG);
                toast.show();
            }
            }
        });
        masuEditView.getCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.get(masuEditView.getViewNumber()).setBackgroundResource(R.drawable.masu_background);
                masuEditFlag = true;
                masuEditView.invisible();
            }
        });
        masuEditView.invisible();
    }
    //コピーとペーストのボックスを作成する
    public void makeMasuCopyWindow(){
        masuCopyWindow = new MasuCopyWindow(context);
        masuCopyWindow.setX(wDisplay/3.0f);
        masuCopyWindow.setY(hDisplay / 3.0f);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) (wDisplay/3), (int)(wDisplay/3));
        constraintLayout.addView(masuCopyWindow, layoutParams);
        masuCopyWindow.getCopy().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masuCopyWindow.allSet(
                        masuData[masuCopyWindow.getViewNumber()].getEvent(),
                        masuData[masuCopyWindow.getViewNumber()].getChangeEvent(),
                        masuData[masuCopyWindow.getViewNumber()].getChangePoint(),
                        masuData[masuCopyWindow.getViewNumber()].getEventNumber());
                linearLayout.get(masuCopyWindow.getViewNumber()).setBackgroundResource(R.drawable.masu_background);
                masuEditFlag = true;
                Toast toast = Toast.makeText(context, "コピーしました", Toast.LENGTH_LONG);
                toast.show();
                masuCopyWindow.invisible();
            }
        });
        masuCopyWindow.getPaste().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(masuCopyWindow.getEvent() != null) {
                    masuData[masuCopyWindow.getViewNumber()].setEvent(masuCopyWindow.getEvent());
                    masuData[masuCopyWindow.getViewNumber()].setChangeEvent(masuCopyWindow.getChangeEvent());
                    masuData[masuCopyWindow.getViewNumber()].setChangePoint(masuCopyWindow.getChangePoint());
                    masuData[masuCopyWindow.getViewNumber()].setEventNumber(masuCopyWindow.getEventNumber());
                    eventView[masuCopyWindow.getViewNumber()].setText(masuData[masuCopyWindow.getViewNumber()].getEvent());
                    changeView[masuCopyWindow.getViewNumber()].setText(masuData[masuCopyWindow.getViewNumber()].getChangeEvent());
                    linearLayout.get(masuCopyWindow.getViewNumber()).setBackgroundResource(R.drawable.masu_background);
                    Toast toast = Toast.makeText(context, "データを貼り付けました", Toast.LENGTH_LONG);
                    toast.show();
                    masuCopyWindow.invisible();
                    masuEditFlag = true;
                }else {
                    Toast toast = Toast.makeText(context, "コピーしたデータがありません", Toast.LENGTH_LONG);
                    toast.show();
                    linearLayout.get(masuCopyWindow.getViewNumber()).setBackgroundResource(R.drawable.masu_background);
                    masuCopyWindow.invisible();
                    masuEditFlag = true;
                }
            }
        });
        masuCopyWindow.invisible();
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