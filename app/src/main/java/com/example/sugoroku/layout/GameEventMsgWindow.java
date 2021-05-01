package com.example.sugoroku.layout;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sugoroku.R;

public class GameEventMsgWindow extends LinearLayout {

    private LinearLayout frame;
    private TextView msgView;
    private Button button;

    public GameEventMsgWindow(Context context){
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.game_eventmsg_window,this);
        frame = layout.findViewById(R.id.frame);
        msgView = layout.findViewById(R.id.msg);
        button = layout.findViewById(R.id.turn_end);

    }

    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(){
        this.frame.setVisibility(View.VISIBLE);
    }

    public Button getButton(){
        return this.button;
    }
    public TextView getMsgView(){
        return this.msgView;
    }
    public void setMsgView(SpannableStringBuilder msg){ this.msgView.setText(msg);}

}
