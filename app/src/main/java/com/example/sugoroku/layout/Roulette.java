package com.example.sugoroku.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.sugoroku.R;
//一回タップしたあと乱数を生成しその分回すルーレット
public class Roulette extends FrameLayout implements View.OnClickListener{
    private ImageButton roulette;
    private ImageView frame;
    private boolean flag = true;
    private Bitmap rouletteImg;
    private Bitmap rouletteFrameImg;
    private RotateAnimation rotateAnimation1;
    public static int rouletteNumber;

    public Roulette(@NonNull Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.roulette,this);
        roulette = layout.findViewById(R.id.roulette);
        roulette.setOnClickListener(this);
        rouletteImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.roulette);
        roulette.setImageBitmap(rouletteImg);
        //画像の大きさをViewの大きさに合わせる。
        roulette.setScaleType(ImageView.ScaleType.FIT_XY);

        frame = layout.findViewById(R.id.frame);
        rouletteFrameImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.roulette_needle);
        frame.setImageBitmap(rouletteFrameImg);
        frame.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View v) {
        int random = new java.util.Random().nextInt(360)+1;
        float randomNumber = random +1800.0f ;
        rotateAnimation1= new RotateAnimation(
                0.0f, randomNumber,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation1.setDuration((long) randomNumber);
        rotateAnimation1.setRepeatCount(0);
        rotateAnimation1.setFillAfter(true);
        roulette.startAnimation(rotateAnimation1);
        if (random < 31 || random > 330 ) {
            rouletteNumber = 1;
        }else if(random < 91){
            rouletteNumber = 2;
        }else if(random < 151){
            rouletteNumber = 3;
        }else if(random < 211){
            rouletteNumber = 4;
        }else if(random < 271){
            rouletteNumber = 5;
        }else {
            rouletteNumber = 6;
        }

    }
}
