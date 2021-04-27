package com.example.sugoroku.map;

import android.widget.TextView;

public class MasuData {
    private String event,changeEvent;
    private int changeMoney,upNextNumber,leftNextNumber,downNextNumber,rightNextNumber;
    private TextView textView;

    public MasuData(String event, String changeEvent, int changeMoney,
                    int upNextNumber, int leftNextNumber, int downNextNumber, int rightNextNumber,TextView textView){
        setEvent(event);
        setChangeEvent(changeEvent);
        setChangeMoney(changeMoney);
        setUpNextNumber(upNextNumber);
        setLeftNextNumber(leftNextNumber);
        setDownNextNumber(downNextNumber);
        setRightNextNumber(rightNextNumber);
        setTextView(textView);
    }

    public String getEvent(){ return this.event; }
    public void setEvent(String event){this.event = event;}
    public String getChangeEvent(){ return this.changeEvent; }
    public void setChangeEvent(String changeEvent){this.changeEvent = changeEvent;}
    public int getChangeMoney(){ return this.changeMoney; }
    public void setChangeMoney(int changeMoney){this.changeMoney = changeMoney;}
    public int getUpNextNumber(){ return this.upNextNumber; }
    public void setUpNextNumber(int upNextNumber){this.upNextNumber = upNextNumber;}
    public int getLeftNextNumber(){ return this.leftNextNumber; }
    public void setLeftNextNumber(int leftNextNumber){this.leftNextNumber = leftNextNumber;}
    public int getDownNextNumber(){ return this.downNextNumber; }
    public void setDownNextNumber(int downNextNumber){this.downNextNumber = downNextNumber;}
    public int getRightNextNumber(){ return this.rightNextNumber; }
    public void setRightNextNumber(int rightNextNumber){this.rightNextNumber = rightNextNumber;}
    public TextView getTextView(){ return this.textView; }
    public void setTextView(TextView textView){this.textView = textView;}
}
