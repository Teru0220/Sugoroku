package com.example.sugoroku.map;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sugoroku.layout.GameEventMsgWindow;
import com.example.sugoroku.layout.GameMenuWindow;
import com.example.sugoroku.layout.Roulette;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sugoroku.map.MapActivity.hDisplay;
import static com.example.sugoroku.map.MapActivity.wDisplay;

public class GameMaster {
    protected Player[] players;
    protected MasuData[] masuData;
    private Context context;
    private Roulette rouletteView;
    private ConstraintLayout constraintLayout;
    protected GameMenuWindow textWindow;
    private GameEventMsgWindow eventMsgWindow;
    protected int turntable= 0;
    protected int turn;
    protected int movePoint = 0;
    private StringBuilder text = new StringBuilder();
    private boolean endFlag = false;
    private boolean backMove = false;
    public boolean moveEvent = false;

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
        makeTextWindow();
        makeEventMsgWindow();
        makeRoulette();

        text.append(textWindow.getText());
        orderPlay();
    }

    public void orderPlay(){
        turn = turntable % 4;
        players[turn].getIcon().scrollNext();
        players[turn].getIcon().getPlayerImg().setVisibility(View.VISIBLE);
        players[turn].getIcon().getPlayerImg().bringToFront();
        rouletteView.setVisibility(View.VISIBLE);
        if(!players[turn].isGoal()) {
            rouletteView.rotateFlag = true;
            textWindow.visible();
            text.delete(0, text.length());
            text.append(players[turn].getName()).append("\n").append("所持金:").append(players[turn].getMoney());
            textWindow.setText(text.toString());
            if (players[turn].isCpu()) {
                rouletteView.rouletteEvent();
            }
        }else {
            if(turn == 0 && players[0].isGoal() && players[1].isGoal()&& players[2].isGoal()&& players[3].isGoal()){
                gameEng();
            }else {
                event("ゴールボーナス発生", " ", 0,0);
            }
        }
    }
    //メッセージ変更用にtextWindowを渡す。
    public void makeRoulette(){
        rouletteView = new Roulette(context,this,textWindow);
        rouletteView.setX(wDisplay -(wDisplay/2.0f));
        rouletteView.setY((MapActivity.hDisplay) -(wDisplay/2.0f));
        ConstraintLayout.LayoutParams layer1 = new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 2.5f), (int) Math.ceil(wDisplay / 2.5f));
        constraintLayout.addView(rouletteView, layer1);
        rouletteView.setVisibility(View.GONE);
    }

    public void makeTextWindow(){
        textWindow = new GameMenuWindow(context);
        textWindow.setX(wDisplay/5.0f * 3);
        textWindow.setY(0);
        ConstraintLayout.LayoutParams layer2 = new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 5.0f * 2), (int) Math.ceil(wDisplay / 5.0f));
        constraintLayout.addView(textWindow, layer2);
        textWindow.invisible();
    }

    public void makeEventMsgWindow(){
        eventMsgWindow = new GameEventMsgWindow(context);
        eventMsgWindow.setX(wDisplay/6.0f);
        eventMsgWindow.setY(hDisplay/5.0f);

        eventMsgWindow.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(endFlag && turn == 0){((MapActivity)context).finish();}
                if(!players[turn].isCpu()) {
                    eventMsgWindow.invisible();
                    if(!moveEvent) {
                        orderPlay();
                    }
                }
            }
        });

        ConstraintLayout.LayoutParams layer3 = new ConstraintLayout.LayoutParams((int) Math.ceil(wDisplay / 1.5f), (int) Math.ceil(wDisplay / 1.5f));
        constraintLayout.addView(eventMsgWindow, layer3);
        eventMsgWindow.invisible();
    }
    //矢印を生成する回数を調整する
    public void move(int moveCount){
        this.movePoint = moveCount;
        players[turn].getIcon().turnLogList.add(players[turn].getIcon().nowPoint);
        if(!players[turn].isCpu()) {
            players[turn].getIcon().makeArrows(masuData[players[turn].getIcon().nowPoint]);
        }else {
            backMove = false;
            moveCPU();
        }
    }

    //CPUの移動
    public void moveCPU(){
        textWindow.setText("残り　" + movePoint + "マス");
        if(movePoint >0) {
            int[] nextMasu = new int[4];
            for (int i = 0; i < movePoint; i++) {
                nextMasu[3] = masuData[players[turn].getIcon().getNowPoint()].getUpNextNumber();
                nextMasu[2] = masuData[players[turn].getIcon().getNowPoint()].getLeftNextNumber();
                nextMasu[1] = masuData[players[turn].getIcon().getNowPoint()].getDownNextNumber();
                nextMasu[0] = masuData[players[turn].getIcon().getNowPoint()].getRightNextNumber();
            }
            //ゴールできなかったときの処理。logをもどるだけなので改良が必要.またmoveCPUYesOrNoでturuだった場合そのまま進んでいる。
            if(backMove || !moveCPUYesOrNo(nextMasu)){
                if(players[turn].getIcon().logList.size() > 1) {
                    players[turn].getIcon().iconMove(players[turn].getIcon().logList.get(players[turn].getIcon().logList.size() - 2), true);
                    backMove = true;
                }
            }
            //止まったマスのイベント
        }else if(!moveEvent){
            players[turn].getIcon().turnLogList.clear();
            event(masuData[players[turn].getIcon().getNowPoint()].getEvent(),
                    masuData[players[turn].getIcon().getNowPoint()].getChangeEvent(),
                    masuData[players[turn].getIcon().getNowPoint()].getChangePoint(),
                    masuData[players[turn].getIcon().getNowPoint()].getEventNumber());
        }else{
            players[turn].getIcon().turnLogList.clear();
            eventMsgWindow.invisible();
            backMove = false;
            moveEvent = false;
            turntable++;
            orderPlay();
        }
    }
    //イベント
    public void event(String event,String changeEvent,int eventPoint,int eventNumber){
        if(players[turn].getIcon().nowPoint != MapActivity.goalPoint) {
            if(eventNumber == 0) {//お金が増減するイベント
                players[turn].setMoney(players[turn].getMoney() - eventPoint);
                text.delete(0, text.length());
                text.append(players[turn].getName()).append("\n").append("所持金:").append(players[turn].getMoney());
                textWindow.setText(text.toString());
                turntable++;
            }else if(eventNumber == 1){//逆走するイベント
                movePoint = Math.abs(eventPoint);
                backMove = true;
                moveEvent = true;
                moveCPU();
            }else if(eventNumber == 2){
                moveEvent = true;
                move(Math.abs(eventPoint));
            }
            eventWindowShow(event, changeEvent,eventPoint);
        }else{
            //ゴールフラグを建てる
            if(!players[turn].isGoal()){players[turn].setGoal(true);}
            eventWindowShow(event, "ゴールボーナス\n所持金×1.1",eventPoint);
            players[turn].setMoney((int)(players[turn].getMoney() * 1.1));
            text.delete(0, text.length());
            text.append(players[turn].getName()).append("\n").append("所持金:").append(players[turn].getMoney());
            textWindow.setText(text.toString());
            turntable++;
        }

    }

    //イベントのメッセージウィンドウ表示+CPUのターンエンド
    public void eventWindowShow(String event,String changeEvent,int eventPoint){
        SpannableStringBuilder windowMsg =new SpannableStringBuilder(changeEvent);
        int color =  eventPoint > 0 ? Color.BLUE : Color.RED;
        windowMsg.setSpan(new ForegroundColorSpan(color),0,windowMsg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        windowMsg.insert(0,event+"\n\n");
        eventMsgWindow.setMsgView(windowMsg);
        eventMsgWindow.visible();
        if(players[turn].isCpu() && !moveEvent){
            Handler handler = new Handler();
            TimerTask task = new TimerTask() {//タイマーに伴う作業を設定。
                @Override
                public void run() {
                    handler.post(new Runnable(){
                        @Override
                        public void run() {
                            eventMsgWindow.invisible();
                            orderPlay();
                        }
                    });
                }
            };
            Timer timer = new Timer();
            timer.schedule(task,1000L);
        }
    }

    public void gameEng(){
        List<String> ranking = new ArrayList<>();
        int[] money = {players[0].getMoney(),players[1].getMoney(),players[2].getMoney(),players[3].getMoney()};
        java.util.Arrays.sort(money);
        SpannableStringBuilder windowMsg =new SpannableStringBuilder("結果発表\n\n");
        for(int i = money.length -1;i >= 0;i--){
            if(money[i] == players[0].getMoney()){
                ranking.add(players[0].getName());
            }else if(money[i] == players[1].getMoney()){
                ranking.add(players[1].getName());
            }else if(money[i] == players[2].getMoney()){
                ranking.add(players[2].getName());
            }else {
                ranking.add(players[3].getName());
            }
        }
        for (int i = 0; i< ranking.size();i++){
            windowMsg.append("第" + (i + 1) + "位  " + ranking.get(i) + "さん\n\n");
        }
        eventMsgWindow.setMsgView(windowMsg);
        eventMsgWindow.visible();
        endFlag = true;
    }
    //cpuがゴールにつきあたって逆走するか判定する
    public boolean moveCPUYesOrNo(int[] masu){
        for(int i : masu){
            if(!players[turn].getIcon().logList.contains(i) && i > -1) {
                players[turn].getIcon().iconMove(i, true);
                return true;
            }
        }
        return false;
    }
}
