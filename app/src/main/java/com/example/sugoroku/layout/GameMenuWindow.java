package com.example.sugoroku.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sugoroku.R;

//
public class GameMenuWindow extends FrameLayout{
    private TextView text;
    private View frame;

    public GameMenuWindow(@NonNull Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.game_menu_window,this);
        text = layout.findViewById(R.id.status);
        frame = layout.findViewById(R.id.frame);
    }

    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(){
        this.frame.setVisibility(View.VISIBLE);
    }

    public void setText(String t){
        text.setText(t);
    }
    public String getText(){
        return text.getText().toString();
    }
}
