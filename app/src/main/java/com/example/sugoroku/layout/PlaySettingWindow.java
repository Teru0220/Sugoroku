package com.example.sugoroku.layout;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sugoroku.R;

public class PlaySettingWindow extends LinearLayout {

    private Button button;
    private Spinner spinner;
    private String[] name= new String[4];
    private LinearLayout frame;
    private TextView textView1,textView2,textView3,textView4;

    public PlaySettingWindow(Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.play_setting_popup_view,this);

        spinner = layout.findViewById(R.id.spinner);
        button = layout.findViewById(R.id.button);
        frame = layout.findViewById(R.id.frame);

        textView1 = layout.findViewById(R.id.player_name1);
        textView2 = layout.findViewById(R.id.player_name2);
        textView3 = layout.findViewById(R.id.player_name3);
        textView4 = layout.findViewById(R.id.player_name4);
    }

    public Button getButton(){
        return this.button;
    }
    public Spinner getSpinner(){
        return this.spinner;
    }
    public String[] getName(){
        name[0] = textView1.getText().toString();
        name[1] = textView2.getText().toString();
        name[2] = textView3.getText().toString();
        name[3] = textView4.getText().toString();
        return name;
    }
    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(){
        this.frame.setVisibility(View.VISIBLE);
    }
}
