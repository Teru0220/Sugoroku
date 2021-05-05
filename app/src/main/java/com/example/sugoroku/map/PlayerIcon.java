package com.example.sugoroku.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.sugoroku.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sugoroku.R.drawable.*;

public class PlayerIcon {
    private ImageView playerImg;
    private Drawable[] iconImege = new Drawable[4];

    private ImageButton upArrow;
    private ImageButton leftArrow;
    private ImageButton downArrow;
    private ImageButton rightArrow;
    private FrameLayout frameLayout;

    private int musuHeight;
    private int musuWidth;
    private int wDisplay;
    private int hDisplay;
    protected int nowPoint = MapActivity.startPoint;//スタート地点を記録
    int[] wPlayerXY = {0, 0};//iconの絶対座標
    float[] tablePlayerXY = new float[2];//iconの相対座標
    float[] tableNextMasuXY = {0.0f, 0.0f};
    float[] iconNowXY = {0.0f,0.0f};
    float[] iconMoveXY = {0.0f,0.0f};
    int[][] masuCoordinate;//map内のすべてのマスの絶対座標
    public List<Integer> logList = new ArrayList<>();
    public List<Integer> turnLogList = new ArrayList<>();

    private ScrollView scrollView;
    private HorizontalScrollView horizontalScrollView;
    public float[] scrollNowXY;//スクロール内の現在地の座標
    private int timerCount = 0;

    private Context context;
    private GameMaster gameMaster;

    private MediaPlayer moveSound;

    public PlayerIcon(Context context, FrameLayout frameLayout, int[][] masuCoordinate,
                      ScrollView scrollView,HorizontalScrollView horizontalScrollView,GameMaster gameMaster){
        this.context = context;
        this.frameLayout = frameLayout;
        this.musuHeight = MapActivity.musuHeight;
        this.musuWidth = MapActivity.musuWidth;
        this.wDisplay = MapActivity.wDisplay;
        this.hDisplay = MapActivity.hDisplay;
        this.masuCoordinate = masuCoordinate;
        this.scrollView = scrollView;
        this.horizontalScrollView = horizontalScrollView;

        wPlayerXY = masuCoordinate[MapActivity.startPoint];
        calcXY(this.masuCoordinate[0],this.wPlayerXY,this.tablePlayerXY);
        this.scrollNowXY = new float[]{(wPlayerXY[0] - (wDisplay/6.0f)),
                (wPlayerXY[1] - (hDisplay/3.0f))};
        this.gameMaster = gameMaster;
        TypedArray Img = context.getResources().obtainTypedArray(R.array.icon);
        for (int i = 0;i<iconImege.length;i++)
        this.iconImege[i] = Img.getDrawable(i);

        this.moveSound = MediaPlayer.create(context,R.raw.move_sound);
    }

