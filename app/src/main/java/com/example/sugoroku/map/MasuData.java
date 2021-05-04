package com.example.sugoroku.map;

import android.widget.TextView;

public class MasuData {
    private String event,changeEvent;
    private int changePoint = 0;
    private int upNextNumber,leftNextNumber,downNextNumber,rightNextNumber;
    private int eventNumber = 0;

    public MasuData(){}
    public MasuData(String event, String changeEvent, int changePoint,int eventNumber,
                    int upNextNumber, int leftNextNumber, int downNextNumber, int rightNextNumber){
        setEvent(event);
        setChangeEvent(changeEvent);
        setChangePoint(changePoint);
        setUpNextNumber(upNextNumber);
        setLeftNextNumber(leftNextNumber);
        setDownNextNumber(downNextNumber);
        setRightNextNumber(rightNextNumber);
        setEventNumber(eventNumber);
    }

    public String getEvent(){ return this.event; }
    public void setEvent(String event){this.event = event;}
    public String getChangeEvent(){ return this.changeEvent; }
    public void setChangeEvent(String changeEvent){this.changeEvent = changeEvent;}
    public int getChangePoint(){ return this.changePoint; }
    public void setChangePoint(int changePoint){this.changePoint = changePoint;}
    public int getEventNumber(){return eventNumber;}
    public void setEventNumber(int eventNumber){this.eventNumber = eventNumber;}
    public int getUpNextNumber(){ return this.upNextNumber; }
    public void setUpNextNumber(int upNextNumber){this.upNextNumber = upNextNumber;}
    public int getLeftNextNumber(){ return this.leftNextNumber; }
    public void setLeftNextNumber(int leftNextNumber){this.leftNextNumber = leftNextNumber;}
    public int getDownNextNumber(){ return this.downNextNumber; }
    public void setDownNextNumber(int downNextNumber){this.downNextNumber = downNextNumber;}
    public int getRightNextNumber(){ return this.rightNextNumber; }
    public void setRightNextNumber(int rightNextNumber){this.rightNextNumber = rightNextNumber;}
}
