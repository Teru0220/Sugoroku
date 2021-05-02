package com.example.sugoroku.layout;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.sugoroku.R;
import com.example.sugoroku.map.GameMaster;

import java.util.Timer;
import java.util.TimerTask;

//一回タップしたあと乱数を生成しその分回すルーレット
public class Roulette extends FrameLayout implements View.OnClickListener{
    private ImageButton roulette;
    private ImageView frame;
    private RotateAnimation rotateAnimation1;
    public int rouletteNumber;
    private float randomTime;
    public boolean rotateFlag;

    private GameMenuWindow textWindow;
    private GameMaster gameMaster;

    public Roulette(@NonNull Context context,GameMaster gameMaster,GameMenuWindow textWindow) {
        super(context);
        this.textWindow = textWindow;
        this.gameMaster = gameMaster;

        View layout = LayoutInflater.from(context).inflate(R.layout.roulette,this);
        roulette = layout.findViewById(R.id.roulette);
        roulette.setOnClickListener(this);
        roulette.setImageResource(R.drawable.roulette);
        //画像の大きさをViewの大きさに合わせる。
        roulette.setScaleType(ImageView.ScaleType.FIT_XY);

        frame = layout.findViewById(R.id.frame);
        frame.setImageResource(R.drawable.roulette_needle);
        frame.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View v) {
        rouletteEvent();
    }

    public void rouletteEvent(){
        if(rotateFlag) {
            rotateFlag = false;
            //ランダムで生成した数値+20回ルーレットを回す。
            int random = new java.util.Random().nextInt(360) + 1;
            randomTime = random + 1800.0f;
            rotateAnimation1 = new RotateAnimation(
                    0.0f, randomTime,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            rotateAnimation1.setDuration((long) randomTime);
            rotateAnimation1.setRepeatCount(0);
            rotateAnimation1.setFillAfter(true);
            roulette.startAnimation(rotateAnimation1);
            //乱数からすすめる目を算出
            if (random < 31 || random > 330) {
                rouletteNumber = 1;
            } else if (random < 91) {
                rouletteNumber = 2;
            } else if (random < 151) {
                rouletteNumber = 3;
            } else if (random < 211) {
                rouletteNumber = 4;
            } else if (random < 271) {
                rouletteNumber = 5;
            } else {
                rouletteNumber = 6;
            }
            //ルーレットの回転が収まって少ししてからルーレットを消す。
            invisible();
        }
    }

    public void invisible(){
        Roulette rouletteView = this;
        Handler handler = new Handler();
        TimerTask task = new TimerTask() {//タイマーに伴う作業を設定。
            @Override
            public void run() {
                handler.post(new Runnable(){
                        @Override
                        public void run() {
                            rouletteView.setVisibility(View.GONE);
                            gameMaster.move(rouletteNumber);
                    }
                });
            }
        };
        Timer timer = new Timer();//タイマーの作成
        timer.schedule(task,(long)randomTime + 1000L);
    }

   @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(rotateFlag){
            super.onTouchEvent(event);
        }
        return true;
    }
}
