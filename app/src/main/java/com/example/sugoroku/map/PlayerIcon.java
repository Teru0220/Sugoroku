package com.example.sugoroku.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import java.sql.Time;
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
    protected int nowPoint = 0;
    int[] wPlayerXY = {0, 0};
    float[] tablePlayerXY = {0.0f, 0.0f};
    float[] tableNextMasuXY = {0.0f, 0.0f};
    int[][] masuCoordinate;

    private ScrollView scrollView;
    private HorizontalScrollView horizontalScrollView;
    public float[] scrollNowXY;
    private int timerCount = 0;

    private Context context;
    private GameMaster gameMaster;

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
        this.scrollNowXY = new float[]{0.0f, 0.0f};
        this.gameMaster = gameMaster;
        TypedArray Img = context.getResources().obtainTypedArray(R.array.icon);
        for (int i = 0;i<iconImege.length;i++)
        this.iconImege[i] = Img.getDrawable(i);
    }

    public void makePlayerIcon(int imgNumber){
        playerImg = new ImageView(context);
        playerImg.setImageDrawable(iconImege[imgNumber]);
        playerImg.setScaleType(ImageView.ScaleType.FIT_XY);
        playerImg.setBackgroundColor(Color.TRANSPARENT);
        playerImg.setX(tablePlayerXY[0]);
        playerImg.setY(tablePlayerXY[1]);
        wPlayerXY = masuCoordinate[0];
        FrameLayout.LayoutParams layer1= new FrameLayout.LayoutParams(musuWidth ,musuHeight);
        frameLayout.addView(playerImg,layer1);
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
            gameMaster.movePoint--;
        }else {
            //最後に止まったマスでイベント発生
            gameMaster.event(masuData.getChangeMoney());
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
                iconMove(upNextMasu);
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
                iconMove(leftNextMasu);
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
                iconMove(downNextMasu);
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
                iconMove(rightNextMasu);
            }
        });
        FrameLayout.LayoutParams arrowLayerH= new FrameLayout.LayoutParams(musuHeight/2,musuWidth/3);
        frameLayout.addView(rightArrow,arrowLayerH);
    }
    //進んだあとに少し時間を開けて矢印を生成する
    private void arrowSetTime(MasuData masuData){
        Handler handler = new Handler();
        TimerTask task = new TimerTask() {//タイマーに伴う作業を設定。
            @Override
            public void run() {
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        makeArrows(masuData);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,110L);
    }

    private void iconMove(int newNextMasu){
        calcXY(wPlayerXY,masuCoordinate[newNextMasu], tableNextMasuXY);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, tablePlayerXY[0],
                Animation.ABSOLUTE, tableNextMasuXY[0],
                Animation.ABSOLUTE, tablePlayerXY[1],
                Animation.ABSOLUTE, tableNextMasuXY[1]
        );
        //スクロール用に移動量を計算,なめらかにスクロールしているようにに見せるためスクロール移動を10分割
        float[] nowXY = {(tablePlayerXY[0] + wPlayerXY[0] - (wDisplay/5.0f)),
                (tablePlayerXY[1] + wPlayerXY[1] - (hDisplay/2.0f))};
        float[] moveXY = {((tableNextMasuXY[0]- tablePlayerXY[0]) / 10.0f),
                ((tableNextMasuXY[1]- tablePlayerXY[1]) / 10.0f)};
        float a = tableNextMasuXY[0];
        float b = tableNextMasuXY[1];
        tablePlayerXY[0] = a;
        tablePlayerXY[1] = b;
        translateAnimation.setDuration(100L);//実行時間ｍｓ
        translateAnimation.setRepeatCount(0);//繰り返し回数
        translateAnimation.setFillAfter(true);//実行後のViewをそのままにするか
        playerImg.startAnimation(translateAnimation);
        //10ミリ秒ごとに10回スクロールを実行
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                nowXY[0] += moveXY[0];
                nowXY[1] += moveXY[1];
                horizontalScrollView.scrollTo((int)Math.ceil(nowXY[0]), 0);
                scrollView.scrollTo(0, (int)Math.ceil(nowXY[1]));
                if(timerCount > 10){
                    timerCount = 0;
                    cancel();
                    scrollNowXY[0] = nowXY[0];
                    scrollNowXY[1] = nowXY[1];
                }
                timerCount++;
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,10);//10ミリ秒ごとに繰り返し

        nowPoint = newNextMasu;
        arrowSetTime(gameMaster.masuData[nowPoint]);
    }

    //次のマスの絶対座標と今のマスの絶対座標の差を計算し、相対座標をすすめる値を求める。
    private void calcXY(int[] now,int[] next,float[] retn){
        retn[0] = (float) (next[0] - now[0]);
        retn[1] = (float) (next[1] - now[1]);
    }

    public ImageView getPlayerImg(){
        return this.playerImg;
    }

    public void scrollNext(){
        horizontalScrollView.scrollTo((int)Math.ceil(scrollNowXY[0]),0);
        scrollView.scrollTo(0,(int)Math.ceil(scrollNowXY[1]));
    }
}
