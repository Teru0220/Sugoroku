package com.example.sugoroku.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sugoroku.R;

public class MasuCopyWindow extends LinearLayout {
    private LinearLayout frame;
    private TextView copy,paste;
    private int viewNumber;

    private String event,changeEvent;
    private int changePoint = 0;
    private int eventNumber = 0;

    public MasuCopyWindow(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.copy_window,this);

        frame = view.findViewById(R.id.frame);
        copy = view.findViewById(R.id.copy_masu);
        paste = view.findViewById(R.id.paste_masu);
    }

    public void allSet(String event,String changeEvent,int changePoint,int eventNumber){
        setEvent(event);
        setChangeEvent(changeEvent);
        setChangePoint(changePoint);
        setEventNumber(eventNumber);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getChangeEvent() {
        return changeEvent;
    }

    public void setChangeEvent(String changeEvent) {
        this.changeEvent = changeEvent;
    }

    public int getChangePoint() {
        return changePoint;
    }

    public void setChangePoint(int changePoint) {
        this.changePoint = changePoint;
    }

    public int getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(int eventNumber) {
        this.eventNumber = eventNumber;
    }

    public TextView getCopy() {
        return copy;
    }

    public TextView getPaste() {
        return paste;
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public void invisible(){
        this.frame.setVisibility(View.GONE);
    }
    public void visible(){
        this.frame.setVisibility(View.VISIBLE);
    }
}
