package com.example.sugoroku.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sugoroku.R;
import com.example.sugoroku.map.MapActivity;

public class PopupWindow {

    private Context context;
    private View view;
    private Button button;
    private Spinner spinner;
    private String[] name= new String[4];
    private android.widget.PopupWindow popupWindow;

    public PopupWindow(Context context,View view){
        this.context = context;
        this.view = view;
    }

    public void showWindow(){
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View popupView = inflater.inflate(R.layout.popup_view, null);
        popupView.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        popupWindow = new android.widget.PopupWindow(context);
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0,0);

        spinner = popupView.findViewById(R.id.spinner);
        button = popupView.findViewById(R.id.button);

        TextView textView1 = popupView.findViewById(R.id.player_name1);
        name[0] = textView1.getText().toString();
        TextView textView2 = popupView.findViewById(R.id.player_name2);
        name[1] = textView2.getText().toString();
        TextView textView3 = popupView.findViewById(R.id.player_name3);
        name[2] = textView3.getText().toString();
        TextView textView4 = popupView.findViewById(R.id.player_name4);
        name[3] = textView4.getText().toString();
    }

    public Button getButton(){
        return this.button;
    }
    public Spinner getSpinner(){
        return this.spinner;
    }
    public void endWindow(){
        popupWindow.dismiss();
    }
    public String[] getName(){
        return name;
    }
}
