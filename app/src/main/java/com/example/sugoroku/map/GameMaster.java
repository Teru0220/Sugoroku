package com.example.sugoroku.map;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sugoroku.layout.GameMenuWindow;
import com.example.sugoroku.layout.Roulette;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.sugoroku.map.MapActivity.wDisplay;

public class GameMaster {
    private Player[] players = new Player[4];
    protected MasuData[] masuData;
    private Context context;
    private Roulette rouletteView;
    private ConstraintLayout constraintLayout;
    protected GameMenuWindow textWindow;
    private int turntable= 0;
    private int turn;
    protected int movePoint = 0;
    private ConstraintLayout.LayoutParams leyer;
    private StringBuilder text = new StringBuilder();

    public GameMaster(Player[] players, MasuData[] masuData, Context context,ConstraintLayout constraintLayout){
        this.players = players;
        this.masuData =masuData;
        this.context = context;
        this.constraintLayout = constraintLayout;
    }

    public void StartGeme() {
        for (int i = 0; i < players.length; i++){
            players[i].getIcon().makePlayerIcon(players[i].getImgNumber());
            players[i].getIcon().getPlayerImg().setVisibility(View.GONE);
        }
        makeTextWindow(players[turn]);
        makeRoulette(players[turn],textWindow);
        text.append(textWindow.getText());
        orderPlay();
    }

    public void orderPlay(){
        turn = turntable % 4;
        players[turn].getIcon().scrollNext();
        players[turn].getIcon().getPlayerImg().setVisibility(View.VISIBLE);
        rouletteView.setVisibility(View.VISIBLE);
        textWindow.setVisibility(View.VISIBLE);
        text.delete(0,text.length());
        text.append(players[turn].getName()).append("\n").append("所持金:").append(players[turn].getMoney());
        textWindow.setText(text.toString());
    }

    public void makeRoulette(Player player,GameMenuWindow textWindow){
        rouletteView = new Roulette(context,this,textWindow);
        rouletteView.setX(player.getIcon().scrollNowXY[0]+ wDisplay -(wDisplay/2.0f));
        rouletteView.setY((player.getIcon().scrollNowXY[1] + MapActivity.hDisplay) -(wDisplay/1.5f));
        leyer= new ConstraintLayout.LayoutParams((int)Math.ceil(wDisplay/2.5f),(int)Math.ceil(wDisplay/2.5f));
        constraintLayout.addView(rouletteView,leyer);
        rouletteView.setVisibility(View.GONE);
    }

    public void makeTextWindow(Player player){
        textWindow = new GameMenuWindow(context);
        textWindow.setX(player.getIcon().scrollNowXY[0]+ wDisplay -(wDisplay/2.5f));
        textWindow.setY(player.getIcon().scrollNowXY[1]);
        leyer= new ConstraintLayout.LayoutParams((int)Math.ceil(wDisplay/2.5f),(int)Math.ceil(wDisplay/5.0f));
        constraintLayout.addView(textWindow,leyer);
        textWindow.setVisibility(View.GONE);
    }

    public void move(int moveCount){
        this.movePoint = moveCount;
        textWindow.visible();
        players[turn].getIcon().makeArrows(masuData[players[turn].getIcon().nowPoint]);
    }

    public void event(int money){
        players[turn].setMoney(players[turn].getMoney() - money);
        text.delete(0,text.length());
        text.append(players[turn].getName()).append("\n").append("所持金:").append(players[turn].getMoney());
        textWindow.setText(text.toString());
        turntable++;
        Handler handler = new Handler();
        TimerTask task = new TimerTask() {//タイマーに伴う作業を設定。
            @Override
            public void run() {
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        players[turn].getIcon().getPlayerImg().setVisibility(View.GONE);
                        orderPlay();
                    }
                });
            }
        };
        Timer timer = new Timer();//タイマーの作成
        timer.schedule(task,1000L);
    }
}
