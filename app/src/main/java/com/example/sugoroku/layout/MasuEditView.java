package com.example.sugoroku.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sugoroku.R;

public class MasuEditView extends LinearLayout {
    private TextView eventTextEdit,eventMoneyEdit;
    private Button end;
    private LinearLayout frame;
    private Spinner eventSet;
    private int number = 1;

    public MasuEditView(Context context) {
        super(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.edit_box,this);

        this.frame = layout.findViewById(R.id.frame);
        this.eventTextEdit = layout.findViewById(R.id.editText);
        this.eventMoneyEdit = layout.findViewById(R.id.editMoney);
        this.eventSet = layout.findViewById(R.id.event_setting);
        this.end = layout.findViewById(R.id.endbutton);

    }

    public String getEventText(){
        return this.eventTextEdit.getText().toString();
    }
    public void setEventText(String text){
        this.eventTextEdit.setText(text);
    }
    public void setChangeMoney(int money){
        this.eventMoneyEdit.setText("" + money);
    }
    public String getChangeEvent(){
        return (this.eventSet.getSelectedItem().toString() + this.eventMoneyEdit.getText().toString());
    }
    public int getChangeMoney(){return Integer.parseInt(this.eventMoneyEdit.getText().toString());}
    public Button getEnd(){return this.end;}
    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(int number){
        this.number = number;
        this.frame.setVisibility(View.VISIBLE);
    }
    public int getNumber(){
        return this.number;
    }
}
