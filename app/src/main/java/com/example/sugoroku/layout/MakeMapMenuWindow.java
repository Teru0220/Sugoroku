package com.example.sugoroku.layout;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sugoroku.R;

public class MakeMapMenuWindow extends LinearLayout {

    private Button button;
    private Spinner spinner;
    private String mapName;
    private LinearLayout frame;
    private TextView textView1;

    public MakeMapMenuWindow(Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.make_map_setting,this);
        spinner = layout.findViewById(R.id.spinner);
        button = layout.findViewById(R.id.button);
        frame = layout.findViewById(R.id.frame);

        textView1 = layout.findViewById(R.id.map_name);
        mapName = textView1.getText().toString();
    }

    public Button getButton(){
        return this.button;
    }
    public Spinner getSpinner(){
        return this.spinner;
    }
    public String getMapName(){
        mapName = textView1.getText().toString();
        return mapName;
    }
    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(){
        this.frame.setVisibility(View.VISIBLE);
    }
}
