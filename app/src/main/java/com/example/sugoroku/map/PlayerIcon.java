package com.example.sugoroku.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.sugoroku.R;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerIcon {
    private ImageView player;
    private Bitmap playerImg;

    private Bitmap upArrowImg;
    private Bitmap leftArrowImg;
    private Bitmap downArrowImg;
    private Bitmap rightArrowImg;
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


    public PlayerIcon(Context context, FrameLayout frameLayout,
                      int musuHeight, int musuWidth,int wDisplar,int hDisplay,int[][] masuCoordinate,
                      ScrollView scrollView,HorizontalScrollView horizontalScrollView){
        this.context = context;
        this.frameLayout = frameLayout;
        this.musuHeight = musuHeight;
        this.musuWidth = musuWidth;
        this.wDisplay = wDisplar;
        this.hDisplay = hDisplay;
        this.masuCoordinate = masuCoordinate;
        this.scrollView = scrollView;
        this.horizontalScrollView = horizontalScrollView;
        this.scrollNowXY = new float[]{horizontalScrollView.getX(), scrollView.getY()};
        this.playerImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.neko1);
        this.upArrowImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.up_arrow);
        this.leftArrowImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.left_arrow);
        this.downArrowImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.down_arrow);
        this.rightArrowImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.right_arrow);
    }

    public void makePlayerIcon(){
        player = new ImageView(context);
        player.setImageBitmap(playerImg);
        player.setBackgroundColor(Color.TRANSPARENT);
        player.setX(tablePlayerXY[0]);
        player.setY(tablePlayerXY[1]);
        wPlayerXY = masuCoordinate[0];
        FrameLayout.LayoutParams layer1= new FrameLayout.LayoutParams(musuWidth ,musuHeight);
        frameLayout.addView(player,layer1);
    }
    //生成する矢印を指定
    public void makeArrows(MasuData masuData){
        removeArrows();

        if(masuData.getUpNextNumber() > -1){
            //次のマスのウィンドウ座標を取得
            makeUpArrow(masuData.getUpNextNumber());
        }
        if(masuData.getLeftNextNumber() > -1){
            makeLeftArrow(masuData.getLeftNextNumber());
        }
        if(masuData.getDownNextNumber() > -1){
            makeDownArrow(masuData.getDownNextNumber());
        }
        if(masuData.getRightNextNumber() > -1){
            makeRightArrow(masuData.getRightNextNumber());
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
        upArrow.setImageBitmap(upArrowImg);
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
        leftArrow.setImageBitmap(leftArrowImg);
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
        downArrow.setImageBitmap(downArrowImg);
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
        rightArrow.setImageBitmap(rightArrowImg);
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

    private void iconMove(int newNextMasu){
        calcXY(wPlayerXY,masuCoordinate[newNextMasu], tableNextMasuXY);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, tablePlayerXY[0],
                Animation.ABSOLUTE, tableNextMasuXY[0],
                Animation.ABSOLUTE, tablePlayerXY[1],
                Animation.ABSOLUTE, tableNextMasuXY[1]
        );
        //スクロール用に移動量を計算
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
        player.startAnimation(translateAnimation);
        //なめらかにスクロールしているようにに見せるためスクロール移動を10分割
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
                }
                timerCount++;
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,0,10);//10ミリ秒ごとに繰り返し
        this.scrollNowXY[0] = horizontalScrollView.getX();
        this.scrollNowXY[1] = scrollView.getY();

        nowPoint = newNextMasu;
    }
    //次のマスの絶対座標と今のマスの絶対座標の差を計算し、相対座標をすすめる値を求める。
    private void calcXY(int[] now,int[] next,float[] retn){
        retn[0] = (float) (next[0] - now[0]);
        retn[1] = (float) (next[1] - now[1]);
    }

}