    public void makePlayerIcon(int imgNumber){
        playerImg = new ImageView(context);
        playerImg.setImageDrawable(iconImege[imgNumber]);
        playerImg.setScaleType(ImageView.ScaleType.FIT_XY);
        playerImg.setBackgroundColor(Color.TRANSPARENT);
        playerImg.setX(tablePlayerXY[0]);
        playerImg.setY(tablePlayerXY[1]);

        FrameLayout.LayoutParams layer1= new FrameLayout.LayoutParams(musuWidth ,musuHeight);
        frameLayout.addView(playerImg,layer1);
        scrollNext();

        logList.add(MapActivity.startPoint);
    }
    //生成する矢印を指定
    public void makeArrows(MasuData masuData){
        gameMaster.textWindow.setText("残り　" + gameMaster.movePoint + "マス");
        if(gameMaster.movePoint > 0) {
            removeArrows();
            if (masuData.getUpNextNumber() > -1) {
                //次のマスのウィンドウ座標を取得
                makeUpArrow(masuData.getUpNextNumber());
            }
            if (masuData.getLeftNextNumber() > -1) {
                makeLeftArrow(masuData.getLeftNextNumber());
            }
            if (masuData.getDownNextNumber() > -1) {
                makeDownArrow(masuData.getDownNextNumber());
            }
            if (masuData.getRightNextNumber() > -1) {
                makeRightArrow(masuData.getRightNextNumber());
            }
        }else {
            //最後に止まったマスでイベント発生
            turnLogList.clear();
            if(!gameMaster.moveEvent) {
                gameMaster.event(masuData.getEvent(), masuData.getChangeEvent(), masuData.getChangePoint(), masuData.getEventNumber());
            }else {
                gameMaster.moveEvent = false;
                gameMaster.turntable++;
                gameMaster.orderPlay();
            }
        }
    }
    //矢印の破棄
    private void removeArrows(){
        frameLayout.removeView(upArrow);
        frameLayout.removeView(leftArrow);
        frameLayout.removeView(downArrow);
        frameLayout.removeView(rightArrow);
    }
    //上矢印生成
    private void makeUpArrow(int upNextMasu){
        upArrow = new ImageButton(context);
        upArrow.setImageResource(up_arrow);
        upArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        upArrow.setBackgroundColor(Color.TRANSPARENT);
        upArrow.setX(tablePlayerXY[0] + (musuWidth /3.0f));
        upArrow.setY(tablePlayerXY[1] -(musuHeight/3.5f));
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeArrows();
                iconMove(upNextMasu,false);
            }
        });
        //矢印アイコンの大きさ調整
        FrameLayout.LayoutParams arrowLayerH= new FrameLayout.LayoutParams(musuWidth/3 ,musuHeight/2);
        frameLayout.addView(upArrow,arrowLayerH);
    }

    private void makeLeftArrow(int leftNextMasu){
        leftArrow = new ImageButton(context);
        leftArrow.setImageResource(left_arrow);
        leftArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        leftArrow.setBackgroundColor(Color.TRANSPARENT);
        leftArrow.setX(tablePlayerXY[0]);
        leftArrow.setY(tablePlayerXY[1] + musuHeight/4.0f);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeArrows();
                iconMove(leftNextMasu,false);
            }
        });
        FrameLayout.LayoutParams arrowLayerH= new FrameLayout.LayoutParams(musuHeight/2,musuWidth/3);
        frameLayout.addView(leftArrow,arrowLayerH);
    }

    private void makeDownArrow(int downNextMasu){
        downArrow = new ImageButton(context);
        downArrow.setImageResource(down_arrow);
        downArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        downArrow.setBackgroundColor(Color.TRANSPARENT);
        downArrow.setX(tablePlayerXY[0] + (musuWidth /3.0f));
        downArrow.setY(tablePlayerXY[1] + (musuHeight -(musuHeight/6.0f)));
        downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeArrows();
                iconMove(downNextMasu,false);
            }
        });
        FrameLayout.LayoutParams arrowLayerH= new FrameLayout.LayoutParams(musuWidth/3,musuHeight/2);
        frameLayout.addView(downArrow,arrowLayerH);
    }

    private void makeRightArrow(int rightNextMasu){
        rightArrow = new ImageButton(context);
        rightArrow.setImageResource(right_arrow);
        rightArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        rightArrow.setBackgroundColor(Color.TRANSPARENT);
        rightArrow.setX(tablePlayerXY[0] + (musuWidth-(musuHeight / 2.0f)));
        rightArrow.setY(tablePlayerXY[1] + (musuHeight / 4.0f));
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeArrows();
                iconMove(rightNextMasu,false);
            }
        });
        FrameLayout.LayoutParams arrowLayerH= new FrameLayout.LayoutParams(musuHeight/2,musuWidth/3);
        frameLayout.addView(rightArrow,arrowLayerH);
    }
    //進んだあとに少し時間を開けて矢印を生成する
    private void arrowSetTime(MasuData masuData,boolean cpu){
        Handler handler = new Handler();
        TimerTask task = new TimerTask() {//タイマーに伴う作業を設定。
            @Override
            public void run() {
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                       if(!cpu) {
                            makeArrows(masuData);
                       }else {
                            gameMaster.moveCPU();
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        long delay = cpu ? 500L : 110L;
        timer.schedule(task,delay);
    }

    public void iconMove(int newNextMasu,boolean cpu){

        calcXY(wPlayerXY,masuCoordinate[newNextMasu], tableNextMasuXY);
        iconMoveXY[0] += tableNextMasuXY[0];
        iconMoveXY[1] += tableNextMasuXY[1];
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, iconNowXY[0],
                Animation.ABSOLUTE, iconMoveXY[0],
                Animation.ABSOLUTE, iconNowXY[1],
                Animation.ABSOLUTE, iconMoveXY[1]
        );
        //スクロール用に移動量を計算,なめらかにスクロールしているようにに見せるためスクロール移動を10分割
        float[] moveXY = {((tableNextMasuXY[0]) / 10.0f),
                ((tableNextMasuXY[1]) / 10.0f)};
        //変化量を現在の相対座標と、iconの座標に合計
        tablePlayerXY[0] += tableNextMasuXY[0];
        tablePlayerXY[1] += tableNextMasuXY[1];
        iconNowXY[0] += tableNextMasuXY[0];
        iconNowXY[1] += tableNextMasuXY[1];

        translateAnimation.setDuration(100L);//実行時間ｍｓ
        translateAnimation.setRepeatCount(0);//繰り返し回数
        translateAnimation.setFillAfter(true);//実行後のViewをそのままにするか
        playerImg.startAnimation(translateAnimation);
        moveSoundPlay();//効果音再生
        //10ミリ秒ごとに10回スクロールを実行
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                scrollNowXY[0] += moveXY[0];
                scrollNowXY[1] += moveXY[1];
                horizontalScrollView.scrollTo((int)Math.ceil(scrollNowXY[0]), 0);
                scrollView.scrollTo(0, (int)Math.ceil(scrollNowXY[1]));
                if(timerCount > 10){
                    timerCount = 0;
                    cancel();
                }
                timerCount++;
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,10);//10ミリ秒ごとに繰り返し
        //動いたあとに動いたあとのマスをログに登録
        if(turnLogList.size() < 2 ||turnLogList.get(turnLogList.size()-2) != newNextMasu) {
            gameMaster.movePoint--;
            this.turnLogList.add(newNextMasu);
            this.logList.add(newNextMasu);
            if(logList.size() > 2 && logList.get(logList.size()-3) == newNextMasu){
                this.logList.remove(logList.size()-1);
                this.logList.remove(logList.size()-1);
            }
        }else {
            gameMaster.movePoint++;
            this.turnLogList.remove(turnLogList.size() -1);
            this.logList.remove(logList.size()-1);
        }
        this.nowPoint = newNextMasu;
        this.wPlayerXY = masuCoordinate[nowPoint];
        arrowSetTime(gameMaster.masuData[nowPoint],cpu);
    }

    //次のマスの絶対座標と今のマスの絶対座標の差を計算し、すすめる値を求める。
    private void calcXY(int[] now,int[] next,float[] retn){
        retn[0] = (float) (next[0] - now[0]);
        retn[1] = (float) (next[1] - now[1]);
    }

    public int getNowPoint(){return this.nowPoint;}
    public ImageView getPlayerImg(){
        return this.playerImg;
    }

    public void scrollNext(){
        horizontalScrollView.scrollTo((int)Math.ceil(scrollNowXY[0]),0);
        scrollView.scrollTo(0,(int)Math.ceil(scrollNowXY[1]));
    }

    public void moveSoundPlay(){
        moveSound.seekTo(0);
        moveSound.start();
    }
}
