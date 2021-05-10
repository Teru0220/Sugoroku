package com.example.sugoroku.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sugoroku.R;

public class MasuEditView extends LinearLayout {
    private TextView eventTextEdit, eventPointEdit;
    private Button write,cancel;
    private LinearLayout frame;
    private Spinner eventSet;
    private int viewNumber = 1;
    private int eventNumber = 0;
    private Context context;

    public MasuEditView(Context context) {
        super(context);
        this.context = context;
        View layout = LayoutInflater.from(context).inflate(R.layout.edit_box,this);

        this.frame = layout.findViewById(R.id.frame);
        this.eventTextEdit = layout.findViewById(R.id.editText);
        this.eventPointEdit = layout.findViewById(R.id.editMoney);
        this.eventSet = layout.findViewById(R.id.event_setting);
        this.write = layout.findViewById(R.id.writebutton);
        this.cancel = layout.findViewById(R.id.cancelbutton);

    }

    public String getEventText(){
        return this.eventTextEdit.getText().toString();
    }
    public void setEventText(String text){
        this.eventTextEdit.setText(text);
    }
    public void setChangePoint(int point){
        this.eventPointEdit.setText("" + point);
    }
    public String getChangeEvent(){
        int p = Integer.parseInt(this.eventPointEdit.getText().toString());
        String ev = this.eventSet.getSelectedItem().toString();
        if(ev.equals("所持金：")){
            setEventNumber(0);
            if(p > 0){
                return "所持金が" + p + "ふえた";
            }else {
                return "所持金が" + p + "へった";
            }

        }else {

                if (p > 0) {
                    setEventNumber(2);
                    return p + "マスすすむ";
                } else {
                    setEventNumber(1);
                    return Math.abs(p) + "マスもどる";
                }
        }
    }
    public int getChangePoint(){
        int point = this.eventPointEdit.getText().toString().equals("")
                ? 0 :Integer.parseInt(this.eventPointEdit.getText().toString());
        return point;
    }
    public Spinner getEventSet(){ return this.eventSet; }
    public Button getWrite(){return this.write;}
    public Button getCancel(){return this.cancel;}
    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(){
        this.frame.setVisibility(View.VISIBLE);
    }
    public int getViewNumber(){
        return this.viewNumber;
    }
    public void setViewNumber(int viewNumber){
        this.viewNumber = viewNumber;
    }
    public int getEventNumber(){
        return this.eventNumber;
    }
    public void setEventNumber(int eventNumber){
        this.eventNumber = eventNumber;
    }
}
